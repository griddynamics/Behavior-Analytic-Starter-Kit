#!/usr/bin/env ruby

require File.join(File.dirname(__FILE__), 'hdfs')
require File.join(File.dirname(__FILE__), 'journals')
require File.join(File.dirname(__FILE__), 'mapred')
require File.join(File.dirname(__FILE__), 'zookeeper')
require File.join(File.dirname(__FILE__), 'zkfc')
require File.join(File.dirname(__FILE__), 'mrzkfc')

service, action = ARGV

if service.nil? || action.nil?
  puts "Usage: ".bold.yellow + "#{$0} SERVICE_NAME ACTION"
  puts "  where SERVICE_NAME is one of:"
  puts "    hdfs"
  puts "    journal"
  puts "    mapred"
  puts "    zookeeper"
  puts "    zkfc"
  puts "    mrzkfc"
  puts "  and ACTION is one of:"
  puts "    status"
  puts "    start"
  puts "    stop"
  puts "    restart"
  puts "Example: ".bold.yellow + "#{$0} hdfs status"
  exit 1
end

case service.to_sym
  when :hdfs
    manager = HdfsManager.new
  when :journals
    manager = JournalManager.new
  when :mapred
    manager = MapReduceManager.new
  when :zookeeper
    manager = ZookeeperManager.new
  when :zkfc
    manager = ZkfcManager.new
  when :mrzkfc
    manager = MapRedZkfcManager.new
  else
    log_error("Unknown service: " + service)
    abort
end

manager.perform_action action
