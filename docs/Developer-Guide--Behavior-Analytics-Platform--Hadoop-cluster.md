Hadoop cluster description and management
-----------------------------------------

Cluster description:

- Based on [Cloudera Hadoop Distribution 4](CDH4)
- Monitoring with [Ganglia]
- Default configuration: noHA - 1 namenode, 4 datanode
- Default VM configuration: m1.medium without instance storage
- AWS image: CentOS 6.4(ami-d41689bd)

Cluster topology:

![Cluster topology][cluster_topology]

Cluster management:
------------------

You can manage Hadoop cluster using behavior analytics platform application output panel.
There you can find buttons to manage cluster.

* Hadoop cluster scale up. 
You can scale up you Behavior analytics platform Hadoop cluster by adding data nodes. 
Scale down functionality is not yet implemented.
Click “Scale_up_cluster” button and enter the number of data nodes to be added. 
Data nodes will be added with flavor defined in manifest (default: flavor m1.medium).
Once data nodes are added successfully you will lose information about previously completed jobs in job tracker.


[Cloudera Hadoop Distribution 4]: http://www.cloudera.com/content/cloudera/en/products/cdh.html
[Ganglia]: http://ganglia.sourceforge.net/
[cluster_topology]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Developer%20Guide/Hadoop_cluster.png
