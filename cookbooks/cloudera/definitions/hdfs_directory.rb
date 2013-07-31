
define :hdfs_directory, :owner => "hdfs", :group => "hadoop", :mode => 0755, :action => :create do

  owner = params[:owner]
  group = params[:group]
  mode = params[:mode].to_s(8)

  unless [ :create ].include?(params[:action])
		 raise ArgumentError, "Unsupported action: #{params[:action]}"
  end

  bash "Create the HDFS directory: #{params[:name]}" do
    not_if "hadoop fs -test -e #{params[:name]}"
    code <<-EOH
    sudo -u hdfs hadoop fs -mkdir #{params[:name]}
    sudo -u hdfs hadoop fs -chown #{owner}:#{group} #{params[:name]}
    sudo -u hdfs hadoop fs -chmod #{mode} #{params[:name]}
    EOH
  end
end
