include_attribute "cloudera_noha"

default[:hadoop][:bin_path] = "/usr/bin/hadoop"
default[:hadoop][:conf_path] = "/etc/hadoop/conf"
default[:hadoop][:env_conf_path] = "#{node[:hadoop][:conf_path]}.#{node.chef_environment}"
default[:hadoop][:slave_disks] = 1


##########################
# Namenode configuration #
##########################

default[:hadoop][:nn][:format] = false
default[:hadoop][:nn][:host] = ""

default[:hadoop][:nn][:ports][:rpc] = 8020
default[:hadoop][:nn][:ports][:http] = 50070
default[:hadoop][:nn][:ports][:zkfc] = 8019


############################
# Jobtracker configuration #
############################
default[:hadoop][:jt][:host] = node[:hadoop][:nn][:host]
default[:hadoop][:jt][:ports][:rpc] = 8021
default[:hadoop][:jt][:ports][:http] = 50030
default[:hadoop][:jt][:ports][:mrzkfc] = 8018

##############################
# Hadoop slave configuration #
##############################
default[:hadoop][:dn][:hosts] = []
default[:hadoop][:dn][:ports][:rpc] = 50010
default[:hadoop][:dn][:ports][:ipc] = 50020
default[:hadoop][:dn][:ports][:http] = 50075

default[:hadoop][:tt][:ports][:http] = 50060


######################
# Core configuration #
######################

default[:hadoop][:conf][:core_site]["fs.defaultFS"] = "hdfs://#{node[:hadoop][:nn][:host]}:#{node[:hadoop][:nn][:ports][:rpc]}"
default[:hadoop][:conf][:core_site]["fs.default.name"] = node[:hadoop][:conf][:core_site]["fs.defaultFS"]
default[:hadoop][:conf][:core_site]["fs.trash.interval"] = 60
default[:hadoop][:conf][:core_site]["fs.trash.checkpoint.interval"] = 60
default[:hadoop][:conf][:core_site]["io.file.buffer.size"] = 65536
default[:hadoop][:conf][:core_site]["io.compression.codecs"] = %w(
org.apache.hadoop.io.compress.DefaultCodec
org.apache.hadoop.io.compress.GzipCodec
org.apache.hadoop.io.compress.BZip2Codec
org.apache.hadoop.io.compress.DeflateCodec
org.apache.hadoop.io.compress.SnappyCodec
org.apache.hadoop.io.compress.Lz4Codec
).join(",")
default[:hadoop][:conf][:core_site]["hadoop.security.authentication"] = "simple"
default[:hadoop][:conf][:core_site]["hadoop.rpc.protection"] = "authentication"
default[:hadoop][:conf][:core_site]["hadoop.security.auth_to_local"] = "DEFAULT"

######################
# Hdfs configuration #
######################

default[:hadoop][:conf][:hdfs_site]["dfs.replication"] = 3
default[:hadoop][:conf][:hdfs_site]["dfs.blocksize"] = 134217728
default[:hadoop][:conf][:hdfs_site]["dfs.permissions"] = true
default[:hadoop][:conf][:hdfs_site]["dfs.permissions.superusergroup"] = "hadoop"
default[:hadoop][:conf][:hdfs_site]["dfs.namenode.name.dir"] = "/data/1/dfs/nn"
default[:hadoop][:conf][:hdfs_site]["dfs.datanode.data.dir"] = (1..node[:hadoop][:slave_disks]).map{ |num| "/data/#{num}/dfs/dn" }.join(",")
default[:hadoop][:conf][:hdfs_site]["dfs.webhdfs.enabled"] = true
default[:hadoop][:conf][:hdfs_site]["dfs.namenode.http-address"] =  "#{node[:hadoop][:nn][:host]}:#{node[:hadoop][:nn][:ports][:http]}"


########################
# Mapred configuration #
########################

default[:hadoop][:conf][:mapred_site]["mapred.reduce.tasks"] = "#{node[:hadoop][:dn][:hosts].size}"
default[:hadoop][:conf][:mapred_site]["mapred.job.tracker"] = "#{node[:hadoop][:jt][:host]}:#{node[:hadoop][:jt][:ports][:rpc]}"
default[:hadoop][:conf][:mapred_site]["mapred.local.dir"] =
    (1..node[:hadoop][:slave_disks]).map{ |num| "/data/#{num}/mapred/local" }.join(",")
