chef_gem "aws-sdk" do
	action :install
end

s3manage "#{node[:s3manage][:localpath]}" do
	bucket "#{node[:s3manage][:bucket]}"
	s3file "#{node[:s3manage][:s3file]}"
	action "#{node[:s3manage][:action]}"
end
