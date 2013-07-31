include_recipe 'ganglia'

##############################################################################
Chef::Log.info("Setting up Ganglia gmetad from package")
##############################################################################

package "ganglia-gmetad"

#node_ips = search(:node, "*:* AND chef_environment:#{node.chef_environment}").map {|node| node.ipaddress}
node_ips = %w(127.0.0.1)

directory "/etc/ganglia"

template "/etc/ganglia/gmetad.conf" do
  source "gmetad.conf.erb"
  variables( :hosts => node_ips.join(" "))
  mode 00644
  owner "root"
  group "root"
  notifies :restart, "service[gmetad]"
end

################################################# #############################
Chef::Log.info("Starting gmetad service")
##############################################################################

service "gmetad" do
  supports :restart => true
  action [ :enable, :start ]
end

##############################################################################
Chef::Log.info("Ganglia gmetad service installed successfully!")
##############################################################################
