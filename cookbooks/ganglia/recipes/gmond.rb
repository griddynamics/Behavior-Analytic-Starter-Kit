include_recipe 'ganglia'
include_recipe 'iptables'

##############################################################################
Chef::Log.info("Setting up Ganglia gmond from package")
##############################################################################

package "ganglia-gmond"

simple_firewall "open tcp gmond ports" do
  ports [
            node['ganglia']['gmond']['ports']['tcp_accept']
        ]
  action :open
end

simple_firewall "open udp gmond ports" do
  protocol :udp
  ports [
            node['ganglia']['gmond']['ports']['udp_accept']
        ]
  action :open
end

directory "#{node['ganglia']['conf_dir']}/conf.d" do
  not_if { Dir.exists?("#{node['ganglia']['conf_dir']}/conf.d") }
  recursive true
end

ganglia_server = node[:hadoop][:nn][:host]

if ganglia_server.empty?
    collector_gmond_host = "127.0.0.1"
else
    collector_gmond_host = ganglia_server
end

template "/etc/ganglia/gmond.conf" do
  source "gmond.conf.erb"
  variables(:collector_gmond_host => collector_gmond_host)
  mode 00644
  owner "root"
  group "root"
  notifies :restart, "service[gmond]"
end

##############################################################################
Chef::Log.info("Starting gmond service")
##############################################################################

service "gmond" do
  supports :restart => true
  action [ :enable, :start ]
end

##############################################################################
Chef::Log.info("Ganglia gmond service installed successfully!")
##############################################################################
