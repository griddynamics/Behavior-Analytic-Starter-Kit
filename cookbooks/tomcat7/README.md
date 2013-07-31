Tomcat 7
========
Description
-----------
Installs and configures Tomcat 7.

Requirements
------------
We are using Oracle JDK, but you have to download it your private server from download.oracle.com

The following Opscode cookbooks are dependencies:
<ul>
  <li>Java</li>
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
    <td><tt>[:tomcat7][:version]</tt></td>
    <td>String</td>
    <td>Version of tomcat</td>
    <td>7.0.41</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:user]</tt></td>
    <td>String</td>
    <td>Tomcat user</td>
    <td>tomcat</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:group]</tt></td>
    <td>String</td>
    <td>Tomcat group</td>
    <td>tomcat</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:target]</tt></td>
    <td>String</td>
    <td>Target installation directory</td>
    <td>/usr/share</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:port]</tt></td>
    <td>Integer</td>
    <td>Network port for Tomcat HTTP connector</td>
    <td>8080</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:ssl_port]</tt></td>
    <td>Integer</td>
    <td>Network port for Tomcat SSL connector</td>
    <td>8080</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:ajp_port]</tt></td>
    <td>Integer</td>
    <td>Network port for Tomcat AJP connector</td>
    <td>8080</td>
  </tr>
  <tr>
    <td><tt>[:tomcat7][:java_options]</tt></td>
    <td>String</td>
    <td>Tomcat Java options</td>
    <td>-Xmx768M -Dajva.awt.headless=true</td>
  </tr>
  <tr>

  </tr>
</table>

Usage
-----
Include tomcat7 default recipe to your runlist
Add url binary and checksum for Oracle JDK
```json
{
  "run_list": [
    "recipe[tomcat7::default]"
  ]
  "override_attributes": {
    "tomcat" => {
        "java_options" => " -Xmx128M -Djava.awt.headless=true"
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
