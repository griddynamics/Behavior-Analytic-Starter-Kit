
include_attribute "cloudera"

default[:sqoop][:conf_path] = "/etc/sqoop/conf"
default[:sqoop][:env_conf_path] = "#{node[:sqoop][:conf_path]}.#{node.chef_environment}"

default[:sqoop][:home_path] = "/usr/lib/sqoop"
default[:sqoop][:lib_path]  = "#{node[:sqoop][:home_path]}/lib"

default[:sqoop][:metastore][:port] = 16000


#############################
# Sqoop configuration files #
#############################

default[:sqoop][:conf][:sqoop_site] = {
    "sqoop.metastore.server.location" => "/var/lib/sqoop-metastore/shared.db",
    "sqoop.metastore.server.port" => node[:sqoop][:metastore][:port],

    "sqoop.metastore.client.record.password" => "true",
}
