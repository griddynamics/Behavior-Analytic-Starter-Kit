Сloudera_noha Cookbook
======================
Description
-----------
Cookbook installing CDH 4.2 (Cloudera Hadoop 4.2) in non high availability configuration  

Requirements
------------
<ul>
  <li>iptables</li>
</ul>

Platform
--------
<ul>
  <li>CentOS 6.3 or later</li>
  <li>Possiable on any other RHEL-based distro, but not tested yet</li>
</ul>

Attributes
----------
#### Cluster attributes
<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>[:hadoop][:slave_disks]</tt></td>
    <td>Int</td>
    <td>Number of mounted partitions on datanodes</td>
    <td>1</td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:conf][:core_site]</tt></td>
    <td>Map</td>
    <td>Parameters and values for core-site.xml</td>
    <td></td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:conf][:hdfs_site]</tt></td>
    <td>Map</td>
    <td>Parameters and values for hdfs-site.xml</td>
    <td></td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:conf][:mapred_site]</tt></td>
    <td>Map</td>
    <td>Parameters and values for mapred-site.xml</td>
    <td></td>
  </tr>
</table>

#### Namenode attributes
<table>
  <tr>
    <th>Required</th>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td align='middle'>*</td>
    <td><tt>[:hadoop][:nn][:format]</tt></td>
    <td>Boolean</td>
    <td>Namenode format flag</td>
    <td><tt><b>False</b></tt></td>
  </tr>
  <tr>
    <td align='middle'>*</td>
    <td><tt>[:hadoop][:nn][:host]</tt></td>
    <td>String</td>
    <td>Namenode hostname or IP</td>
    <td>nil</td>
  </tr>
  <tr>
    <td align='middle'></td>
    <td><tt>[:hadoop][:nn][:ports][:http]</tt></td>
    <td>String</td>
    <td>Namenode webui port</td>
    <td>50070</td>
  </tr>
  <tr>
    <td align='middle'></td>
    <td><tt>[:hadoop][:nn][:ports][:rpc]</tt></td>
    <td>String</td>
    <td>Namenode RPC port</td>
    <td>8020</td>
  </tr>
  <tr>
    <td align='middle'></td>
    <td><tt>[:hadoop][:nn][:ports][:zkfc]</tt></td>
    <td>String</td>
    <td>Namenode ZKFC port</td>
    <td>8019</td>
  </tr>
</table>

#### Jobtracker attributes
<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>[:hadoop][:jt][:host]</tt></td>
    <td>String</td>
    <td>Jobtracker hostname or IP</td>
    <td><tt>[:hadoop][:nn][:host]</tt></td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:jt][:ports][:http]</tt></td>
    <td>String</td>
    <td>Jobtracker webui port</td>
    <td>50030</td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:jt][:ports][:rpc]</tt></td>
    <td>String</td>
    <td>Jobtracker RPC port</td>
    <td>8021</td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:jt][:ports][:mrzkfc]</tt></td>
    <td>String</td>
    <td>Mapreduce ZKFC port</td>
    <td>8018</td>
  </tr>
</table>

#### Datanode attributes
<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>[:hadoop][:dn][:ports][:http]</tt></td>
    <td>String</td>
    <td>Datanode webui port</td>
    <td>50075</td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:dn][:ports][:rpc]</tt></td>
    <td>String</td>
    <td>Datanode RPC port</td>
    <td>50010</td>
  </tr>
  <tr>
    <td><tt>[:hadoop][:dn][:ports][:ipc]</tt></td>
    <td>String</td>
    <td>Datanode IPC port</td>
    <td>50020</td>
  </tr>
</table>

#### Tasktracker attributes
<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>[:hadoop][:tt][:ports][:http]</tt></td>
    <td>String</td>
    <td>Tasktracker webui port</td>
    <td>50060</td>
  </tr>
</table>

####
Usage
-----
Include repice `cloudera_noha::<component_name>` to run_list,
to install it
```json
{
  "run_list": [
    "recipe[cloudera_noha::namenode]"
    "recipe[cloudera_noha::jobtracker]"
  ]
  "override_attributes": {
    "hadoop": {
      "nn": {
        "host": "namenode-example-hostname",
        "format": "true"
      }
    }
  }
}
```

Contributing
------------
1. Fork the repository on Github
2. Create a named feature branch (like `add_component_x`)
3. Write you change
4. Write tests for your change (if applicable)
5. Run the tests, ensuring they all pass
6. Submit a Pull Request using Github

License and Authors
-------------------
Authors:
<ul>
<li>Nickolay Yurin (nyurin@griddynamics.com)</li>
<li>Denis Khurtin (dkhurtin@griddynamics.com)</li>
</ul>

