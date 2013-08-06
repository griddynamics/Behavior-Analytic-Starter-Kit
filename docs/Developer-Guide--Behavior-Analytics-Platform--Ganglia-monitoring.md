Ganglia monitoring
------------------

We use [Ganglia](http://ganglia.sourceforge.net) as metric collector and storage. Ganglia receives metrics directly from Hadoop. 
It’s recommended to install rrdcache to improve performance. See [Ganglia documentation](http://sourceforge.net/apps/trac/ganglia/wiki/ganglia_documents) to learn more.
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
