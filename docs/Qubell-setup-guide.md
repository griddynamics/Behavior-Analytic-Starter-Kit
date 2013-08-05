Qubell setup guide
==================

1. Create Environment
    - Go to "Environments" page, press "Add Environment" button
    - Specify environment name and press "Create"
2. Create Cloud Account with Amazon secret and access keys. 
    - Go to environment, created before and press "Set a cloud account"
    - Specify AWS credentials and security group and press "Save"
3. Create Service Vault
    - Press "Add a service" and select a security value and press "Add"
4. Get, upload, regenerate keys
    - To get keys go to "Service" page and download private and public keys
    - To upload key press "Edit", specify service name and press "Upload"
    - To regenerate key press "Edit" and then "Regenarate" button
    - On yours environment page select key to use in Security Valut
5. Add Environment Policies     
    - s3manage.aws_access_key_id
        On your environment page press "Add policy" and specify parameters:<br>
            "When asked to execute:" .s3manage<br>
            "Override value of:" aws_access_key_id<br>
            "With:" your Amazon Access Key ID.<br>     
    - s3manage.aws_secret_access_key
        On your environment page press "Add policy" and specify parameters:<br>
            "When asked to execute:" .s3manage<br>
            "Override value of:" aws_secret_access_key<br>
            "With:" your Amazon Secret Access Key.<br>

------------- need to add one more scenario for not new user ( already has qubell env and cloud account)
Now you are ready to launch “Behavior Analytic Starter Kit” applications.
