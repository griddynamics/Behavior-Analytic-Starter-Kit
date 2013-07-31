
include_recipe "cloudera_noha"
include_recipe "cloudera_noha::hadoop_conf"

include_recipe "iptables"

package "hadoop-hdfs-namenode"

simple_firewall "open namenode-ha ports" do
  ports node[:hadoop][:nn][:ports].values
  action :open
end

node[:hadoop][:conf][:hdfs_site]['dfs.namenode.name.dir'].split(",").each do |local_dir|
  directory local_dir do
    owner "hdfs"
    group "hadoop"
    mode 0700
    recursive true
  end
end

execute "HDFS namenode init" do
  command "service hadoop-hdfs-namenode init"
  user "root"
  only_if { node[:hadoop][:nn][:format] }
end

service "hadoop-hdfs-namenode" do
  action :nothing
  supports :status => true, :start => true, :stop => true, :restart => true
end