#
# Cookbook Name:: tomcat7
# Attributes:: default
#
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

default[:tomcat7][:version] = "7.0.41"
default[:tomcat7][:user] = "tomcat"
default[:tomcat7][:group] = "tomcat"
default[:tomcat7][:ports][:http] = 8080
default[:tomcat7][:ports][:ssl] = 8443
default[:tomcat7][:ports][:ajp] = 8009
default[:tomcat7][:java_options] = " -Xmx768M -Djava.awt.headless=true"
default[:tomcat7][:tarball] = "apache-tomcat-#{node[:tomcat7][:version]}.tar.gz"
default[:tomcat7][:url] = "http://archive.apache.org/dist/tomcat/tomcat-7/v#{node[:tomcat7][:version]}/bin/#{node[:tomcat7][:tarball]}"
default[:tomcat7][:home] = "/usr/share/tomcat"
default[:tomcat7][:target] = File.dirname("#{node[:tomcat7][:home]}")
default[:tomcat7][:java_home] = "/usr/lib/jvm/java/"