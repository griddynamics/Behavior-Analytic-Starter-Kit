define :hdfs_directory, :owner => "hdfs", :group => "hadoop", :mode => 0755, :action => :create do

  owner = params[:owner]
  group = params[:group]
  mode = params[:mode].to_s(8)

  unless [ "create", "delete" ].include?("#{params[:action]}")
         raise ArgumentError, "Unsupported action: #{params[:action]}"
  end

  case params[:action]
    when "create"
      bash "Create the HDFS directory: #{params[:name]}" do
        not_if "hadoop fs -test -e #{params[:name]}"
        user "hdfs"
        code <<-EOH
        hadoop fs -mkdir #{params[:name]}
        hadoop fs -chown #{owner}:#{group} #{params[:name]}
        hadoop fs -chmod #{mode} #{params[:name]}
        EOH
      end
    when "delete"
      bash "Delete the HDFS directory: #{params[:name]}" do
        only_if "hadoop fs -test -e #{params[:name]}"
        user "hdfs"
        code <<-EOH
        hadoop fs -rm -r -skipTrash #{params[:name]}
        EOH
      end
  end  
end
