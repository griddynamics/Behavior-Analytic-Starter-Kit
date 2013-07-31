#!/usr/bin/env ruby

require 'rubygems'
require 'net/ssh'
require 'colored'


def print_usage(usage_line)
  puts "Usage: ".bold.yellow + usage_line
end


def print_example(example_line)
  puts "Example: ".bold.yellow + example_line
end


def log_info(message)
  puts "[INFO] ".green + message
end


def log_warn(message)
  puts "[WARN] ".bold.yellow + message
end


def log_error(message)
  puts "[ERROR] ".bold.magenta + message
end


class BaseManager

  SSH_USER = "root"

  attr_accessor :supported_actions

  def initialize
    @masters = []
    @master_service_name = "none"
    @slaves = []
    @slave_service_name = "none"

    @supported_actions = []
  end

  def status
    service_by_ssh(@masters, @master_service_name, :status, "Down")
    service_by_ssh(@slaves, @slave_service_name, :status, "Down")
  end

  def start
    service_by_ssh(@masters, @master_service_name, :start, "Unable to start")
    service_by_ssh(@slaves, @slave_service_name, :start, "Unable to start")
  end

  def stop
    service_by_ssh(@masters, @master_service_name, :stop, "Unable to stop")
    service_by_ssh(@slaves, @slave_service_name, :stop, "Unable to stop")
  end

  def restart
    stop
    start
  end

  def service_by_ssh(machines, service_name, action, error_message="FAILED")
    machines.each do |machine_address|
      Net::SSH.start(machine_address, SSH_USER) do |ssh|
        out, err, exit_code = ssh_exec!(ssh, "service #{service_name} #{action}")

        if exit_code == 0
          log_info("[#{machine_address}]".blue + " #{action} of #{service_name}: " + "[OK]".green)
        else
          log_warn("[#{machine_address}]".bold.cyan + " #{action} of #{service_name}: " + "[#{error_message}]".bold.magenta)
          puts out.strip.yellow unless out.empty?
          puts err.strip.yellow unless err.empty?
          puts
        end
      end
    end
  end

  def do_by_ssh(user, address, command, strict=true)
    Net::SSH.start(address, user) do |ssh|
      _, _, exit_code = ssh_exec!(ssh, command, verbose=true)

      if exit_code == 0
        log_info("[#{address}]".blue + " #{command}: " + "[OK]".green)
      else
        log_warn("[#{address}]".bold.cyan + " #{command}: " + "[#{error_message}]".bold.magenta)

        abort if strict
      end
    end
  end

  def ssh_exec!(ssh, command, verbose=false)
    # Originally submitted by 'flitzwald' over here: http://stackoverflow.com/a/3386375
    stdout_data = ""
    stderr_data = ""
    exit_code = nil

    ssh.open_channel do |channel|
      channel.request_pty

      channel.exec(command) do |ch, success|
        unless success
          log_error("Couldn't execute command (ssh.channel.exec)")
          abort
        end

        channel.on_data do |ch, data|
          stdout_data += data
          print data if verbose
        end

        channel.on_extended_data do |ch, type, data|
          stderr_data += data
          warn data if verbose
        end

        channel.on_request("exit-status") do |ch, data|
          exit_code = data.read_long
        end
      end
    end
    ssh.loop
    [stdout_data, stderr_data, exit_code]
  end

  def perform_action(action)
    action = action.to_sym

    if @supported_actions.include?(action)
      method(action).call
    else
      log_error("Unsupported action: #{action}")
    end
  end

end
