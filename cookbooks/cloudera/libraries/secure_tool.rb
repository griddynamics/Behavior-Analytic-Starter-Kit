#
# Cookbook Name:: cloudera
# Library:: secret_key
# Author:: Denis Khurtin <dkhurtin@griddynamics.com>
#
# Analog secure_password from openssl cookbook

require 'openssl'

module Opscode
  module OpenSSL
    module SecureTool
      def secret_key
        pw = String.new

        while pw.length < 60
          pw << ::OpenSSL::Random.random_bytes(1).gsub(/\W/, '')
        end

        pw
      end
    end
  end
end
