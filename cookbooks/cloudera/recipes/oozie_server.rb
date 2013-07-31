
include_recipe "cloudera"
include_recipe "cloudera::hadoop_client"

Chef::Recipe.send(:include, Chef::Cloudera::SearchNodes)
Chef::Recipe.send(:include, Opscode::OpenSSL::Password)

rdbms_server = find_node_by_role("rdbms_server")

node.set[:oozie][:conf][:oozie_site]["oozie.service.JPAService.jdbc.url"] = "jdbc:#{rdbms_server.db_type}://" +
    "#{rdbms_server.fqdn}:#{rdbms_server.db_port}/#{node[:oozie][:db][:name]}"
node.set[:oozie][:conf][:oozie_site]["oozie.service.JPAService.jdbc.driver"] = rdbms_server.db_driver
node.set[:oozie][:conf][:oozie_site]["oozie.service.JPAService.jdbc.username"] = node[:oozie][:db][:user]
node.set_unless[:oozie][:conf][:oozie_site]["oozie.service.JPAService.jdbc.password"] = secure_password

include_recipe "cloudera::oozie_conf"

package "oozie"

case rdbms_server.db_type
  when 'mysql'
    package "mysql-connector-java"

    directory node[:oozie][:lib_path] do
      owner "oozie"
      group "oozie"
      mode 0755
      recursive true
    end

    execute "create symlink to java mysql connector for oozie server" do
      command "ln -s /usr/share/java/mysql-connector-java.jar #{node[:oozie][:lib_path]}/mysql-connector-java.jar"
      not_if do
        ::File.symlink?("#{node[:oozie][:lib_path]}/mysql-connector-java.jar") &&
            ::File.readlink("#{node[:oozie][:lib_path]}/mysql-connector-java.jar") == "/usr/share/java/mysql-connector-java.jar"
      end
    end

    template "#{node[:oozie][:env_conf_path]}/init_db.sql" do
      source "oozie/init_mysql.sql.erb"
      variables(
          :metastore_host => node.fqdn,
          :db_name => node[:oozie][:db][:name],
          :db_user => node[:oozie][:db][:user],
          :db_password => node[:oozie][:conf][:oozie_site]["oozie.service.JPAService.jdbc.password"]
      )
      mode 0600
      owner "root"
      group "root"
      not_if { ::File.exists?("#{node[:oozie][:env_conf_path]}/init_db.sql") }
      notifies :run, "execute[create oozie db]", :immediately
    end

    execute "create oozie db" do
      command "mysql -u root -p'#{rdbms_server['mysql']['server_root_password']}' < #{node[:oozie][:env_conf_path]}/init_db.sql"
      action :nothing
      notifies :run, "execute[init oozie db]", :immediately
    end

    execute "init oozie db" do
      command "service oozie init"
      action :nothing
    end
  else
    # derby db
end


include_recipe "iptables"

simple_firewall "open oozie ports" do
  ports [ node[:oozie][:server][:port] ]
  action :open
end

service "oozie" do
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end

hdfs_directory "/user/oozie" do
  owner "oozie"
  group "oozie"
  mode  0755
end

bash "Install oozie shared libs" do
  cwd Chef::Config[:file_cache_path]
  not_if "hadoop fs -test -e /user/oozie/share"

  code <<-EOH
  tar xzf #{node[:oozie][:shared_libs]}
  sudo -u oozie hadoop fs -put share /user/oozie/share
  EOH
end

hdfs_directory node[:oozie][:deployment_hdfs_path] do
  owner "oozie"
  group "oozie"
  mode  01777
end
