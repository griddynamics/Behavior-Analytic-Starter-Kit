#!/usr/bin/env ruby

require File.join(File.dirname(__FILE__), 'base_manager')
require File.join(File.dirname(__FILE__), 'cluster_infrastructure')

class MapReduceManager < BaseManager

  def initialize
    super

    @master_service_name = "hadoop-0.20-mapreduce-jobtrackerha"
    @masters = JOB_TRACKERS

    @slave_service_name = "hadoop-0.20-mapreduce-tasktracker"
    @slaves = TASK_TRACKERS

    @supported_actions = [ :start, :stop, :restart, :status, :init ]
  end

  def init
    if @masters.size == 2
      do_by_ssh(SSH_USER, @masters[0], "sudo -u mapred hadoop mrzkfc -formatZK -force")

      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -mkdir #{JOB_TRACKER_HDFS_DIR}")
      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -chown mapred:hadoop #{JOB_TRACKER_HDFS_DIR}")

      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -mkdir #{MAPRED_SYSTEM_HDFS_DIR}")
      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -chown mapred:hadoop #{MAPRED_SYSTEM_HDFS_DIR}")

      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -mkdir /tmp")
      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -chmod -R 1777 /tmp")

      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -mkdir #{MAPRED_STAGING_HDFS_DIR}")
      do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hadoop fs -chmod -R 1777 #{MAPRED_STAGING_HDFS_DIR}")

    else
      log_error("Must be exactly two Job Trackers. But found #{@masters.size}: " + @masters.join(" "))
    end
  end

end

if __FILE__ == $0
  manager = MapReduceManager.new
  action = ARGV[0]

  if action.nil?
    print_usage "#{$0} ACTION\n  where ACTION is one of: #{manager.supported_actions.join(", ")}"
    print_example "#{$0} status"
    exit 1
  end

  manager.perform_action action
end
