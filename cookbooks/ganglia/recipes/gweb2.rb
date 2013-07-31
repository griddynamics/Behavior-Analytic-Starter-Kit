
include_recipe 'iptables'

include_recipe 'selinux::disabled'

include_recipe 'apache2'
include_recipe 'apache2::mod_rewrite'
include_recipe 'apache2::mod_php5'

package "wget"

##############################################################################
Chef::Log.info("Setting up Ganglia web from package")
##############################################################################

bash "Install gweb2" do
  cwd Chef::Config[:file_cache_path]
  not_if { ::File.exists?(node['ganglia']['gweb2']['docroot']) }

  code <<-EOH
  wget "#{node['ganglia']['gweb2']['uri']}" -O ganglia-web-#{node['ganglia']['gweb2']['version']}.tar.gz
  tar -xzf ganglia-web-#{node['ganglia']['gweb2']['version']}.tar.gz
  cp -r ganglia-web-#{node['ganglia']['gweb2']['version']} #{node['ganglia']['gweb2']['docroot']}

  mkdir -p "#{node['ganglia']['gweb2']['docroot']}/conf/dwoo/compiled"
  mkdir -p "#{node['ganglia']['gweb2']['docroot']}/conf/dwoo/cache"
  chown -R #{node['apache']['user']}.#{node['ganglia']['group']} "#{node['ganglia']['gweb2']['docroot']}/conf"

  GWEB2_CONF_FILE="#{node['ganglia']['gweb2']['docroot']}/conf_default.php"
  cat ${GWEB2_CONF_FILE}.in > ${GWEB2_CONF_FILE}

  sed -i "s|@vargwebstatedir@|#{node['ganglia']['gweb2']['docroot']}/conf|g" ${GWEB2_CONF_FILE}
  sed -i "s|@vargmetadir@|#{node['ganglia']['state_dir']}|g" ${GWEB2_CONF_FILE}
  sed -i "s|\$conf\['auth_system'\] = .*;|\$conf['auth_system'] = 'disabled';|g" ${GWEB2_CONF_FILE}
  sed -i "s|\$conf\['external_location'\] = .*;|\$conf['external_location'] = 'http://#{node.ipaddress}/ganglia2';|g" ${GWEB2_CONF_FILE}
  EOH
end

simple_firewall "open http port" do
  ports %w( 80 )
  action :open
end

##############################################################################
Chef::Log.info("Fix configs")
##############################################################################

bash "Correct timezone in php.ini" do
  code <<-EOH
  sed -i "s%;date.timezone =%date.timezone = $(grep ZONE= /etc/sysconfig/clock | awk -F= {'print $2'})%g" /etc/php.ini
  EOH
end

##############################################################################
Chef::Log.info("Adding Ganglia virtual host to httpd")
##############################################################################

apache_site "000-default" do
  enable false
end

template "#{node['apache']['dir']}/sites-available/gweb2.conf" do
  source "gweb2.httpd.conf.erb"
  mode 00644
  if ::File.symlink?("#{node['apache']['dir']}/sites-enabled/gweb2.conf")
    notifies :reload, "service[apache2]"
  end
end

apache_site "gweb2.conf"

##############################################################################
Chef::Log.info("Ganglia gmond service installed successfully!")
##############################################################################
