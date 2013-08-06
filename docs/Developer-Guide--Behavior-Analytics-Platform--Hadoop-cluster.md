Hadoop cluster description
--------------------------

- Based on [Cloudera Hadoop Distribution 4](CDH4)
- Monitoring with [Ganglia]
- Default configuration: noHA - 1 namenode, 8 datanode
- Default VM configuration: m1.medium without instance storage
- AWS image: CentOS 6.3(ami-d41689bd)

Cluster topology:

![Cluster topology][cluster_topology]


[Cloudera Hadoop Distribution 4]: http://www.cloudera.com/content/cloudera/en/products/cdh.html
[Ganglia]: http://ganglia.sourceforge.net/
[cluster_topology]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Developer%20Guide/Hadoop_cluster.png