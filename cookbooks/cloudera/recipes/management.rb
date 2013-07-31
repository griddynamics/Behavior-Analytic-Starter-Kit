
package "rubygems"

gem_package "net-ssh"
gem_package "colored"

directory node[:hadoop][:management][:path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

name_nodes = search(:node, "roles:namenode AND chef_environment:#{node.chef_environment}")
journal_nodes = search(:node, "roles:journalnode AND chef_environment:#{node.chef_environment}")
hadoop_slaves = search(:node, "roles:hslave AND chef_environment:#{node.chef_environment}")
job_trackers = search(:node, "roles:jobtracker AND chef_environment:#{node.chef_environment}")
zookeeper_servers = search(:node, "roles:zookeeper AND chef_environment:#{node.chef_environment}")

template "#{node[:hadoop][:management][:path]}/cluster_infrastructure.rb" do
  source "management/cluster_infrastructure.rb.erb"
  variables(
      :name_nodes => name_nodes,
      :journal_nodes => journal_nodes,
      :data_nodes => hadoop_slaves,
      :job_trackers => job_trackers,
      :task_trackers => hadoop_slaves,
      :zookeepers => zookeeper_servers
  )
  mode 0644
  owner "root"
  group "root"
end

management_script "base_manager.rb"
management_script "hadoop_manager.rb"
management_script "hdfs.rb"
management_script "journals.rb"
management_script "mapred.rb"
management_script "zookeeper.rb"
management_script "zkfc.rb"
management_script "mrzkfc.rb"
