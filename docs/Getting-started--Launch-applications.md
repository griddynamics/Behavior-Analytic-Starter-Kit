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

The Qubell platform constructs two applications from Behavior analytics starter kit configuration defined in Behavior Analytics Starter Kit manifests (behavior analytics platform and web store). 
During the launch process, the platform provisions the virtual machines, installs the necessary software packages using Chef, sets up cluster configuration from the property file. 
Once behavior analytics platform with Hadoop cluster and sample store are up everything is ready to use Starter Kit.
To learn more about application launching please read [Qubell documentation](http://docs.qubell.com/concepts/applications.html)

Important notice!
-----------------
Qubell sets time to destroy for each running instance to 1 hour by default. 
So after 1 hour from start your instance will be destroyed, 
all VMs in instance will be shutdown, all data will be lost.
You can change default time to destroy by launching instance with "Advanced" button and set appropriate time in "Destroy in" field.
Also you can reschedule destroying time for already running instances by pressing "Jobs" button on instance output panel and then press "Reschedule" button at the end of jobs list. 

![launch apps][launch_apps]

[launch_apps]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Developer%20Guide/launch_apps.png
