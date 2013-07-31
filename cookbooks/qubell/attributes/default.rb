default['scm']['provider'] = 'git'

default['database']['driver'] = 'com.mysql.jdbc.Driver'
default['database']['adapter'] = 'mysql'
default['database']['port'] = 3306

default['haproxy']['enable_admin'] = true
default['haproxy.rebalance']['nodes'] = ["localhost"]

default['maven']['version'] = 2
default['maven']['m2_home'] = '/usr/local/maven'
default['maven']['2']['version'] = "2.2.1"
default['maven']['2']['url'] = "http://apache.mirrors.tds.net/maven/maven-2/#{node['maven']['2']['version']}/binaries/apache-maven-#{node['maven']['2']['version']}-bin.tar.gz"
default['maven']['2']['checksum'] = "b9a36559486a862abfc7fb2064fd1429f20333caae95ac51215d06d72c02d376"
