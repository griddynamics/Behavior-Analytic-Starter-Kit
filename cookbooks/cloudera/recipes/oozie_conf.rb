
directory node[:oozie][:env_conf_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

directory "#{node[:oozie][:env_conf_path]}/action-conf" do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

cookbook_file "#{node[:oozie][:env_conf_path]}/oozie-log4j.properties" do
  source "oozie/oozie-log4j.properties"
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:oozie][:env_conf_path]}/action-conf/hive.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:oozie][:conf][:hive])
  mode 0644
  owner "root"
  group "root"
end


template "#{node[:oozie][:env_conf_path]}/oozie-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:oozie][:conf][:oozie_site])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:oozie][:env_conf_path]}/hadoop-config.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:oozie][:conf][:hadoop_config])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:oozie][:env_conf_path]}/adminusers.txt" do
  source "oozie/adminusers.txt.erb"
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:oozie][:env_conf_path]}/oozie-env.sh" do
  source "oozie/oozie-env.sh.erb"
  mode 0644
  owner "root"
  group "root"
end

bash "Reset alternatives for oozie conf" do
  not_if do
    ::File.symlink?(node[:oozie][:conf_path]) &&
        ::File.readlink(node[:oozie][:conf_path]) == "/etc/alternatives/oozie-conf" &&
        ::File.symlink?("/etc/alternatives/oozie-conf") &&
        ::File.readlink("/etc/alternatives/oozie-conf") == node[:oozie][:env_conf_path]
  end
  code <<-EOH
  alternatives --install #{node[:oozie][:conf_path]} oozie-conf #{node[:oozie][:env_conf_path]} 1000000
  alternatives --set oozie-conf #{node[:oozie][:env_conf_path]}
  EOH
end
