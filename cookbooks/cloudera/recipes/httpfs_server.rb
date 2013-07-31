
include_recipe "cloudera"
include_recipe "cloudera::hadoop_conf"

package "hadoop-httpfs"

directory node[:httpfs][:env_conf_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

template "#{node[:httpfs][:env_conf_path]}/httpfs-site.xml" do
  source "generic-site.xml.erb"
  variables(:options => node[:httpfs][:conf][:httpfs_site])
  mode 0644
  owner "root"
  group "root"
end

template "#{node[:httpfs][:env_conf_path]}/httpfs-env.sh" do
  source "httpfs/httpfs-env.sh.erb"
  mode 0755
  owner "root"
  group "root"
end

template "#{node[:httpfs][:env_conf_path]}/httpfs-signature.secret" do
  source "httpfs/httpfs-signature.secret.erb"
  mode 0755
  owner "root"
  group "root"
end

cookbook_file "#{node[:httpfs][:env_conf_path]}/httpfs-log4j.properties" do
  source "httpfs/httpfs-log4j.properties"
  mode 0644
  owner "root"
  group "root"
end

bash "Reset alternatives for httpfs conf" do
  not_if do
    ::File.symlink?("/etc/alternatives/httpfs-conf") &&
        ::File.readlink("/etc/alternatives/httpfs-conf") == node[:httpfs][:env_conf_path]
  end
  code <<-EOH
  alternatives --install #{node[:httpfs][:conf_path]} httpfs-conf #{node[:httpfs][:env_conf_path]} 1000000
  alternatives --set httpfs-conf #{node[:httpfs][:env_conf_path]}
  EOH
end

execute "Fix httpfs log dir in logging.properties" do
  command "sed -i 's%{catalina.base}/logs%{httpfs.log.dir}%g' /usr/lib/hadoop-httpfs/conf/logging.properties"
end

simple_firewall "open hadoop-httpfs port" do
  ports [ node[:httpfs][:server][:port] ]
  action :open
end

service "hadoop-httpfs" do
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end
