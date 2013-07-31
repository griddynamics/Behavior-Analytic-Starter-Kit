include_recipe "tomcat7::default"
include_recipe "mysql::server"
include_recipe "ecask::db_setup"

# package "mysql-connector-java"

bash "installing mysql-connector-java" do
    user "root"
    cwd "/tmp"
    code <<-EOH
wget -q -O - "http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.25.tar.gz/from/http://cdn.mysql.com/" | tar xvfz -
mv /tmp/mysql-connector-java-*/mysql-connector-java*.jar #{node[:ecask][:tomcat_dir]}/lib/    
    EOH
end

execute "Fixing daemon script" do
  command "sed -i '2iCATALINA_OPTS=\"$CATALINA_OPTS -server -Xms256m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Druntime.environment=development\"' #{node[:ecask][:tomcat_dir]}/bin/catalina.sh"
  user "#{node[:ecask][:tomcat_user]}"
end

template "#{node[:ecask][:tomcat_dir]}/conf/context.xml" do
  source "context.xml.erb"
  variables(:options => node[:hadoop][:conf][:core_site])
  mode 0644
  owner "#{node[:ecask][:tomcat_user]}"
  group "tomcat"
end

remote_file "#{node[:ecask][:tomcat_dir]}/webapps/ecask-admin.war" do
  source "#{node[:ecask][:admin_app_url]}"
  mode 0755
  action :create
end
  
remote_file "#{node[:ecask][:tomcat_dir]}/webapps/ecask-site.war" do
  source "#{node[:ecask][:site_app_url]}"
  mode 0755
  action :create
end

service "tomcat" do
  action :restart
end

bash "waiting ecask app deployment" do
    user "root"
    code <<-EOH
	until [ "`curl --silent --show-error --connect-timeout 1 -I http://localhost:8080/ecask-site/ | grep 'Coyote'`" != "" ];
	do
	  sleep 10
	done
    EOH
end

