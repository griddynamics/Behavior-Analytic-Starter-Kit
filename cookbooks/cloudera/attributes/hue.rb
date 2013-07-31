
include_attribute "cloudera"
include_attribute "cloudera::hadoop"
include_attribute "cloudera::httpfs"
include_attribute "cloudera::oozie"


default[:hue][:conf_path] = "/etc/hue"
default[:hue][:logs_path] = "/var/log/hue"

default[:hue][:server][:port] = 8888


default[:hue][:conf][:hue_ini] = { # TODO: add ssl configuration

    :secret_key => "", # generated in recipe

    :http_host => "0.0.0.0",
    :http_port => node[:hue][:server][:port],

    :time_zone => node[:system][:time_zone_name],

    :db => {  # Now supported sqlite3 only!
        :engine => "sqlite3",
        :host => "",
        :port => "",
        :name => "hue",
        :user => "hue",
        #:password => "",
    },

    :hdfs => {
        :fs_defaultfs => node[:hadoop][:conf][:core_site]["fs.defaultFS"],
        :webhdfs_url => "unknown"  # search HttpFS server in recipe
    },

    :mapred => {
        :jobtracker_host => node[:hadoop][:jt][:hosts][:jt1], # this configuration is bad (jt1 or jt2 !?)
        :jobtracker_port => node[:hadoop][:jt][:ports][:rpc],
        :thrift_port => node[:hadoop][:jt][:ports][:thrift],
        :submit_to => true,
    },

    :liboozie => {
        :oozie_url => "unknown",  # search Oozie server in recipe
        :remote_deployement_dir => node[:oozie][:deployment_hdfs_path],
    },

    :oozie => {
        :share_jobs => true,
    },

    :beeswax => {
        :share_saved_queries => true,
    },

    :jobbrowser => {
        :share_jobs => true,
    },
}
