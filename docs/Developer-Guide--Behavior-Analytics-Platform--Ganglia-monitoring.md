Ganglia monitoring
------------------

We use [Ganglia](http://ganglia.sourceforge.net) to monitor Behavior analytics platform Hadoop cluster.
Web store isn't covered by Ganglia monitoring because of it is independent application 
that can't be configured with one particular Ganglia server.
Ganglia already has preconfigured templates for Hadoop monitoring and receives metrics directly from Hadoop.
Hadoop has special confguration file 
[/etc/hadoop/conf/hadoop-metrics2.properties](../cookbooks/ganglia/templates/default/hadoop-metrics2.properties.erb) 
that describes settings for Ganglia. Ganglia also collects custom metrics (counters) from MapReduce jobs.
It’s recommended to install rrdcache to improve performance. 
See [Ganglia documentation](http://sourceforge.net/apps/trac/ganglia/wiki/ganglia_documents) to learn more.

Now Ganglia collects following metric groups:
* CPU metrics
* Disk metrics
* Load metrics
* Memory metrics
* Network metrics
* Process metrics
* JVM metrics
* DFS metrics
* RPC metrics
