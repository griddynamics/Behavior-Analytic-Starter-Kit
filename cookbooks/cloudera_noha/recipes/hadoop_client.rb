
include_recipe "cloudera_noha"
include_recipe "cloudera_noha::hadoop_conf"

package "hadoop-client"
