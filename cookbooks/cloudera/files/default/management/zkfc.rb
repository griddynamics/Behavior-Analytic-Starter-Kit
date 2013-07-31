#!/usr/bin/env ruby

require File.join(File.dirname(__FILE__), 'base_manager')
require File.join(File.dirname(__FILE__), 'cluster_infrastructure')

class ZkfcManager < BaseManager

  def initialize
    super

    @master_service_name = "hadoop-hdfs-zkfc"
    @masters = NAME_NODES

    @supported_actions = [ :start, :stop, :restart, :status]
  end

end

if __FILE__ == $0
  manager = ZkfcManager.new
  action = ARGV[0]

  if action.nil?
    print_usage "#{$0} ACTION\n  where ACTION is one of: #{manager.supported_actions.join(", ")}"
    print_example "#{$0} status"
    exit 1
  end

  manager.perform_action action
end
