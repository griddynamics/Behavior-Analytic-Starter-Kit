
include_recipe "cloudera"
include_recipe "cloudera::hadoop_client"
include_recipe "cloudera::sqoop_conf"

package "sqoop"


# Installing database connectors

directory node[:sqoop][:lib_path] do
  owner "root"
  group "root"
  mode 0755
  recursive true
end

package "mysql-connector-java"

execute "create symlink to java mysql connector for sqoop client" do
  command "ln -s /usr/share/java/mysql-connector-java.jar #{node[:sqoop][:lib_path]}/mysql-connector-java.jar"
  not_if do
    ::File.symlink?("#{node[:sqoop][:lib_path]}/mysql-connector-java.jar") &&
        ::File.readlink("#{node[:sqoop][:lib_path]}/mysql-connector-java.jar") == "/usr/share/java/mysql-connector-java.jar"
  end
end
