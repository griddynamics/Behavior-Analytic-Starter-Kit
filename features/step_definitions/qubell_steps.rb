
Given(/^I have a qubell platform$/) do
  @qubell = QubellPlatform.new
end

Given(/^I have a instance of '(.*)'$/) do |app_name|
  @app_instance = $qubell_instances[app_name]
  raise 'Instance should be existed.' if @app_instance.nil?
end

When(/^I start new instance of '(.*)'$/) do |app_name|
  app = @qubell.create_application app_name
  @app_instance = @qubell.launch_application app
  $qubell_instances[app_name] = @app_instance
end

When(/^I run workflow '(.*)'$/) do |workflow_name|
  @qubell.run_workflow @app_instance, workflow_name
end

Then(/^I wait when instance status will changed from '(.*)' to '(.*)'$/) do |from_status, to_status|
  @app_instance.wait_status from_status, to_status
end
