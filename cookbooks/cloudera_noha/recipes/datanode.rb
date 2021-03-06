
include_recipe "cloudera_noha"
include_recipe "cloudera_noha::hadoop_conf"

include_recipe "iptables"

package "hadoop-hdfs-datanode"

simple_firewall "open datanode ports" do
  ports node[:hadoop][:dn][:ports].values
  action :open
end

node[:hadoop][:conf][:hdfs_site]['dfs.datanode.data.dir'].split(",").each do |local_dir|
  directory local_dir do
    owner "hdfs"
    group "hadoop"
    mode 0700
    recursive true
  end
end

service "hadoop-hdfs-datanode" do 
  action :nothing
  supports :status => true, :start => true, :stop => true, :restart => true
end