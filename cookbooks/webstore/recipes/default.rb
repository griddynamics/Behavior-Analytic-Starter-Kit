include_recipe "tomcat7::default"
include_recipe "mysql::server"
include_recipe "webstore::db_setup"

package "mysql-connector-java"

execute "installing mysql-connector-java" do
  command "cp /usr/share/java/mysql-connector-java.jar #{node[:webstore][:tomcat_dir]}/lib/"
  action :run
end

execute "Fixing daemon script" do
  command "sed -i '2iCATALINA_OPTS=\"$CATALINA_OPTS -server -Xms256m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Druntime.environment=development\"' #{node[:webstore][:tomcat_dir]}/bin/catalina.sh"
  user "#{node[:webstore][:tomcat_user]}"
end

template "#{node[:webstore][:tomcat_dir]}/conf/context.xml" do
  source "context.xml.erb"
  variables(:options => node[:hadoop][:conf][:core_site])
  mode 0644
  owner "#{node[:webstore][:tomcat_user]}"
  group "tomcat"
end

remote_file "#{node[:webstore][:tomcat_dir]}/webapps/webstore-admin.war" do
  source "#{node[:webstore][:admin_app_url]}"
  mode 0755
  action :create
end
  
remote_file "#{node[:webstore][:tomcat_dir]}/webapps/webstore.war" do
  source "#{node[:webstore][:site_app_url]}"
  mode 0755
  action :create
end

service "tomcat" do
  action :restart
end

bash 'waiting webstore app deployment' do
  user 'root'
  code <<-EOH
  while [ "`curl -s -w "%{http_code}" "http://localhost:8080/webstore" -o /dev/null`" == "000" ];
  do
          sleep 10
  done
  if [ "`curl -s -w "%{http_code}" "http://localhost:8080/webstore" -o /dev/null`" == "302" ];
  then
          exit 0
  else
          exit 1
  fi
  EOH
end

