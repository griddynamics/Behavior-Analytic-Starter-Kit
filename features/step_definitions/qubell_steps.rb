
Given(/^I have a qubell platform$/) do
  @qubell = QubellPlatform.new
end

When(/^I start new instance of '(.*)'$/) do |app_name|
  @app = @qubell.create_application app_name
  @app_instance = @qubell.launch_application @app
end

When(/^I run workflow '(.*)'$/) do |workflow_name|
  @qubell.run_workflow @app_instance, workflow_name
end

Then(/^I wait when instance status will changed from '(.*)' to '(.*)'$/) do |from_status, to_status|
  instance_info = @qubell.fetch_instance_info @app_instance

  while instance_info['status'] == from_status
    sleep 5
    instance_info = @qubell.fetch_instance_info @app_instance
  end

  if instance_info['status'] != to_status
    raise "Instance has #{instance_info['status']} status, expected: #{to_status}"
  end
end
