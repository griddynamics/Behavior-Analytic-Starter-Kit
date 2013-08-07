Dependencies
------------

Behavior analytic starter kit was developed and tested in specific environment. 
We would like to describe all important dependencies of starter kit.

Cloud specific AWS EC2:
* Services: run instance and s3 storage services: get, put

Network access to public resources:
* Cloudera repositories
* CentOS repositories
* ntp servers
* Network access to internal resources:
* S3 service
* VMs in the same security group

Java distribution:
* Open JDK

Vm image & OS distribution:
* starter kit was tested on CentOS vm images , CentOS version is 6.4
* ec2-user should exist and should have access root privileges through sudo
* ssh key should be placed in authorized_keys
* firewall should be turn off
* selinux policies should be turn off


Artifacts access:
* S3 bucket with cookbook package
* S3 bucket with binaries - transaction log generator, recommendation processor.

Configuration settings:
Qubell - Environment policies:
* vmIdentity
* s3_access_key and s3_secret_key

Qubell features:
* Qubell version 1.19
* chef-solo
