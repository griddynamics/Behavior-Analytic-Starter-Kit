execute "Create webstore db" do
	command %Q{mysql -u root -p#{node[:webstore][:db_root_password]} << EOF
		drop database if exists ecask;
		create database ecask;
		grant all privileges on ecask.* to ecask@localhost identified by 'ecask_password';
EOF}
end