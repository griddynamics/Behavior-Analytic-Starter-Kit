include_recipe 'yum::epel'

%w{libxml2-devel libxslt-devel rubygem-nokogiri gcc ruby-devel make}.each do |pkg|
  package pkg do
    action :install
  end
end

gem_package "aws-sdk" do
  	gem_binary("/opt/chef/embedded/bin/gem")
	action :install
end
