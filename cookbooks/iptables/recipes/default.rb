
execute "iptables save" do
  command "service #{node[:iptables][:service_name]} save"
  action :nothing
end
