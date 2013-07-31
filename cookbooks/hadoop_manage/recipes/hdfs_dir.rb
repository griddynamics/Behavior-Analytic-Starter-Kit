node[:hadoop_manage][:hdfs][:manage][:dirs].delete(" ").split(",").each do |hdfs_dir|
    hdfs_directory "#{hdfs_dir}" do
        action "#{node[:hadoop_manage][:hdfs][:manage][:action]}"
        owner "#{node[:hadoop_manage][:hdfs][:manage][:owner]}"
    end
end