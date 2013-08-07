Ganglia monitoring
------------------

We use [Ganglia](http://ganglia.sourceforge.net) as metric collector and storage. 
Ganglia already has preconfigured templates for Hadoop monitoring and receives metrics directly from Hadoop.
Hadoop has special [confguration file](../cookbooks/ganglia/templates/default/hadoop-metrics2.properties.erb) /etc/hadoop/conf/hadoop-metrics2.properties that describes Ganglia metrics.
Ganglia also collects custom metrics (counters) from MapReduce jobs.
It’s recommended to install rrdcache to improve performance. 
See [Ganglia documentation](http://sourceforge.net/apps/trac/ganglia/wiki/ganglia_documents) to learn more.
Ganglia collects following metric groups:
* CPU metrics
* Disk metrics
* Load metrics
* Memory metrics
* Network metrics
* Process metrics
* JVM metrics
* DFS metrics
* RPC metrics
