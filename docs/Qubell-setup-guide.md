Qubell setup guide
==================

Guide for new Qubell user
-------------------------
Before you begin using of starter kit you need to obtain an Amazon EC2 account, capable of creating EC2 nodes and S3 usage. 
The EC2 security group ‘default’ has to allow connections on the following ports:
* 22 (SSH)
* 8080
* 443 (НTTPS)
* 50030 (HTTP, jobtracker console)
* 50070 (HTTP, namenode console)

You should [setup 'default' security group][aws_security_group_doc] to open ports listed above.

If you already have Qubell account and have defined Cloud Account please skip related steps 1 and 2

1. [Sign up for an Express account](http://qubell.com/sign-up/) on Qubell Adaptive PaaS. It is free and takes only a few minutes.
2. Create Cloud Account with Amazon secret and access keys.
    - Go to "Platform->Cloud Accounts" page and press "Add a cloud account" button
        ![add cloud acccount][add_cloud_acccount]
    - Specify account name, provider="Amazon EC2", AWS credentials and security group and press "Save"
        ![setup cloud account][setup_cloud_account]
3. Create Service Vault
    - Go to "Platform->Services" page and press "Add a service" button
        ![add service][add_service]
    - Specify service name and type="Secure Vault 2.0" then press "Add". SSH key pair will be generated automatically.
        ![setup service][setup_service]
4. Get, upload, regenerate keys (You can skip this step)
    - To get keys go to "Platform->Services" page and download private and public keys
    - To upload key press "Edit", specify service name and press "Upload" button
    - To regenerate key press "Edit" and then "Regenarate" button
        ![edit service][edit_service]
5. Create Environment
    - Go to "Environments" page, press "Add an environment" button
        ![add env][add_env]
    - Specify environment name and press "Add". New environment will be open.
        ![setup env][setup_env]
    - Press "Set a cloud account" and specify previously created cloud account name then press "Save"
        ![set cloud account][set_cloud_account]
    - Press "Add a service" button and select previously created service name then press "Add"
6. Add Environment Policies     
    - s3manage.aws_access_key_id
        Go to "Environments" page, open previously created environment and press "Add policy" and specify parameters:<br>
            "When asked to execute:" .s3manage<br>
            "Override value of:" aws_access_key_id<br>
            "With:" your Amazon Access Key ID.
    - s3manage.aws_secret_access_key
        Go to "Environments" page, open previously created environment and press "Add policy" and specify parameters:<br>
            "When asked to execute:" .s3manage<br>
            "Override value of:" aws_secret_access_key<br>
            "With:" your Amazon Secret Access Key.
    
    ![add policy][add_policy]
Now you are ready to launch “Behavior Analytic Starter Kit” applications.

[aws_security_group_doc][http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-network-security.html#adding-security-group-rule]

[add_cloud_acccount]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/add_cloud_acccount.png
[setup_cloud_account]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/setup_cloud_account.png

[add_service]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/add_service.png
[setup_service]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/setup_service.png
[edit_service]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/edit_service.png

[add_env]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/add_env.png
[setup_env]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/setup_env.png

[set_cloud_account]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/set_cloud_account.png

[add_policy]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/add_policy.png

<!-- [env_page]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/Qubell%20Guide/env_page.png
![env page][env_page]
 -->

