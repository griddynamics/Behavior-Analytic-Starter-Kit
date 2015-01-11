%w{libxml2-devel libxslt-devel rubygem-nokogiri gcc ruby-devel}.each do |pkg|
  package pkg do
    action :install
  end
end

gem_package "nokogiri" do
  	gem_binary("/opt/chef/embedded/bin/gem")
  	version "1.6.1"
    action :install
end

gem_package "aws-sdk" do
  	gem_binary("/opt/chef/embedded/bin/gem")
    action :install
end
