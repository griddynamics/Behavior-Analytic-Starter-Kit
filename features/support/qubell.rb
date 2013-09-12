
require 'json'
require 'rest_client'


class QubellPlatform

  QUBELL_API_URL = 'https://express.qubell.com/api/1'

  TEST_ENV_ID = '523034c8e4b07d962112af55'

  APPLICATION_IDS = {
      'Web Store' => '522f17cce4b07d962112ad1e',
      'Recommendation Engine' => '522f17cce4b07d962112ad1d',
  }

  def api_call(method, api_path, body)

    $logger.debug "API CALL: '#{method}' '#{QUBELL_API_URL}/#{api_path}' '#{body}'"

    resp = RestClient::Request.new(
        :method => method,
        :url => "#{QUBELL_API_URL}/#{api_path}",
        :user => ENV['QUBELL_TEST_USER'],
        :password => ENV['QUBELL_TEST_PASSWORD'],
        :headers => { :accept => :json, :content_type => :json },
        :payload => body
    ).execute

    result = resp.to_str

    $logger.debug "Response: #{result}"

    JSON.parse(result)
  end

  def api_get(api_path)
    api_call :get, api_path, '{}'
  end

  def api_post(api_path, body)
    api_call :post, api_path, body
  end

  def create_application(name)
    QubellApplication.new(name, APPLICATION_IDS[name], self)
  end

  # launch new instance
  # destroyInterval=1800000 - 30 min
  # destroyInterval=3600000 - 1 hour
  # destroyInterval=7200000 - 2 hours
  def launch_application(application)
    launch_response = api_post("applications/#{application.get_id}/launch",
                               '{"destroyInterval":3600000,"environmentId":"' + TEST_ENV_ID + '"}')

    QubellApplicationInstance.new application, launch_response['id'], self
  end

  def fetch_instance_info(instance)
    api_get "instances/#{instance.get_id}"
  end

  def destroy_instance(instance)
    run_workflow instance, 'destroy'
  end

  def run_workflow(instance, workflow_name)
    $logger.debug "Run '#{workflow_name}' on #{instance}"
    api_post "instances/#{instance.get_id}/#{workflow_name}", '{}'
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
