
if node.roles.include?("hive_metastore")
  hive_metastore_node = node
else
  hive_metastore_nodes = search(:node, "roles:hive_metastore AND chef_environment:#{node.chef_environment}")

  if hive_metastore_nodes.empty?
    Chef::Log.fatal("Nodes with 'hive_metastore' role not found!")
    raise
  elsif hive_metastore_nodes.length == 1
    hive_metastore_node = hive_metastore_nodes[0]
  else
    Chef::Log.fatal("Multiple nodes with 'hive_metastore' role was found!")
    raise
  end
end

node.set_unless[:hive][:conf][:hive_site]["hive.metastore.uris"] =
    "thrift://#{hive_metastore_node.ipaddress}:#{node[:hive][:metastore][:port]}"

directory node[:hive][:env_conf_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

template "#{node[:hive][:env_conf_path]}/hive-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:hive][:conf][:hive_site])
  mode 0644
  owner "root"
  group "root"
end

cookbook_file "#{node[:hive][:env_conf_path]}/hive-log4j.properties" do
  source "hive/hive-log4j.properties"
  mode 0644
  owner "root"
  group "root"
end

cookbook_file "#{node[:hive][:env_conf_path]}/hive-exec-log4j.properties" do
  source "hive/hive-exec-log4j.properties"
  mode 0644
  owner "root"
  group "root"
end

bash "Reset alternatives for hive conf" do
  not_if do
    ::File.symlink?(node[:hive][:conf_path]) &&
        ::File.readlink(node[:hive][:conf_path]) == "/etc/alternatives/hive-conf" &&
        ::File.symlink?("/etc/alternatives/hive-conf") &&
        ::File.readlink("/etc/alternatives/hive-conf") == node[:hive][:env_conf_path]
  end
  code <<-EOH
  alternatives --install #{node[:hive][:conf_path]} hive-conf #{node[:hive][:env_conf_path]} 1000000
  alternatives --set hive-conf #{node[:hive][:env_conf_path]}
  EOH
end
