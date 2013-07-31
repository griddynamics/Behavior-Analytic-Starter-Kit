ECASK Demo Application
======================

Description
-----------
Installs and setup Demo Store

Requirements
------------
We are using Oracle JDK, but you have to download it your private server from download.oracle.com

The following Opscode cookbooks are dependencies:
<ul>
  <li>tomcat</li>
  <li>mysql</li>
</ul>

Platforms
---------
<ul>
  <li>CentOS 6.3 or later</li>
  <li>Possiable on any other RHEL-based distro, but not tested yet</li>
</ul>

Attributes
----------
<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>[:ecask][:db_user]</tt></td>
    <td>String</td>
    <td>Demo Store database username</td>
    <td>ecask</td>
  </tr>
  <tr>
    <td><tt>[:ecask][:db_password]</tt></td>
    <td>String</td>
    <td>Demo Store database user password</td>
    <td>ecask_password</td>
  </tr>
  <tr>
    <td><tt>[:ecack][:db_root_password]</tt></td>
    <td>String</td>
    <td>Root password for MySQL</td>
    <td>PaSsWoRd</td>
  </tr>
  <tr>
    <td><tt>[:ecask][:tomcat_opts]</tt></td>
    <td>String</td>
    <td>Java options for Tomcat server</td>
    <td>-server -Xms256m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Druntime.environment=development</td>
  </tr>
</table>

Usage
-----
Include ecask::default recipe to your runlist
Add url binary and checksum for Oracle JDK for tomcat
```json
{
  "run_list": [
    "recipe[ecask::default]"
  ]
  "override_attributes": {
    "ecask" => {
        "db_password" => "SoMe_OtHeR_pAsSwOrD"
    }
    "java" => {
        "6" => {
            "x86_64" => {
                "url" => "url_to_jdk_binary"
                "checksum" => "jdk_checksum(sha256)"
            }
        }
    }
  }
}
```

License and Author
------------------
Author:: Nickolay Yurin (nyurin@griddynamics.com)
