Webstore Demo Application
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
    <td><tt>[:webstore][:db_user]</tt></td>
    <td>String</td>
    <td>Demo Store database username</td>
    <td>ecask</td>
  </tr>
  <tr>
    <td><tt>[:webstore][:db_password]</tt></td>
    <td>String</td>
    <td>Demo Store database user password</td>
    <td>ecask_password</td>
  </tr>
  <tr>
    <td><tt>[:webstore][:db_root_password]</tt></td>
    <td>String</td>
    <td>Root password for MySQL</td>
    <td>PaSsWoRd</td>
  </tr>
  <tr>
    <td><tt>[:webstore][:tomcat_opts]</tt></td>
    <td>String</td>
    <td>Java options for Tomcat server</td>
    <td>-server -Xms256m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Druntime.environment=development</td>
  </tr>
</table>

Usage
-----
Include webstore::default recipe to your runlist
```json
{
  "run_list": [
    "recipe[webstore::default]"
  ]
  "override_attributes": {
    "webstore" => {
        "db_password" => "SoMe_OtHeR_pAsSwOrD"
    }
  }
}
```

License and Author
------------------
Author:: Nickolay Yurin (nyurin@griddynamics.com)
