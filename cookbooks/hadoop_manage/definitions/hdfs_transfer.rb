define :hdfs_transfer, :source => nil, :destination => nil do

  source = params[:source]
  destination = params[:destination]
  action = params[:name]

  unless [ "get", "put" ].include?("#{action}")
         raise ArgumentError, "Unsupported action: #{params[:action]}"
  end

  case action
    when "put"
      unless File.exist?("#{source}")
        raise ArgumentError, "Source #{source} not found!"      
      end

      execute "Create HDFS dir" do
        command "hadoop fs -mkdir #{destination}"
        user "hdfs"
        not_if "hadoop fs -test -e #{destination}"
      end
      
      execute "Transferring from #{source} to hdfs:/#{destination}" do
        command "hadoop fs -put #{source} #{destination}"
        user "hdfs"
        # not_if "hadoop fs -test -e #{destination}"
      end
    when "get"
      unless File.exist?("#{destination}")
        raise ArgumentError, "Destination #{destination} not found!"      
      end

      execute "Transferring from hdfs://#{source} to #{destination}" do
        command "hadoop fs -get #{source} #{destination}"
        user "root"
        # not_if "hadoop fs -test -e #{source}"
      end
  end
end
