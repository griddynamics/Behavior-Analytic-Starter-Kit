
define :management_script do

  cookbook_file "#{node[:hadoop][:management][:path]}/#{params[:name]}" do
    source "management/#{params[:name]}"
    mode 0755
    owner "root"
    group "root"
  end
end
