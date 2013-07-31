
define :simple_firewall, :ports => [], :protocol => :tcp, :action => :open do

  bin_path = node[:iptables][:bin_path]
  ports = params[:ports]
  protocol = params[:protocol]

  if ports.empty?
	raise ArgumentError, "ports list is empty"
  end

  unless [:open, :close].include?(params[:action])
	raise ArgumentError, "Unsupported action: #{params[:action]}"
  end

  if params[:action] == :open
    action = "ACCEPT"
  else
    action = "REJECT"
  end

  ports.each do |port|
    execute "open #{port} port" do
      command "#{bin_path} -I INPUT     -p #{protocol} -m state --state NEW -m #{protocol} --dport #{port} -j #{action}"
      notifies :run, "execute[iptables save]", :delayed
    end
  end
end
