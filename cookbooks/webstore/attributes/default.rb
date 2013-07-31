default[:webstore][:db_user] = "ecask"
default[:webstore][:db_password] = "ecask_password"
default[:webstore][:db_root_password] = "PaSsWoRd"
default[:webstore][:tomcat_opts] = "-server -Xms256m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Druntime.environment=development"
default[:webstore][:admin_app_url] = "http://nyurin.s3.amazonaws.com/webstore-admin.war"
default[:webstore][:site_app_url] = "http://nyurin.s3.amazonaws.com/ecask-site.war"

set[:webstore][:tomcat_user] = "tomcat"
set[:webstore][:tomcat_group] = "tomcat"
set[:webstore][:tomcat_dir] = "/usr/share/tomcat"

set[:tomcat7][:java_options] = node[:webstore][:tomcat_opts]

set[:mysql][:remove_anonymous_users] = true
set[:mysql][:remove_test_database] = true
set[:mysql][:allow_remote_root] = false

set[:mysql][:server_root_password] = node[:webstore][:db_root_password]
set[:mysql][:server_debian_password] = node[:webstore][:db_root_password]
set[:mysql][:server_repl_password] = node[:webstore][:db_root_password]

set[:mysql][:bind_address] = "localhost"
