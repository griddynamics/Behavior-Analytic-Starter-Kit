
define :nagios_conf, :conf_type => "", :variables => {}, :config_subdir => true do

  conf_dir = params[:config_subdir] ? node['nagios']['config_dir'] : node['nagios']['conf_dir']

  template "#{conf_dir}/#{params[:name]}.cfg" do
    owner node['nagios']['user']
    group node['nagios']['group']
    source "#{params[:conf_type]}/#{params[:name]}.cfg.erb"
    mode 00644
    variables params[:variables]
    notifies :reload, "service[nagios]"
    backup 0
  end
end
