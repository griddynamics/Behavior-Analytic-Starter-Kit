
include_recipe "cloudera"
include_recipe "cloudera::hadoop_conf"

include_recipe "iptables"

if node[:hadoop][:mr][:ha]
  package "hadoop-0.20-mapreduce-jobtrackerha"
  package "hadoop-0.20-mapreduce-zkfc"
else
  package "hadoop-0.20-mapreduce-jobtracker"
end

package "hue-plugins" if node[:hue][:enabled]

simple_firewall "open jobtracker ports" do
  ports node[:hadoop][:jt][:ports].values
  action :open
end

node[:hadoop][:conf][:mapred_site]['mapred.local.dir'].split(",").each do |mapred_dir|
  directory mapred_dir do
    owner "mapred"
    group "hadoop"
    mode 0700
    recursive true
  end
end

if node[:hadoop][:mr][:ha]
  service "hadoop-0.20-mapreduce-zkfc" do
    action :start
  end
  service "hadoop-0.20-mapreduce-jobtrackerha" do
      action :start
  end
else 
  service "hadoop-0.20-mapreduce-zkfc" do
    action :start
  end
end