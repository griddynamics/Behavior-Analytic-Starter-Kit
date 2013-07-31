default['ganglia']['conf_dir']    = "/etc/ganglia"
default['ganglia']['state_dir']    = "/var/lib/ganglia"

default['ganglia']['user']  = "ganglia"
default['ganglia']['group'] = "ganglia"

default['ganglia']['grid_name'] = "Grid Dynamics"
default['ganglia']['cluster_name'] = "Deming demo"

# gmond
default['ganglia']['gmond']['ports']['tcp_accept'] = "8649"
default['ganglia']['gmond']['ports']['udp_accept'] = "8649"

# gweb
default['ganglia']['gweb']['docroot']     = "/usr/share/ganglia"
default['ganglia']['gweb']['alias']     = "/ganglia"

# gweb2
default['ganglia']['gweb2']['version'] = "3.5.7"
default['ganglia']['gweb2']['uri'] = "http://sourceforge.net/projects/ganglia/files/ganglia-web/#{node['ganglia']['gweb2']['version']}/ganglia-web-#{node['ganglia']['gweb2']['version']}.tar.gz/download"
default['ganglia']['gweb2']['docroot'] = "/usr/share/ganglia2"
default['ganglia']['gweb2']['alias']   = "/ganglia2"
