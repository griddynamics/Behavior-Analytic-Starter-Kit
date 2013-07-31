default[:ecask][:db_user] = "ecask"
default[:ecask][:db_password] = "ecask_password"
default[:ecask][:db_root_password] = "PaSsWoRd"
default[:ecask][:tomcat_opts] = "-server -Xms256m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Druntime.environment=development"
default[:ecask][:admin_app_url] = "http://nyurin.s3.amazonaws.com/ecask-admin.war"
default[:ecask][:site_app_url] = "http://nyurin.s3.amazonaws.com/ecask-site.war"

set[:ecask][:tomcat_user] = "tomcat"
set[:ecask][:tomcat_group] = "tomcat"
set[:ecask][:tomcat_dir] = "/usr/share/tomcat"

set[:tomcat7][:java_options] = node[:ecask][:tomcat_opts]

set[:mysql][:remove_anonymous_users] = true
set[:mysql][:remove_test_database] = true
set[:mysql][:allow_remote_root] = false

set[:mysql][:server_root_password] = node[:ecask][:db_root_password]
set[:mysql][:server_debian_password] = node[:ecask][:db_root_password]
set[:mysql][:server_repl_password] = node[:ecask][:db_root_password]

set[:mysql][:bind_address] = "localhost"
