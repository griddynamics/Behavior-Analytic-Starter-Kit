name             's3manage'
maintainer       'Grid Dynamics'
maintainer_email 'nyurin@griddynamics.com'
license          'All rights reserved'
description      'Upload/Download Amazon S3 files'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '0.1.0'

%w{ yum }.each do |cb|
  depends cb
end

%w{ centos }.each do |os|
  supports os
end
