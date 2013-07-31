
include_recipe 'nagios'
include_recipe 'nagios::httpd'

include_recipe 'cloudera::hadoop_client'

web_user = node['apache']['user']
web_group = node['apache']['group'] || web_user

##############################################################################
Chef::Log.info "Setting up Nagios server from package"
##############################################################################

package "nagios"
package "nagios-plugins"
package "nagios-plugins-all"

##############################################################################
Chef::Log.info "Creating Nagios directories"
##############################################################################

directory node['nagios']['conf_dir']

directory "#{node['nagios']['conf_dir']}/dist" do
  owner node['nagios']['user']
  group node['nagios']['group']
  mode 00755
end

execute "archive-default-nagios-object-definitions" do
  command "mv #{node['nagios']['config_dir']}/*.cfg #{node['nagios']['conf_dir']}/dist"
  not_if { Dir.glob("#{node['nagios']['config_dir']}/*.cfg").empty? }
end

directory node['nagios']['state_dir'] do
  owner node['nagios']['user']
  group node['nagios']['group']
  mode 00751
end

directory "#{node['nagios']['state_dir']}/cmd" do
  owner node['nagios']['user']
  group web_group
  mode 02710
end

directory "#{node['nagios']['log_dir']}/spool" do
  action :delete
  only_if { Dir.exists?("#{node['nagios']['log_dir']}/spool") }
  recursive true
end

directory "#{node['nagios']['state_dir']}/spool/checkresults" do
  owner node['nagios']['user']
  group node['nagios']['group']
  mode 00750
  recursive true
end

directory "#{node['nagios']['cache_dir']}" do
  owner node['nagios']['user']
  group node['nagios']['group']
  mode 00755
end

directory "#{node['nagios']['run_dir']}" do
  owner node['nagios']['user']
  group node['nagios']['group']
  mode 00755
end

##############################################################################
Chef::Log.info("Search nagios web interface users from the users data bag")
##############################################################################

begin
  sysadmins = search("#{node.chef_environment}_users", "groups:#{node['nagios']['users_data_bag_group']}")
rescue Net::HTTPServerException
  message = "Could not find appropriate items in the \"users\" databag. " +
      "Check to make sure there is a users databag " +
      "and if you have set the \"users_data_bag_group\" that users in that group exist"
  Chef::Log.fatal(message)
  raise message
end

template "#{node['nagios']['conf_dir']}/htpasswd.users" do
  source "htpasswd.users.erb"
  owner node['nagios']['user']
  group web_group
  mode 00640
  variables(:sysadmins => sysadmins)
end

##############################################################################
Chef::Log.info "Setting up Nagios plugins"
##############################################################################

nagios_plugin "check_hadoop.sh"
nagios_plugin "check_hbase.sh"
nagios_plugin "check_hdfs.sh"

nagios_plugin "check_name_dir_status.py"
nagios_plugin "check_hdfs_blocks.py"
nagios_plugin "check_hdfs_capacity.py"
nagios_plugin "check_datanode_storage.php"

nagios_plugin "check_hive_metastore_status.sh"
nagios_plugin "check_oozie_status.sh"

nagios_plugin "check_webui.py"
nagios_plugin "check_aggregate.php"

nagios_plugin "check_cpu.pl"
nagios_plugin "check_ifoperstatus"
nagios_plugin "check_ifstatus"
nagios_plugin "check_rpcq_latency.php"
nagios_plugin "check_templeton_status.sh"
nagios_plugin "sys_logger.py"

##############################################################################
Chef::Log.info("Generate SSL Certificates")
##############################################################################

directory "#{node['nagios']['conf_dir']}/certificates" do
  owner web_user
  group web_group
  mode 00700
end

bash "Create SSL Certificates" do
  cwd "#{node['nagios']['conf_dir']}/certificates"
  code <<-EOH
  umask 077
  openssl genrsa 2048 > nagios-server.key
  openssl req -subj "#{node['nagios']['ssl_req']}" -new -x509 -nodes -sha1 -days 3650 -key nagios-server.key > nagios-server.crt
  cat nagios-server.key nagios-server.crt > nagios-server.pem
  chown #{web_user}.#{web_group} nagios-server.key nagios-server.crt nagios-server.pem
  EOH
  not_if { ::File.exists?("#{node['nagios']['conf_dir']}/certificates/nagios-server.pem") }
end

##############################################################################
Chef::Log.info("Beginning search nodes to monitor and data bags. " +
               "This may take some time depending on your node count")
##############################################################################

nodes = search(:node, "hostname:[* TO *] AND chef_environment:#{node.chef_environment}")

if nodes.empty?
  Chef::Log.info("No nodes returned from search, using this node so hosts.cfg has data")
  nodes << node
end

# Sort by name to provide stable ordering
nodes.sort! {|a,b| a.name <=> b.name }

# maps nodes into nagios hostgroups
hostgroups = search(:role, "*:*").map { |role| role.name }

# Add all unique platforms to the array of hostgroups
nodes.each do |n|
  unless hostgroups.include?(n['os'])
    hostgroups << n['os']
  end
end

# pick up base contacts
members = Array.new
sysadmins.each do |sysadmin|
  members << sysadmin['id']
end

##############################################################################
Chef::Log.info("Generate nagios configs")
##############################################################################

%w{ nagios cgi }.each do |conf_name|
  nagios_conf conf_name do
    config_subdir false
  end
end

nagios_conf "commands" do
  conf_type :common
end


nagios_conf "contacts" do
  conf_type :common
  variables(:admins => sysadmins,
            :members => members)
end

nagios_conf "hostgroups" do
  conf_type :common
  variables(:hostgroups => hostgroups)
end

nagios_conf "localhost" do
  conf_type :common
end

nagios_conf "hosts" do
  conf_type :common
  variables(:nodes => nodes)
end

nagios_conf "services" do
  conf_type :common
end


nagios_conf "hadoop-commands" do
  conf_type :hadoop
end

nagios_conf "hadoop-servicegroups" do
  conf_type :hadoop
end

nagios_conf "hadoop-services" do
  conf_type :hadoop
end

nagios_conf "hadoop-services-hdfs" do
  conf_type :hadoop
end

nagios_conf "hadoop-services-mapreduce" do
  conf_type :hadoop
end

nagios_conf "hadoop-services-zookeeper" do
  conf_type :hadoop
end

##############################################################################
Chef::Log.info("Start nagios server")
##############################################################################

service "nagios" do
  service_name node['nagios']['server']['service_name']
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end
