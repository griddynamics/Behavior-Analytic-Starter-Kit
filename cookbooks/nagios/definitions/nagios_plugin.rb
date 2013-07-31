
define :nagios_plugin do

  cookbook_file "#{node['nagios']['plugin_dir']}/#{params[:name]}" do
    source "#{params[:name]}"
    mode 0755
    owner "root"
    group "root"
  end
end
