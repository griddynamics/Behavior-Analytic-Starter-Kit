maintainer       "Qubell"
maintainer_email "apanasenko@qubell.com"
license          "Apache 2.0"
description      "Installs/Configures Application"
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          "0.0.5"

%w{ debian ubuntu centos suse fedora redhat }.each do |os|
  supports os
end

%w{ maven tomcat ark yum yumrepo jpackage git }.each do |cb|
  depends cb
end
