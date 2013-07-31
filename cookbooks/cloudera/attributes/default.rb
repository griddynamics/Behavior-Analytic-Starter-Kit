
default[:cloudera][:cdh_version] = "4.2.0"
default[:cloudera][:jdk][:uri] = "http://download.oracle.com/otn-pub/java/jdk/6u31-b04/jdk-6u31-linux-x64-rpm.bin"
default[:cloudera][:jdk][:home] = "/usr/java/default"

default[:cluster][:hosts] = {
    :hm1 => "hmaster1",
    :hm2 => "hmaster2",
    :hm3 => "hmaster3",
}

default[:system][:time_zone_name] = "America/Los_Angeles"

default[:hue][:enabled] = true

default[:hadoop][:proxy_users] = %w(httpfs hue oozie)
default[:httpfs][:proxy_users] = %w(hue)
