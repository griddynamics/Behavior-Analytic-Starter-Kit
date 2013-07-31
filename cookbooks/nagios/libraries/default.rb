
def nagios_boolean(true_or_false)
  true_or_false ? "1" : "0"
end

def nagios_interval(seconds)
  if seconds.to_i != 0 && seconds.to_i < node['nagios']['interval_length'].to_i
    raise ArgumentError, "Specified nagios interval of #{seconds} seconds must ether be zero or be equal to or greater than the default interval length of #{node['nagios']['interval_length']}"
  end
  interval = seconds.to_f / node['nagios']['interval_length']
  if interval != interval.to_i
    raise ArgumentError, "Specified nagios interval of #{seconds} seconds must be a multiple of the interval length of #{node['nagios']['interval_length']}"
  end
  interval
end

def nagios_attr(name)
  node['nagios'][name]
end