default[:hadoop][:conf][:mapred_site]["mapred.system.dir"] = "/var/mapred/staging"
default[:hadoop][:conf][:mapred_site]["mapreduce.jobtracker.staging.root.dir"] = "/var/mapred/staging"
default[:hadoop][:conf][:mapred_site]["mapred.output.compress"] = false
default[:hadoop][:conf][:mapred_site]["mapred.output.compression.type"] = "BLOCK"
default[:hadoop][:conf][:mapred_site]["mapred.output.compression.codec"] = "org.apache.hadoop.io.compress.DefaultCodec"
default[:hadoop][:conf][:mapred_site]["mapred.compress.map.output"] = true
default[:hadoop][:conf][:mapred_site]["mapred.map.output.compression.codec"] = "org.apache.hadoop.io.compress.SnappyCodec"
default[:hadoop][:conf][:mapred_site]["mapred.tasktracker.map.tasks.maximum"] = 1
default[:hadoop][:conf][:mapred_site]["mapred.tasktracker.reduce.tasks.maximum"] = 1
default[:hadoop][:conf][:mapred_site]["mapred.child.java.opts"] = "-Xmx512m -server"
default[:hadoop][:conf][:mapred_site]["mapred.job.reuse.jvm.num.tasks"] = -1
default[:hadoop][:conf][:mapred_site]["mapred.map.tasks.speculative.execution"] = false
default[:hadoop][:conf][:mapred_site]["mapred.reduce.tasks.speculative.execution"] = false
default[:hadoop][:conf][:mapred_site]["mapred.reduce.slowstart.completed.maps"] = 0.8
default[:hadoop][:conf][:mapred_site]["mapred.jobtracker.restart.recover"] = true

# configuring capacity scheduler
default[:hadoop][:conf][:mapred_site]["mapred.jobtracker.taskScheduler"] = "org.apache.hadoop.mapred.CapacityTaskScheduler"
default[:hadoop][:conf][:mapred_site]["mapred.queue.names"] = "default"
default[:hadoop][:conf][:mapred_site]["mapred.cluster.map.memory.mb"] = 1024        # memory size of single map slot
default[:hadoop][:conf][:mapred_site]["mapred.cluster.reduce.memory.mb"] = 1024     # memory size of single reduce slot
default[:hadoop][:conf][:mapred_site]["mapred.cluster.max.map.memory.mb"] = 4096    # max memory size of single map task
default[:hadoop][:conf][:mapred_site]["mapred.cluster.max.reduce.memory.mb"] = 5096 # max memory size of single reduce task
default[:hadoop][:conf][:mapred_site]["mapred.job.map.memory.mb"] = 1024            # memory size of single map task for particular job
default[:hadoop][:conf][:mapred_site]["mapred.job.reduce.memory.mb"] = 1024         # memory size of single reduce task for particular job


#######################################################################################
# Capacity Scheduler configuration                                                    #
# For more details, see: http://hadoop.apache.org/docs/stable/capacity_scheduler.html #
#######################################################################################

default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.maximum-system-jobs"] = 3000

# The default configuration settings for the capacity task scheduler
# The default values would be applied to all the queues which don't have
# the appropriate property for the particular queue
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-supports-priority"] = false
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-minimum-user-limit-percent"] = 100
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-maximum-initialized-jobs-per-user"] = 2
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-user-limit-factor"] = 1
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-maximum-active-tasks-per-queue"] = 200000
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-maximum-active-tasks-per-user"] = 100000
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.default-init-accept-jobs-factor"] = 10

default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.capacity"] = 100
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.maximum-capacity"] = -1
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.supports-priority"] = false
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.minimum-user-limit-percent"] = 100
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.user-limit-factor"] = 1
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.maximum-initialized-active-tasks"] = 200000
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.maximum-initialized-active-tasks-per-user"] = 100000
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.queue.default.init-accept-jobs-factor"] = 10

# Capacity scheduler Job Initialization configuration parameters
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.init-poll-interval"] = 5000
default[:hadoop][:conf][:capacity_scheduler]["mapred.capacity-scheduler.init-worker-threads"] = 5

###################################
# Mapred queue acls configuration #
###################################

default[:hadoop][:conf][:mapred_queue_acls]["mapred.queue.default.acl-submit-job"] = " "
default[:hadoop][:conf][:mapred_queue_acls]["mapred.queue.default.acl-administer-jobs"] = " "
