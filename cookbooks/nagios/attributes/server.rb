
default['nagios']['server']['service_name'] = 'nagios'
default['nagios']['server']['mail_command'] = '/bin/mail'

default['nagios']['conf_dir']   = '/etc/nagios'
default['nagios']['config_dir'] = '/etc/nagios/conf.d'
default['nagios']['private_conf_dir'] = '/etc/nagios/private'

default['nagios']['log_dir']    = '/var/log/nagios'
default['nagios']['cache_dir']  = '/var/cache/nagios'
default['nagios']['state_dir']  = '/var/lib/nagios'
default['nagios']['run_dir']    = '/var/run/nagios'

default['nagios']['docroot']     = '/usr/share/nagios/html'
default['nagios']['cgi_bin_dir'] = '/usr/lib64/nagios/cgi-bin'

default['nagios']['enable_ssl'] = false
default['nagios']['http_port']  = node['nagios']['enable_ssl'] ? '443' : '80'
default['nagios']['server_name'] = node.has_key?(:domain) ? "nagios.#{domain}" : 'nagios'
default['nagios']['ssl_req'] = '/C=US/ST=Several/L=Locality/O=Example/OU=Operations/' +
    "CN=#{node['nagios']['server_name']}/emailAddress=ops@#{node['nagios']['server_name']}"

default['nagios']['notifications_enabled']   = 1
default['nagios']['check_external_commands'] = true
default['nagios']['default_contact_groups']  = %w{admins}
default['nagios']['sysadmin_email']          = "root@localhost"
default['nagios']['sysadmin_sms_email']      = "root@localhost"
default['nagios']['users_data_bag_group']    = "sysadmin"
default['nagios']['host_name_attribute']     = "hostname"

# This setting is effectively sets the minimum interval (in seconds) nagios can handle.
# Other interval settings provided in seconds will calculate their actual from this value, since nagios works in 'time units' rather than allowing definitions everywhere in seconds

default['nagios']['templates'] = Mash.new
default['nagios']['interval_length'] = 60

# Provide all interval values in seconds
default['nagios']['default_host']['check_interval']     = 15
default['nagios']['default_host']['retry_interval']     = 15
default['nagios']['default_host']['max_check_attempts'] = 1
default['nagios']['default_host']['notification_interval'] = 300
default['nagios']['default_host']['flap_detection'] = true

default['nagios']['default_service']['check_interval']     = 60
default['nagios']['default_service']['retry_interval']     = 15
default['nagios']['default_service']['max_check_attempts'] = 3
default['nagios']['default_service']['notification_interval'] = 1200
default['nagios']['default_service']['flap_detection'] = true
