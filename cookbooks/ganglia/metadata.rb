name             'ganglia'
maintainer       'Grid Dynamics'
maintainer_email 'dkhurtin@griddynamics.com'
license          'Apache 2.0'
description      'Installs and configures Ganglia monitoring tool'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '0.1.0'

%w{ apache2 yum selinux iptables }.each do |cb|
  depends cb
end
