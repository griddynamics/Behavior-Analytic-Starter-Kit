
directory node[:hadoop][:env_conf_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

if node[:cloud][:provider] == "rackspace"
  template "/etc/hosts" do
    source "hosts.erb"
    owner "root"
    group "root"
    variables(:options => node[:hadoop][:dn][:hosts])
    mode 00644
  end
  template "/etc/host.conf" do
    source "host.conf.erb"
    owner "root"
    group "root"
    mode 00644
  end
  execute "changing hostname" do
    command "hostname default#{node[:cloud][:local_ipv4].delete(".")}"
    action :run
  end  
end

template "#{node[:hadoop][:env_conf_path]}/core-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:hadoop][:conf][:core_site])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:hadoop][:env_conf_path]}/hdfs-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:hadoop][:conf][:hdfs_site])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:hadoop][:env_conf_path]}/mapred-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:hadoop][:conf][:mapred_site])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:hadoop][:env_conf_path]}/capacity-scheduler.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:hadoop][:conf][:capacity_scheduler])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:hadoop][:env_conf_path]}/mapred-queue-acls.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:hadoop][:conf][:mapred_queue_acls])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:hadoop][:env_conf_path]}/hadoop-env.sh" do
   source "hadoop/hadoop-env.sh.erb"
   mode 0755
   owner "root"
   group "root"
end

cookbook_file "#{node[:hadoop][:env_conf_path]}/log4j.properties" do
  source "hadoop/log4j.properties"
  mode 0644
  owner "root"
  group "root"
end

bash "Reset alternatives for hadoop conf" do
  not_if do
    ::File.symlink?("/etc/alternatives/hadoop-conf") &&
        ::File.readlink("/etc/alternatives/hadoop-conf") == node[:hadoop][:env_conf_path]
  end
  code <<-EOH
  alternatives --install #{node[:hadoop][:conf_path]} hadoop-conf #{node[:hadoop][:env_conf_path]} 1000000
  alternatives --set hadoop-conf #{node[:hadoop][:env_conf_path]}
  EOH
end
