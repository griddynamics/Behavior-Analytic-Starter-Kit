Launch applications
===================

Launch Sample store
-------------------

Click “Launch” button on sample store application dashboard to launch the sample store application in your environment.
It should take about 10 minutes for the application to come up.
Once sample store launched successfully you can see URL to sample store web UI on application output panel.

Launch Behavior analytics platform
----------------------------------

Click “Launch” button on behavior analytics platform application dashboard to launch the behavior analytics platform application in the default environment.
Hadoop cluster will be deployed. 
It should take about 10 minutes (depends on slave node number) for the application to come up.

The Qubell platform constructs two applications from Behavior analytics starter kit configuration defined in Behavior Analytics Starter Kit manifests (behavior analytics platform and sample store). During the launch process, the platform provisions the virtual machines, installs the necessary software packages using Chef, installs transaction log generator, recommendation processor and sample store (web UI), sets up cluster configuration from the property file. 
Once behavior analytics platform with Hadoop cluster and sample store are up everything is ready to use Starter Kit.
