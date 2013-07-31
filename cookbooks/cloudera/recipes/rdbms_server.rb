
node.override['mysql']['allow_remote_root']      = true
node.override['mysql']['remove_anonymous_users'] = true
node.override['mysql']['remove_test_database']   = true

include_recipe "mysql::server"

simple_firewall "open mysql port" do
  ports [ node['mysql']['port'] ]
  action :open
end

node.set_unless[:db_type] = "mysql"
node.set_unless[:db_port] = node['mysql']['port']
node.set_unless[:db_driver] = "com.mysql.jdbc.Driver"
