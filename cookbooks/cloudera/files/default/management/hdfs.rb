#!/usr/bin/env ruby

require File.join(File.dirname(__FILE__), 'base_manager')
require File.join(File.dirname(__FILE__), 'cluster_infrastructure')

class HdfsManager < BaseManager

  def initialize
    super

    @master_service_name = "hadoop-hdfs-namenode"
    @masters = NAME_NODES

    @slave_service_name = "hadoop-hdfs-datanode"
    @slaves = DATA_NODES

    @supported_actions = [ :start, :stop, :restart, :status, :init, :create_users ]
  end

  def init
    if @masters.size == 2
      do_by_ssh(SSH_USER, @masters[0], "sudo -u hdfs hdfs namenode -format -force")
      do_by_ssh(SSH_USER, @masters[0], "sudo hdfs zkfc -formatZK -force")

      # Doesn't work properly die CDH4 bug https://issues.apache.org/jira/browse/HDFS-3752
      #do_by_ssh(SSH_USER, @masters[1], "sudo -u hdfs hdfs namenode -bootstrapStandby")

      # workaround:
      do_by_ssh(SSH_USER, @masters[1], "sudo rm -rf #{NAME_NODE_DIR}/*")
      do_by_ssh(SSH_USER, @masters[1], "sudo scp -r -oStrictHostKeyChecking=no '#{SSH_USER}@#{@masters[0]}:#{NAME_NODE_DIR}/*' #{NAME_NODE_DIR}")
      do_by_ssh(SSH_USER, @masters[1], "sudo chown -R hdfs.hadoop #{NAME_NODE_DIR}")
    else
      log_error("Must be exactly two NameNodes. But found #{@masters.size}: " + @masters.join(" "))
    end
  end

  def create_users
    if @masters.size > 0
      users = ARGV[1, ARGV.length]

      if users.nil? or users.empty?
        puts "Usage: ".bold.yellow + "#{$0} ACTION"
        puts "  where ACTION is one of: #{@supported_actions.join(", ")}"
        puts "Example: ".bold.yellow + "#{$0} status"
        exit 1
      end

      users.each do |user_name|
        if user_name =~ /^[A-Za-z]\w+$/
          do_by_ssh(SSH_USER, @masters[0], "sudo -u hdfs hadoop fs -mkdir  /user/#{user_name}")
          do_by_ssh(SSH_USER, @masters[0], "sudo -u hdfs hadoop fs -chown #{user_name} /user/#{user_name}")
        else
          log_error("Invalid user name: " + user_name)
        end
      end
    else
      log_error("Must be at least one NameNode.")
    end
  end

end

if __FILE__ == $0
  manager = HdfsManager.new
  action = ARGV[0]

  if action.nil?
    print_usage "#{$0} ACTION\n  where ACTION is one of: #{manager.supported_actions.join(", ")}"
    print_example "#{$0} status"
    exit 1
  end

  manager.perform_action action
end
