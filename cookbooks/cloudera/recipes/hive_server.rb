
include_recipe "cloudera"
include_recipe "cloudera::hive_conf"

package "hive-server2"

service "hive-server2" do
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end
