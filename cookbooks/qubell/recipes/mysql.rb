case node[:platform]
  when "ubuntu"
    # http://ry4an.org/unblog/post/chef_mysql_database_vagrant/
    %w{mysql-client libmysqlclient-dev make}.each do |pack|
      package pack do
        action :nothing
      end.run_action(:install)
    end
    g = chef_gem "mysql" do
      action :nothing
    end
    g.run_action(:install)
  else
    package "mysql"
end

include_recipe "database::mysql"


#create database
mysql_connection_info = {:host => "localhost",
                         :username => 'root',
                         :password => node['mysql']['server_root_password']}

mysql_database node['database']['name'] do
  connection mysql_connection_info
  action :create
end

# save node data after writing the MYSQL root password, so that a failed chef-client run that gets this far doesn't cause an unknown password to get applied to the box without being saved in the node data.
unless Chef::Config[:solo]
  ruby_block "save node data" do
    block do
      node.save
    end
    action :create
  end
end

#create user and grant provileges
mysql_database_user node['database']['user'] do
  connection mysql_connection_info
  password node['database']['password']
  host '%'
  action :create
end

mysql_database_user node['database']['user'] do
  connection mysql_connection_info
  password node['database']['password']
  database_name node['database']['name']
  host '%'
  action :grant
end

mysql_database "flush the privileges" do
  connection mysql_connection_info
  sql "flush privileges"
  action :query
end

#load schema and data
require 'net/http'
require 'net/https'

[node['database']['schema'], node['database']['data']].each do |script|
  mysql_database node['database']['name'] do
    connection mysql_connection_info
    # this part could be just "sql { Net::HTTP.get(script) }" for ruby 2.0
    sql { parsed = URI.parse(script)
    http = Net::HTTP.new(parsed.host, parsed.port)
    if script.include?("https")
      http.use_ssl = true
      http.verify_mode = OpenSSL::SSL::VERIFY_NONE
    end
    resp = http.start { |cx| cx.request(Net::HTTP::Get.new(script)) }
    resp.body
    }
    action :query
    retries 3
  end
end