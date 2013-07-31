
include_recipe "cloudera_noha"
include_recipe "cloudera_noha::hadoop_conf"

include_recipe "iptables"

package "hadoop-0.20-mapreduce-tasktracker"

simple_firewall "open tasktracker ports" do
  ports node[:hadoop][:tt][:ports].values
  action :open
end

node[:hadoop][:conf][:mapred_site]['mapred.local.dir'].split(",").each do |mapred_dir|
  directory mapred_dir do
    owner "mapred"
    group "hadoop"
    mode 0755
    recursive true
  end
end

service "hadoop-0.20-mapreduce-tasktracker" do
  action :nothing
  supports :status => true, :start => true, :stop => true, :restart => true
end
