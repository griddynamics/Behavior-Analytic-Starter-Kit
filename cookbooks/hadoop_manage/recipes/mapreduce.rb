directory "#{node[:hadoop_manage][:mapreduce][:job_local_path]}" do
	owner "hdfs"
	group "mapred"
	mode 0755
	action :create
end

jobpath = "#{node[:hadoop_manage][:mapreduce][:job_local_path]}/#{File.basename(node[:hadoop_manage][:mapreduce][:job_url])}"

execute "Downloading job" do
  command "wget -c -P #{node[:hadoop_manage][:mapreduce][:job_local_path]} #{node[:hadoop_manage][:mapreduce][:job_url]}"
  creates "#{jobpath}"
end

execute "Starting job" do
    command "hadoop jar #{jobpath} #{node[:hadoop_manage][:mapreduce][:job_parameters]}"
    only_if { File.exists?("#{jobpath}") }
    user "hdfs"
end
