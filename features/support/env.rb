require 'logger'

$logger = Logger.new(STDERR)
$logger.level = Logger::INFO
$logger.formatter = proc do |severity, datetime, progname, msg|
  "[#{severity}] (#{datetime.strftime('%Y-%m-%d %H:%M:%S')}) #{msg}\n"
end

$qubell_instances = {}

at_exit do
  $logger.info 'Destroying all instances:' unless $qubell_instances.empty?

  $qubell_instances.values.each do |instance|
    $logger.info "Destroying #{instance.get_id}"
    instance.destroy
  end
end
