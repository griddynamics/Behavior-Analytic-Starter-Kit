
include_recipe 'ganglia'
include_recipe 'iptables'

include_recipe 'selinux::disabled'

include_recipe 'apache2'
include_recipe 'apache2::mod_rewrite'
include_recipe 'apache2::mod_php5'

##############################################################################
Chef::Log.info("Setting up Ganglia web from package")
##############################################################################

package "ganglia-web"

simple_firewall "open http port" do
  ports %w( 80 )
  action :open
end

##############################################################################
Chef::Log.info("Fix configs")
##############################################################################

bash "Correct timezone in php.ini" do
  code <<-EOH
  sed -i "s%;date.timezone =%date.timezone = $(cat /etc/sysconfig/clock | awk -F= {'print $2'})%g" /etc/php.ini
  EOH
end

##############################################################################
Chef::Log.info("Adding Ganglia virtual host to httpd")
##############################################################################

apache_site "000-default" do
  enable false
end

template "#{node['apache']['dir']}/sites-available/ganglia.conf" do
  source "httpd.conf.erb"
  mode 00644
  variables( :public_domain => node['public_domain'] || node['domain'] )
  if ::File.symlink?("#{node['apache']['dir']}/sites-enabled/ganglia.conf")
    notifies :reload, "service[apache2]"
  end
end

file "#{node['apache']['dir']}/conf.d/ganglia.conf" do
  action :delete
end

apache_site "ganglia.conf"

##############################################################################
Chef::Log.info("Ganglia gmond service installed successfully!")
##############################################################################
