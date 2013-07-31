
include_attribute "cloudera"
include_attribute "cloudera::hadoop"

default[:httpfs][:conf_path]     = "/etc/hadoop-httpfs/conf"
default[:httpfs][:env_conf_path] = "#{node[:httpfs][:conf_path]}.#{node.chef_environment}"
default[:httpfs][:home_path]     = "/usr/lib/hadoop-httpfs"
default[:httpfs][:data_path]     = "/var/lib/hadoop-httpfs"
default[:httpfs][:temp_path]     = "/var/tmp/hadoop-httpfs"

default[:httpfs][:server][:port] = 14000

default[:httpfs][:conf][:httpfs_site] = {
    "httpfs.hadoop.config.dir" => node[:hadoop][:conf_path]
}

node[:httpfs][:proxy_users].each do |proxy_user|
  default[:httpfs][:conf][:httpfs_site]["httpfs.proxyuser.#{proxy_user}.hosts"] = "*"
  default[:httpfs][:conf][:httpfs_site]["httpfs.proxyuser.#{proxy_user}.groups"] = "*"
end
