#!/usr/bin/env ruby

require File.join(File.dirname(__FILE__), 'base_manager')
require File.join(File.dirname(__FILE__), 'cluster_infrastructure')

class MapRedZkfcManager < BaseManager

  def initialize
    super

    @master_service_name = "hadoop-0.20-mapreduce-zkfc"
    @masters = JOB_TRACKERS

    @supported_actions = [ :start, :stop, :restart, :status]
  end

end

if __FILE__ == $0
  manager = MapRedZkfcManager.new
  action = ARGV[0]

  if action.nil?
    print_usage "#{$0} ACTION\n  where ACTION is one of: #{manager.supported_actions.join(", ")}"
    print_example "#{$0} status"
    exit 1
  end

  manager.perform_action action
end
