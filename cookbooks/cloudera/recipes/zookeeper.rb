
include_recipe "cloudera"

include_recipe "iptables"

package "zookeeper"
package "zookeeper-server"

simple_firewall "open zookeeper ports" do
  ports node[:zookeeper][:ports].values
  action :open
end

directory node[:zookeeper][:env_conf_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

template "#{node[:zookeeper][:env_conf_path]}/zoo.cfg" do
  source "zookeeper/zoo.cfg.erb"
  variables node[:zookeeper]
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:zookeeper][:env_conf_path]}/java.env" do
  source "zookeeper/java.env.erb"
  mode 0644
  owner "root"
  group "root"
end

cookbook_file "#{node[:zookeeper][:env_conf_path]}/log4j.properties" do
  source "zookeeper/log4j.properties"
  mode 0644
  owner "root"
  group "root"
end

bash "Reset alternatives for zookeeper conf" do
  not_if do
    ::File.symlink?("/etc/alternatives/zookeeper-conf") &&
        ::File.readlink("/etc/alternatives/zookeeper-conf") == node[:zookeeper][:env_conf_path]
  end
  code <<-EOH
  alternatives --install #{node[:zookeeper][:conf_path]} zookeeper-conf #{node[:zookeeper][:env_conf_path]} 1000000
  alternatives --set zookeeper-conf #{node[:zookeeper][:env_conf_path]}
  EOH
end

Chef::Log.info(node.hostname)

execute "zookeeper initialization" do
  not_if { ::File.exists?("#{node[:zookeeper][:data_dir]}/myid") }
	command "service zookeeper-server init --myid=#{node[:zookeeper][:hosts].key(node.hostname)} --force"
	action :run
end
