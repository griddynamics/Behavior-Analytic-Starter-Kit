Hadoop Manage Cookbook
=================
Description
-----------
Possibilities of this cookbook:
<ul>
<li>Start/Stop/Restart services</li>
<li>Create/Delete HDFS directiries</li>
<li>Transfer data between local filesystem and HDFS</li>
<li>Download and run mapreduce jobs</li>
</ul>

Requirements
------------
<ul>
<li>Packages: wget, hadoop-client</li>
<li>Installed and initialized hadoop cluster</li>
</ul>

Platform
--------
<ul>
<li>CentOS 6.3 or later</li>
<li>Possiable on any other distro, but not tested yet</li>
</ul>

Attributes
----------
#### hadoop_manage::service
<table>
  <tr>
    <th>Required</th>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td align="middle">*</td>
    <td><tt>['manage']['service']</tt></td>
    <td>List</td>
    <td>List of services</td>
    <td>nil</td>
  </tr>
  <tr>
    <th align="middle">*</th>
    <td><tt>['manage']['action']</tt></td>
    <td>String</td>
    <td>Action for selected services (Start/Stop/Restart)</td>
    <td>start</td>
  </tr>
</table>

#### hadoop_manage::mapreduce
<table>
  <tr>
    <th>Required</th>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td align="middle">*</td>
    <td><tt>['mapreduce']['job_url']</tt></td>
    <td>String</td>
    <td>URL to job file</td>
    <td>nil</td>
  </tr>
  <tr>
    <td align="middle"></td>
    <td><tt>['mapreduce']['job_local_path']</tt></td>
    <td>String</td>
    <td>Directory to download job</td>
    <td>/tmp/jobs/</td>
  </tr>
  <tr>
    <td align="middle"></td>
    <td><tt>['mapreduce']['job_parameters']</tt></td>
    <td>String</td>
    <td>Parameters for job</td>
    <td>nil</td>
  </tr>
</table>

#### hadoop_manage::hdfs_dir
<table>
  <tr>
    <th>Required</th>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td align="middle">*</td>
    <td><tt>['hdfs']['manage']['dirs']</tt></td>
    <td>String</td>
    <td>List of directories</td>
    <td>nil</td>
  </tr>
  <tr>
    <td align="middle">*</td>
    <td><tt>['hdfs']['manage']['owner']</tt></td>
    <td>String</td>
    <td>Directories owner (requied only on create)</td>
    <td>hdfs</td>
  </tr>
  <tr>
    <td align="middle"></td>
    <td><tt>['hdfs']['manage']['action']</tt></td>
    <td>String</td>
    <td>Create or delete directory</td>
    <td>create</td>
  </tr>
</table>

#### hadoop_manage::hdfs_transfer
<table>
  <tr>
    <th>Required</th>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td align="middle">*</td>
    <td><tt>['hdfs']['transfer']['source']</tt></td>
    <td>String</td>
    <td>File or file mask to trasfer</td>
    <td>nil</td>
  </tr>
  <tr>
    <td align="middle">*</td>
    <td><tt>['hdfs']['transfer']['destination']</tt></td>
    <td>String</td>
    <td>Destination directory</td>
    <td>nil</td>
  </tr>
  <tr>
    <td align="middle"></td>
    <td><tt>['hdfs']['transfer']['action']</tt></td>
    <td>String</td>
    <td>HDFS->Local (get) or Local->HDFS (put)</td>
    <td>put</td>
  </tr>
</table>

#### 
Usage
-----
#### hadoop_manage::service
Restarting namenode and jobtracker services
```json
{
  "run_list": [
    "recipe[hadoop_manage::service]"
  ]
  "override_attributes": {
    "manage": {
      "services": [ 
        "hadoop-hdfs-namenode", 
        "hadoop-0.20-mapreduce-jobtracker" 
      ],
      "action": "restart"
    }
  }
}
```

#### hadoop_manage::mapreduce
Running example hadoop job for Pi calculation
```json
{
  "run_list": [
    "recipe[hadoop_manage::mapreduce]"
  ]
  "override_attributes": {
    "mapreduce": {
      "job_url": "http://some/server/hadoop-examples.jar",
      "job_parameters": "pi 100 1000"
    }
  }
}
```

#### hadoop_manage::hdfs_dir
Creating two directories in HDFS: /tmp and /tmp1
```json
{
  "run_list": [
    "recipe[hadoop_manage::hdfs_dir]"
  ]
  "override_attributes": {
    "hdfs": {
      "manage": {
        "dirs": "/tmp,/tmp1",
        "owner": "root",
        "action": "create"
      }
    }
  }
}
```

Deleting /tmp1 directory in HDFS
```json
{
  "run_list": [
    "recipe[hadoop_manage::hdfs_dir]"
  ]
  "override_attributes": {
    "hdfs": {
      "manage": {
        "dirs": "/tmp1",
        "action": "delete"
      }
    }
  }
}
```


#### hadoop_manage::hdfs_transfer
Transfer /tmp/test.log to hdsf://tmp dir
```json
{
  "run_list": [
    "recipe[hadoop_manage::hdfs_dir]"
  ]
  "override_attributes": {
    "hdfs": {
      "transfer": {
        "source": "/tmp/test.log",
        "destination": "/tmp",
        "action": "put"
      }
    }
  }
}
```

Trasfer hdsf://tmp/result/*.out to local /tmp dir
```json
{
  "run_list": [
    "recipe[hadoop_manage::hdfs_dir]"
  ]
  "override_attributes": {
    "hdfs": {
      "transfer": {
        "source": "/tmp/test.log",
        "destination": "/tmp",
        "action": "put"
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
Authors: Nickolay Yurin (nyurin@griddyanmics.com)
