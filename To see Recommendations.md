To see the Recommendations 
==========================

Load product catalog from S3 to Web Store
-----------------------------------------
The first step is to go to the Web Store application and replace the current product catalog in the Web Store with new a new product catalog from S3. To do so, press the "Load_product_catalog_S3" button. You may keep the default settings or change the recommendations filename as you wish. 

![load product catalog](/Images/load product catalog.png)


Put product catalog information to S3
-------------------------------------
Once you have successfully loaded the product catalog from S3 to Web Store, press the "Save_product_catalog_structure_to_S3" button in Web Store to put product catalog information from Web Store to your Amazon S3 bucket. You may keep the default settings or change the S3 product catalog structure filename as you wish. Now product catalog information will be uploaded to your private bucket on Amazon S3. 

![save catalog](Images/save product catalog.png)


Generate sample transaction log
-------------------------------
To generate a sample transaction log, you must first go to the Recommendation Engine application and click on the "Load_transaction_log_from_S3" button to load the transaction log. 

![Load transaction log](/Images/load transaction log.png)

After the application has finished successfully launching that job, next click on the "Generate_new_transaction_log" to generate a sample transaction log. As a result, a transaction log generator job (LINK describing transaction log generator job) will be launched. You may keep the default settings or change them (URLs to transaction log generator job and configuration file, and your Amazon S3 bucket and filename), as you wish.

![Get new transaction log](/Images/generate transaction.png)


Compute New Recommendation
-----------------------------
To computer new recoomendations, press the "Compute_new_recommendations" button from the Recommendation Engine application. This will launch the recommendation processor (LINK explaining recommendation processor) on the transaction log to get recommendations. 

![Compute new recommendations](/Images/compute new.png)

You may keep the default settings or change them as you wish. Further description on the parameters are explained here: (LINK)

![Parameters](/Images/minSupport)






