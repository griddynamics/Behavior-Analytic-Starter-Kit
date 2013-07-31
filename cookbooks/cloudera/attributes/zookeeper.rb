
include_attribute "cloudera"

default[:zookeeper][:conf_path] = "/etc/zookeeper/conf"
default[:zookeeper][:env_conf_path] = "#{node[:zookeeper][:conf_path]}.#{node.chef_environment}"
default[:zookeeper][:data_dir] = "/var/lib/zookeeper"

default[:zookeeper][:hosts] = {
    "1" => node[:cluster][:hosts][:hm1],
    "2" => node[:cluster][:hosts][:hm2],
    "3" => node[:cluster][:hosts][:hm3],
}

default[:zookeeper][:ports] = {
    :client => "2181",
    :server => "2888",
    :other => "3888",
}

default[:zookeeper][:quorum] = node[:zookeeper][:hosts].values.map do |zk_host|
  "#{zk_host}:#{node[:zookeeper][:ports][:client]}"
end
