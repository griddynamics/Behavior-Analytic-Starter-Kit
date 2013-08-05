Qubell setup guide
==================

1. [Sign up for an Express account](http://qubell.com/sign-up/) on Qubell Adaptive PaaS. It is free and takes only a few minutes.
2. Create Environment
    - Go to "Environments" page, press "Add an environment" button
    - Specify environment name and press "Add"
3. Create Cloud Account with Amazon secret and access keys.
    - Go to "Platform->Cloud Accounts" page and press "Add a cloud account" button
    - Specify account name, provider="Amazon EC2", AWS credentials and security group and press "Save"
    - Go to "Environments" page, open previously created environment and press "Set a cloud account"
    - Specify previously created account name and press "Save" 
4. Create Service Vault
    - Go to "Platform->Services" page and press "Add a service" button
    - Specify service name and type="Secure Vault 2.0" then press "Add"
    - Go to "Environments" page, open previously created environment and press "Add a service" button
    - Select previously created service name and press "Add"
5. Get, upload, regenerate keys
    - To get keys go to "Service" page and download private and public keys
    - To upload key press "Edit", specify service name and press "Upload"
    - To regenerate key press "Edit" and then "Regenarate" button
    - On yours environment page select key to use in Security Valut
6. Add Environment Policies     
    - s3manage.aws_access_key_id
        On your environment page press "Add policy" and specify parameters:<br>
            "When asked to execute:" .s3manage<br>
            "Override value of:" aws_access_key_id<br>
            "With:" your Amazon Access Key ID.
    - s3manage.aws_secret_access_key
        On your environment page press "Add policy" and specify parameters:<br>
            "When asked to execute:" .s3manage<br>
            "Override value of:" aws_secret_access_key<br>
            "With:" your Amazon Secret Access Key.

------------- need to add one more scenario for not new user ( already has qubell env and cloud account)
Now you are ready to launch “Behavior Analytic Starter Kit” applications.
