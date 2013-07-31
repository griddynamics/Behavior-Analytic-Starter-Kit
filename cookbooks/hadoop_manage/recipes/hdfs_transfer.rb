hdfs_transfer node[:hadoop_manage][:hdfs][:transfer][:action] do
    source node[:hadoop_manage][:hdfs][:transfer][:source]
    destination node[:hadoop_manage][:hdfs][:transfer][:destination]
end