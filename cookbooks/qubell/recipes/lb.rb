
service "haproxy" do
  supports :restart => true, :status => true, :reload => true
  action [:enable]
end

template "/etc/haproxy/haproxy.cfg" do
  source "haproxy.cfg.erb"
  owner "root"
  group "root"
  mode 0644
  variables(
    :nodes => node['haproxy.rebalance']['nodes']
  )
  notifies :restart, "service[haproxy]"
end