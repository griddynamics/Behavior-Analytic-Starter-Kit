
if node.roles.include?("sqoop_metastore")
  sqoop_metastore_node = node
else
  sqoop_metastore_nodes = search(:node, "roles:sqoop_metastore AND chef_environment:#{node.chef_environment}")

  if sqoop_metastore_nodes.empty?
    Chef::Log.fatal("Nodes with 'sqoop_metastore' role not found!")
    raise
  elsif sqoop_metastore_nodes.length == 1
    sqoop_metastore_node = sqoop_metastore_nodes[0]
  else
    Chef::Log.fatal("Multiple nodes with 'sqoop_metastore' role was found!")
    raise
  end
end

node.set[:sqoop][:conf][:sqoop_site]["sqoop.metastore.client.autoconnect.url"] =
    "jdbc:hsqldb:hsql://#{sqoop_metastore_node.ipaddress}:#{node[:sqoop][:metastore][:port]}/sqoop"

directory node[:sqoop][:env_conf_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

template "#{node[:sqoop][:env_conf_path]}/sqoop-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:sqoop][:conf][:sqoop_site])
  mode 0644
  owner "root"
  group "root"
end

bash "Reset alternatives for sqoop conf" do
  not_if do
    ::File.symlink?(node[:sqoop][:conf_path]) &&
        ::File.readlink(node[:sqoop][:conf_path]) == "/etc/alternatives/sqoop-conf" &&
        ::File.symlink?("/etc/alternatives/sqoop-conf") &&
        ::File.readlink("/etc/alternatives/sqoop-conf") == node[:sqoop][:env_conf_path]
  end
  code <<-EOH
  alternatives --install #{node[:sqoop][:conf_path]} sqoop-conf #{node[:sqoop][:env_conf_path]} 1000000
  alternatives --set sqoop-conf #{node[:sqoop][:env_conf_path]}
  EOH
end
