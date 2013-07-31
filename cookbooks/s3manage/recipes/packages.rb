%w{libxml2-devel libxslt-devel rubygem-nokogiri gcc ruby-devel}.each do |pkg|
  package pkg do
    action :install
  end
end