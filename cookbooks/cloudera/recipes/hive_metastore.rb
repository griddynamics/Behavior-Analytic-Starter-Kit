
Chef::Recipe.send(:include, Chef::Cloudera::SearchNodes)
Chef::Recipe.send(:include, Opscode::OpenSSL::Password)

rdbms_server = find_node_by_role("rdbms_server")

node.set[:hive][:conf][:hive_site]["javax.jdo.option.ConnectionURL"] = "jdbc:#{rdbms_server.db_type}://" +
    "#{rdbms_server.fqdn}:#{rdbms_server.db_port}/#{node[:hive][:metastore][:db][:name]}"
node.set[:hive][:conf][:hive_site]["javax.jdo.option.ConnectionDriverName"] = rdbms_server.db_driver
node.set[:hive][:conf][:hive_site]["javax.jdo.option.ConnectionUserName"] = node[:hive][:metastore][:db][:user]
node.set_unless[:hive][:conf][:hive_site]["javax.jdo.option.ConnectionPassword"] = secure_password

include_recipe "cloudera"
include_recipe "cloudera::hive_conf"

package "hive-metastore"

case rdbms_server.db_type
  when 'mysql'
    package "mysql-connector-java"

    directory node[:hive][:lib_path] do
      owner "root"
      group "root"
      mode 0755
      recursive true
    end

    execute "create symlink to java mysql connector for hive metastore" do
      command "ln -s /usr/share/java/mysql-connector-java.jar #{node[:hive][:lib_path]}/mysql-connector-java.jar"
      not_if do
        ::File.symlink?("#{node[:hive][:lib_path]}/mysql-connector-java.jar") &&
            ::File.readlink("#{node[:hive][:lib_path]}/mysql-connector-java.jar") == "/usr/share/java/mysql-connector-java.jar"
      end
    end

    template "#{node[:hive][:env_conf_path]}/init_db.sql" do
      source "hive/init_mysql.sql.erb"
      variables(
          :metastore_host => node.fqdn,
          :hive_version => node[:hive][:version],
          :db_name => node[:hive][:metastore][:db][:name],
          :db_user => node[:hive][:conf][:hive_site]["javax.jdo.option.ConnectionUserName"],
          :db_password => node[:hive][:conf][:hive_site]["javax.jdo.option.ConnectionPassword"]
      )
      mode 0600
      owner "root"
      group "root"
      not_if { ::File.exists?("#{node[:hive][:env_conf_path]}/init_db.sql") }
      notifies :run, "execute[init hive db]", :immediately
    end

    execute "init hive db" do
      command "mysql -u root -p'#{rdbms_server['mysql']['server_root_password']}' < #{node[:hive][:env_conf_path]}/init_db.sql"
      action :nothing
    end
  else
    # derby db
end

simple_firewall "open hive_metastore port" do
  ports [ node[:hive][:metastore][:port] ]
  action :open
end

service "hive-metastore" do
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end
