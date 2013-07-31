node[:hadoop_manage][:service][:services].each do |service|
    service "#{service}" do
        action node[:hadoop_manage][:service][:action]
    end
end
        
