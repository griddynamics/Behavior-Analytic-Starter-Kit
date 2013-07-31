
include_attribute "cloudera"

default[:oozie][:conf_path] = "/etc/oozie/conf"
default[:oozie][:env_conf_path] = "#{node[:oozie][:conf_path]}.#{node.chef_environment}"
default[:oozie][:home_path] = "/usr/lib/oozie"
default[:oozie][:data_path] = "/var/lib/oozie"
default[:oozie][:lib_path]  = "/var/lib/oozie"

default[:oozie][:deployment_hdfs_path]  = "/user/oozie/workflows"

default[:oozie][:shared_libs]  = "/usr/lib/oozie/oozie-sharelib.tar.gz"

default[:oozie][:server][:port] = 11000

default[:oozie][:db][:name] = "oozie"
default[:oozie][:db][:user] = "oozie"


#######################
# Oozie configuration #
#######################

default[:oozie][:conf][:oozie_site] = {

    # Creates Oozie DB.
    # If set to true, it creates the DB schema if it does not exist. If the DB schema exists is a NOP.
    # If set to false, it does not create the DB schema. If the DB schema does not exist it fails start up.
    # Note: false is recommended for production. In production need to use ooziedb.sh script.
    #       /usr/lib/oozie/bin/ooziedb.sh create -sqlfile oozie.sql -runValidate DB Connection.
    # Default value: true
    "oozie.service.JPAService.create.db.schema" => "false",

    "oozie.service.ActionService.executor.ext.classes" => %w(
        org.apache.oozie.action.email.EmailActionExecutor
        org.apache.oozie.action.hadoop.HiveActionExecutor
        org.apache.oozie.action.hadoop.ShellActionExecutor
        org.apache.oozie.action.hadoop.SqoopActionExecutor
        org.apache.oozie.action.hadoop.DistcpActionExecutor
    ).join(","),

    "oozie.service.SchemaService.wf.ext.schemas" => %w(
        shell-action-0.1.xsd
        shell-action-0.2.xsd
        email-action-0.1.xsd
        hive-action-0.2.xsd
        hive-action-0.3.xsd
        sqoop-action-0.2.xsd
        sqoop-action-0.3.xsd
        ssh-action-0.1.xsd
        distcp-action-0.1.xsd
    ).join(","),

    # Specifies whether security (user name/admin role) is enabled or not.
    # If disabled any user can manage Oozie system and manage any job.
    # Default value: !!! not found in oozie-default.xml !!!
    "oozie.service.AuthorizationService.security.enabled" => "false",

    "oozie.service.HadoopAccessorService.hadoop.configurations" => "*=#{node[:hadoop][:conf_path]}",

    # Default timeout for a coordinator action input check (in minutes) for normal job.
    # -1 means infinite timeout
    # Default value: 10080
    "oozie.service.coord.normal.default.timeout" => "120",

    # If true, enables the oozie.mapreduce.uber.jar mapreduce workflow configuration property, which is used to specify an
    # uber jar in HDFS.  Submitting a workflow with an uber jar requires at least Hadoop 2.2.0 or 1.2.0.  If false, workflows
    # which specify the oozie.mapreduce.uber.jar configuration property will fail.
    # Default value: false
    #"oozie.action.mapreduce.uber.jar.enable" => "true",

    #Default proxyuser configuration for Hue
    "oozie.service.ProxyUserService.proxyuser.hue.hosts" => "*",
    "oozie.service.ProxyUserService.proxyuser.hue.groups" => "*",
}

default[:oozie][:conf][:hadoop_config] = {
    "mapreduce.jobtracker.kerberos.principal" => "mapred/_HOST@LOCALREALM",
    "yarn.resourcemanager.principal" => "yarn/_HOST@LOCALREALM",
    "dfs.namenode.kerberos.principal" => "hdfs/_HOST@LOCALREALM",
    "mapreduce.framework.name" => "yarn",
}

default[:oozie][:conf][:hive] = {
    "hadoop.bin.path" => node[:hadoop][:bin_path],
    "hadoop.config.dir" => node[:hadoop][:conf_path],
}
