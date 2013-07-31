
package "sudo"
package "wget"

template "/etc/yum.repos.d/cloudera-cdh4.repo" do
  source "cloudera-cdh4.repo.erb"
  variables(:cdh_version => node[:cloudera][:cdh_version])
  mode 0644
  owner "root"
  group "root"
end

execute "Import RPM-GPG-KEY-cloudera" do
  not_if { ::File.exists?("/etc/pki/rpm-gpg/RPM-GPG-KEY-cloudera") }
  command "rpm --import http://archive.cloudera.com/cdh4/redhat/6/x86_64/cdh/RPM-GPG-KEY-cloudera"
  action :run
end
