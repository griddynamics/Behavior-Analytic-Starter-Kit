
include_attribute "cloudera"

default[:hive][:version] = "0.10.0"

default[:hive][:conf_path] = "/etc/hive/conf"
default[:hive][:env_conf_path] = "#{node[:hive][:conf_path]}.#{node.chef_environment}"
default[:hive][:home_path] = "/usr/lib/hive"
default[:hive][:lib_path]  = "#{node[:hive][:home_path]}/lib"

default[:hive][:metastore][:port] = 9083

default[:hive][:metastore][:db][:name]   = "hive_metastore"
default[:hive][:metastore][:db][:user]   = "hive"


############################
# Hive configuration files #
############################

default[:hive][:conf][:hive_site] = {
    "hive.metastore.warehouse.dir" => "/user/hive/warehouse",
    "hive.metastore.local" => "false",
    "hive.metastore.uris" => "",

    #"hive.exec.compress.output" => "true",
    #"hive.exec.compress.intermediate" => "true",
}
