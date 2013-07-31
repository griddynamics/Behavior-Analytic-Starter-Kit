ganglia_server = node[:hadoop][:nn][:host]

if ganglia_server.empty?
    collector_gmond_host = "127.0.0.1"
else
    collector_gmond_host = ganglia_server
end

template "/etc/hadoop/conf/hadoop-metrics2.properties" do
    source "hadoop-metrics2.properties.erb"
    variables(:collector_gmond_host => collector_gmond_host)
    mode 00644
    owner "root"
    group "root"
    notifies :restart, "service[gmond]"
end
