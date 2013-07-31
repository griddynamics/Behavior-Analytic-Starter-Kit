
include_recipe "cloudera"
include_recipe "cloudera::hadoop_conf"

include_recipe "iptables"

package "hadoop-hdfs-namenode"
package "hadoop-hdfs-zkfc" if node[:hadoop][:hdfs][:ha]

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

if node[:hadoop][:hdfs][:ha] && node[:hadoop][:nn][:format]
    execute "HDFS namenode format" do
        command "sudo -u hdfs hdfs namenode -format -force"
        not_if { ::File.exist?("/data/1/dfs/nn/") }
    end
    
    execute "ZKFC format" do
        command "sudo -u hdfs zkfc -formatZK -force"
        not_if { ::File.exist?("/data/1/dfs/nn/") }
    end

    service "hadoop-hdfs-zkfc" do
        action :start
    end

    node[:hadoop][:nn][:format] = false
elif node[:hadoop][:hdfs][:ha] && not node[:hadoop][:nn][:format] do
    # execute "HDFS namenode format" do
    #     command "sudo -u hdfs hdfs namenode -bootstrapStandby"
    #     not_if { ::File.exist?("/data/1/dfs/nn/") }
    # end
    #
    # Doesn't work properly die CDH4 bug https://issues.apache.org/jira/browse/HDFS-3752
else node[:hadoop][:hdfs][:ha]
    execute "HDFS namenode init" do
        command "sudo service hadoop-hdfs-namenode init"
        not_if { ::File.exist?("/data/1/dfs/nn/") }
    end
end

service "hadoop-hdfs-namenode" do
    action :start
end

