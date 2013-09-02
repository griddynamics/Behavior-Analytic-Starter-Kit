Troubleshooting
===============
[Catalog of common user error message]

Categories:
-----------
- 1. Short description of catalog
- 2. Starter Kit level errors ( functionality workflows,input parameters )
  - 2.1- “Compute new recommendations” workflow fails without transaction log in HDFS.
  - 2.2- “Generate new transaction log” workflow fails without product catalog structure file in your S3 bucket.
  - 2.3- “Load recommendations from S3” workflow fails without recommendation file in your S3 bucket.
  - 2.4- “Save transaction log to S3” workflow fails without transaction log file in HDFS
  - 2.5- “Load transaction log from S3” workflow fails without transaction log file in your S3 bucket.
- 3. Platform and environment level errors ( environment dependencies )
  - 3.1- Instance of application can’t be launched without cloud account settings in environment
  - 3.2- Instance of application can’t be launched with wrong cloud account credentials
  - 3.3- Instance of application can’t be launched with wrong security group settings for cloud account.
  - 3.4- Instance of application can’t be launched without keystore service, configured in environment.
  - 3.5- Instance of application can’t be launched without “s3_bucket_name” property, configured in environment.
  - 3.6- Workflow can’t be launched without policies, configured in environment.
  - 3.7- Failed to retrieve execution status for action RunPreparedExec
  - 3.8- Unknown server error


1. Short description of catalog
-------------------------------
Catalog of common user error messages is to help resolve errors by end-user of Behavior Analytic Starter Kit. We will try to log some possible errors which were happened at particular time and with particular conditions. It can be related with changes in global environment: permanent or temporary. We will try to log some possible errors which were happened at particular time and with particular version of Starter Kit and Qubell.
Starter kit level errors, these are errors with our functionality workflows (buttons): input parameters can be wrong, sequence of steps in scenario can be unsupported  because some workflows are using output from others. Platform or Global environment level errors: errors with state of global environment. It can be: Amazon EC2 or S3 fail, Qubell fail, Cloudera repository fail, public internet access from VM in amazon is failed, github fail. 


2. Starter Kit level errors (functionality workflows, input parameters)
-----------------------------------------------------------------------

**2.1 “Compute new recommendations” workflow fails without transaction log in HDFS.**

Solution: Put transaction log to HDFS by executing “Generate new transaction log” workflow first.

![Error](/Images/2.1a.png)
![Error](/Images/2.1b.png)
***
**2.2 “Generate new transaction log” workflow fails without product catalog structure file in your S3 bucket.**

Solution: Execute “Load product catalog structure to S3” workflow in Web Store application first.

![Error](/Images/2.2a.png)
![Error](/Images/2.2b.png)

***
**2.3 “Load recommendations from S3” workflow fails without recommendation file in your S3 bucket.**

Solution: Execute “Compute new recommendation” workflow in Recommendation Engine application first.

![Error](/Images/2.3.png)

***
**2.4 “Save transaction log to S3” workflow fails without transaction log file in HDFS.**

Solution: Execute “Generate new transaction log” workflow in Recommendation Engine application first.

![Error](/Images/2.4a.png)
![Error](/Images/2.4b.png)

***
**2.5 “Load transaction log from S3” workflow fails without transaction log file in your S3 bucket.**

Solution: Execute “Save transaction log to S3” workflow in Recommendation Engine application first.

![Error](/Images/2.5a.png)
![Error](/Images/2.5b.png)


3. Platform and environment level errors (environment dependencies)
-------------------------------------------------------------------

**3.1 Instance of application can’t be launched without cloud account settings in environment.**

Solution: Setup your cloud account in environment according to documentation. 

![Error](/Images/3.1.png)
***

**3.2 Instance of application can’t be launched with wrong cloud account credentials.**

Solution: Setup valid credentials (access_key and secret_key) to your cloud account according to documentation.

![Error](/Images/3.2.png)
***

**3.3 Instance of application can’t be launched with wrong security group settings for cloud account.**

Solution: Setup security group for your cloud account according to documentation.

![Error](/Images/3.3.png)
***

**3.4 Instance of application can’t be launched without keystore service, configured in environment.**

Solution: Setup keystore service in environment services  according to documentation.

![Error](/Images/3.4.png)
***

**3.5 Instance of application can’t be launched without “s3_bucket_name” property, configured in environment.**

Solution: Setup “s3_bucket_name” property in environment properties according to documentation.

![Error](/Images/3.5.png)
***

**3.6 Workflow can’t be launched without policies, configured in environment.**

Solution: Setup policies in environment  according to documentation.

![Error](/Images/3.6a.png)
![Error](/Images/3.6b.png)
***

**3.7 Failed to retrieve execution status for action RunPreparedExec.**

Solution: If you have such issue with launch workflow ,then please destroy your application and launch it again. Such issue can be related with network access between Qubell agent and ssh server launched on virtual machine.

![Error](/Images/3.7.png)
***

**3.8 Unknown server error.**

Solution: Please refresh page in your browser. Such issue can be related with network issue and java script specific.

![Error](/Images/3.8.png)
Go back to the [Mainpage](README.md)



