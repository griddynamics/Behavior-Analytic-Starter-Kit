name             'nagios'
maintainer       'Grid Dynamics'
maintainer_email 'dkhurtin@griddynamics.com'
license          'Apache 2.0'
description      'Installs and configures Nagios monitoring tool'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '0.1.0'

recipe 'nagios', 'Includes the client recipe.'
recipe 'nagios::server', 'Installs and configures a nagios server'

%w{ apache2 yum }.each do |cb|
  depends cb
end

supports 'centos'
