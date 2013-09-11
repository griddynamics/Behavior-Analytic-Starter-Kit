#
# Cookbook Name:: tomcat7
# Recipe:: default
#

package "gcc"
package "make"

#Open ports
simple_firewall "open tomcat ports" do
  ports node[:tomcat][:ports].values
  action :open
end

# Get the tomcat binary 
remote_file "/tmp/#{node[:tomcat7][:tarball]}" do
    source "#{node[:tomcat7][:url]}"
    mode "0644"
	action :create
end

# Create group
group "#{node[:tomcat7][:group]}" do
    action :create
end

# Create user
user "#{node[:tomcat7][:user]}" do
    comment "Tomcat7 user"
    gid "#{node[:tomcat7][:group]}"
    home "#{node[:tomcat7][:target]}"
    shell "/bin/false"
    action :create
end

# Create base folder
directory "#{node[:tomcat7][:target]}/apache-tomcat-#{node[:tomcat7][:version]}" do
    owner "#{node[:tomcat7][:user]}"
    group "#{node[:tomcat7][:group]}"
    mode "0755"
    action :create
end

# Extract
execute "tar" do
    user "#{node[:tomcat7][:user]}"
    group "#{node[:tomcat7][:group]}"
    installation_dir = "#{node[:tomcat7][:target]}"
    cwd installation_dir
    command "tar zxf /tmp/#{node[:tomcat7][:tarball]}"
    action :run
end

# Set the symlink
link "#{node[:tomcat7][:home]}" do
    to "apache-tomcat-#{node[:tomcat7][:version]}"
    link_type :symbolic
end

# Config from template
template "#{node[:tomcat7][:home]}/conf/server.xml" do
    source "server.xml.erb"
    owner "#{node[:tomcat7][:user]}"
    group "#{node[:tomcat7][:group]}"
    mode "0644"
end

# Add the jrebel script
template "#{node[:tomcat7][:home]}/bin/catalina-jrebel.sh" do
    source "catalina-jrebel.sh.erb"
    owner "#{node[:tomcat7][:user]}"
    group "#{node[:tomcat7][:group]}"
    mode "0755"
end

template "/etc/init.d/tomcat" do
    source "init.erb"
    owner "root"
    group "root"
    mode "0755"
end

# Add the init-script
execute "init-rh" do
    user "root"
    group "root"
    command "chkconfig --add tomcat"
    only_if { ::File.exists?("/etc/init.d/tomcat") }
    action :run
end

service "tomcat" do
    action :start
end