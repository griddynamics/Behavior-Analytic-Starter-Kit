
require 'json'
require 'rest_client'


class QubellPlatform

  def initialize
    @qubell_api_url = ENV['QUBELL_API_URL']
    @qubell_user = ENV['QUBELL_TEST_USER']
    @qubell_password = ENV['QUBELL_TEST_PASSWORD']

    qubell_organization_name = ENV['QUBELL_ORGANIZATION']
    qubell_organization_id = find_organization_id(qubell_organization_name)

    test_env_name = ENV['QUBELL_TEST_ENV']
    @qubell_environment_id = find_environment_id(qubell_organization_id, test_env_name)

    recommendation_engine_app_name = ENV['RECOMMENDATION_ENGINE_APP_NAME']
    web_store_app_name = ENV['WEB_STORE_APP_NAME']

    @applications = {
        'Recommendation Engine' => find_application_id(qubell_organization_id, recommendation_engine_app_name),
        'Web Store' => find_application_id(qubell_organization_id, web_store_app_name),
    }
  end

  def api_call(method, api_path, request_body)

    end_point = "#{@qubell_api_url}/#{api_path}"

    $logger.debug "API CALL: '#{method}' '#{end_point}' '#{request_body}'"

    resp = RestClient::Request.new(
        :method => method,
        :url => end_point,
        :user => @qubell_user,
        :password => @qubell_password,
        :headers => { :accept => :json, :content_type => :json },
        :payload => request_body
    ).execute

    result = resp.to_str

    $logger.debug "Response: #{result}"

    JSON.parse(result)
  end

  def api_get(api_path)
    api_call :get, api_path, '{}'
  end

  def api_post(api_path, request_body)
    api_call :post, api_path, request_body
  end

  def find_organization_id(org_name)
    organizations = api_get "organizations"
    organization = organizations.find{ |org| org['name'] == org_name }

    raise "Organization '#{org_name}' not found" if organization.nil?

    organization['id']
  end

  def find_environment_id(org_id, env_name)
    environments = api_get "organizations/#{org_id}/environments"
    environment = environments.find{ |env| env['name'] == env_name }

    raise "Environment '#{env_name}' not found in org '#{org_id}'" if environment.nil?

    environment['id']
  end

  def find_application_id(org_id, app_name)
    applications = api_get "organizations/#{org_id}/applications"
    application = applications.find{ |app| app['name'] == app_name }

    raise "Application '#{app_name}' not found in org '#{org_id}'" if application.nil?

    application['id']
  end

  def create_application(name)
    QubellApplication.new(name, @applications[name], self)
  end

  # launch new instance
  # destroyInterval=1800000 - 30 min
  # destroyInterval=3600000 - 1 hour
  # destroyInterval=7200000 - 2 hours
  def launch_application(application)
    launch_response = api_post("applications/#{application.get_id}/launch",
                               '{"destroyInterval":3600000,"environmentId":"' + @qubell_environment_id + '"}')

    QubellApplicationInstance.new application, launch_response['id'], self
  end

  def run_workflow(instance, workflow_name)
    $logger.debug "Run '#{workflow_name}' on #{instance}"
    api_post "instances/#{instance.get_id}/#{workflow_name}", '{}'
  end

  def fetch_instance_info(instance)
    api_get "instances/#{instance.get_id}"
  end

  def destroy_instance(instance)
    run_workflow instance, 'destroy'
  end
end

class QubellApplication

  def initialize(application_name, application_id, qubell_platform)
    @name = application_name
    @app_id = application_id
    @qubell = qubell_platform
  end

  def get_id
    @app_id
  end

  def to_s
    "QubellApplication{ id: #{@app_id}, name: '#{@name}' }"
  end
end

class QubellApplicationInstance

  def initialize(application, instance_id, qubell_platform)
    @app = application
    @id = instance_id
    @qubell = qubell_platform
  end

  def get_id
    @id
  end

  def fetch_status
    instance_info = @qubell.fetch_instance_info self
    instance_info['status']
  end

  def wait_status(from_status, to_status)
    instance_status = fetch_status

    while instance_status == from_status
      sleep 5
      instance_status = fetch_status
    end

    if instance_status != to_status
      raise "Instance has #{instance_status} status, but expected: #{to_status}"
    end
  end

  def destroy
    @qubell.destroy_instance self
    wait_status 'Destroying', 'Destroyed'
  end

  def to_s
    "QubellApplicationInstance{ id: #{@id}, app: #{@app} }"
  end
end
