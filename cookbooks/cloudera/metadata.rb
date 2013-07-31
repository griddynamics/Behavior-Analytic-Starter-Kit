name             'cloudera'
maintainer       'Grid Dynamics'
maintainer_email 'nyurin@griddynamics.com'
license          'All rights reserved'
description      'Installs/Configures cloudera'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '0.1.0'

%w{ iptables mysql }.each do |cb|
  depends cb
end

%w{ centos }.each do |os|
  supports os
end
