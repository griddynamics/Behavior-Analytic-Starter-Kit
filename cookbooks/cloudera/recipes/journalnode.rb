
include_recipe "cloudera"
include_recipe "cloudera::hadoop_conf"

include_recipe "iptables"

package "hadoop-hdfs-journalnode"

simple_firewall "open journalnode ports" do
  ports node[:hadoop][:jn][:ports].values
  action :open
end

directory node[:hadoop][:conf][:hdfs_site]['dfs.journalnode.edits.dir'] do
  owner "hdfs"
  group "hadoop"
  mode 0700
  recursive true
end
