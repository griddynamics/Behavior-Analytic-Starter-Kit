
include_recipe "cloudera"
include_recipe "cloudera::sqoop_conf"

package "sqoop-metastore"

directory ::File.dirname(node[:sqoop][:conf][:sqoop_site]["sqoop.metastore.server.location"])  do
  owner "sqoop"
  group "sqoop"
  mode 0750
  recursive true
end

simple_firewall "open sqoop metastore port" do
  ports [ node[:sqoop][:metastore][:port] ]
  action :open
end

service "sqoop-metastore" do
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end
