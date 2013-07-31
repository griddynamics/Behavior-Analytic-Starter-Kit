
include_recipe 'apache2'
include_recipe 'apache2::mod_rewrite'
include_recipe 'apache2::mod_php5'
include_recipe 'apache2::mod_cgi'

if node['nagios']['enable_ssl']
  include_recipe "apache2::mod_ssl"
end

apache_site "000-default" do
  enable false
end

template "#{node['apache']['dir']}/sites-available/nagios.conf" do
  source "httpd.conf.erb"
  mode 00644
  variables( :public_domain => node['public_domain'] || node['domain'] )
  if ::File.symlink?("#{node['apache']['dir']}/sites-enabled/nagios.conf")
    notifies :reload, "service[apache2]"
  end
end

file "#{node['apache']['dir']}/conf.d/nagios.conf" do
  action :delete
end

apache_site "nagios.conf"
