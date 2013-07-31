
class Chef
  module Cloudera
    module SearchNodes
      def find_node_by_role(role)
        if node.roles.include?(role)
          founded_node = node
        else
          founded_nodes = search(:node, "roles:#{role} AND chef_environment:#{node.chef_environment}")

          if founded_nodes.empty?
            Chef::Log.fatal("Nodes with '#{role}' role not found!")
            raise
          elsif founded_nodes.length == 1
            founded_node = founded_nodes.first
          else
            Chef::Log.fatal("Multiple nodes with '#{role}' role was found!")
            raise
          end
        end

        founded_node
      end
    end
  end
end
