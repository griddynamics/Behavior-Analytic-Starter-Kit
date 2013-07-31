
Chef::Recipe.send(:include, Opscode::OpenSSL::Password)
Chef::Recipe.send(:include, Opscode::OpenSSL::SecureTool)

Chef::Recipe.send(:include, Chef::Cloudera::SearchNodes)


##############################
##### Install Hue Server #####
##############################

package "hue"
package "hue-server"


################################
##### Set some hue options #####
################################

node.set_unless[:hue][:conf][:hue_ini][:secret_key] = secret_key


################################
##### Search HttpFS server #####
################################

httpfs_server = find_node_by_role("httpfs_server")

node.set[:hue][:conf][:hue_ini][:hdfs][:webhdfs_url] =
    "http://#{httpfs_server.fqdn}:#{node[:httpfs][:server][:port]}/webhdfs/v1/"


###############################
##### Search Oozie server #####
###############################

oozie_server = find_node_by_role("oozie_server")

node.set[:hue][:conf][:hue_ini][:liboozie][:oozie_url] =
    "http://#{oozie_server.fqdn}:#{node[:oozie][:server][:port]}/oozie"


###############################
##### Search rdbms server #####    Note: Now supported sqlite3 only
###############################          because db migration of Hue (from cdh 4.2.1) not work

#rdbms_server = find_node_by_role("rdbms_server")

#node.set[:hue][:conf][:hue_ini][:db][:engine] = rdbms_server.db_type
#node.set[:hue][:conf][:hue_ini][:db][:host] = rdbms_server.fqdn
#node.set[:hue][:conf][:hue_ini][:db][:port] = rdbms_server.db_port

#node.set_unless[:hue][:conf][:hue_ini][:db][:password] = secure_password


#########################
##### Init database #####
#########################

#case rdbms_server.db_type
#  when 'mysql'
#    include_recipe "mysql::client"

#    package "mysql-connector-java"

#    template "#{node[:hue][:conf_path]}/init_db.sql" do
#      source "hue/init_mysql.sql.erb"
#      variables(
#          :db_name => node[:hue][:conf][:hue_ini][:db][:name],
#          :db_user => node[:hue][:conf][:hue_ini][:db][:user],
#          :db_password => node[:hue][:conf][:hue_ini][:db][:password]
#      )
#      mode 0600
#      owner "root"
#      group "root"
#      not_if { ::File.exists?("#{node[:hue][:conf_path]}/init_db.sql") }
#      notifies :run, "execute[create hue db]", :immediately
#    end

#    execute "create hue db" do
#      command "mysql -u root -p'#{rdbms_server['mysql']['server_root_password']}' < #{node[:hue][:conf_path]}/init_db.sql"
#      action :nothing
#      notifies :run, "execute[init hue db]", :immediately
#    end

#    execute "init hue db" do
#      command "sudo -u hue DESKTOP_LOG_DIR=#{node[:hue][:logs_path]} /usr/share/hue/build/env/bin/hue syncdb --noinput"
#      action :nothing
#    end
#  else
    # sqlite3
#end


###############################################
##### Configuring and starting Hue Server #####
###############################################

template "#{node[:hue][:conf_path]}/hue.ini" do
  source "hue/hue.ini.erb"
  variables( :options => node[:hue][:conf][:hue_ini] )
  mode 0755
  owner "root"
  group "root"
end

simple_firewall "open hue server port" do
  ports [ node[:hue][:server][:port] ]
  action :open
end

service "hue" do
  action [ :start, :enable ]
end
