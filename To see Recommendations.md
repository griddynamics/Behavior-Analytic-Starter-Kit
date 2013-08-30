To see the Recommendations 
==========================

Load product catalog from S3 to Web Store
-----------------------------------------
The first step is to go to the Web Store application and replace the current product catalog in the Web Store with new a new product catalog from S3. To do so, press the "Load_product_catalog_S3" button. Keep the default settings or you may change the recommendations filename as you wish. 

![load product catalog](/Images/load product catalog.png)


Put product catalog information to S3
-------------------------------------
Once you have successfully loaded the product catalog from S3 to Web Store, press the "Save_product_catalog_structure_to_S3" button in Web Store to put product catalog information from Web Store to your Amazon S3 bucket. Keep the default settings or you may change the S3 product catalog structure filename as you wish. Now product catalog information will be uploaded to your private bucket on Amazon S3. 

![save catalog](Images/save product catalog.png)


Generate sample transaction log
-------------------------------
To generate a sample transaction log, you must first go to the Recommendation Engine application and click on the "Load_transaction_log_from_S3" button to load the transaction log. 

![Load transaction log](/Images/load transaction log.png)

After the application has finished successfully launching that job, next click on the "Generate_new_transaction_log" to generate a sample transaction log. As a result, a transaction log generator job (LINK describing transaction log generator job) will be launched. Keep the default settings or you may change them (URLs to transaction log generator job and configuration file, and your Amazon S3 bucket and filename), as you wish.

![Get new transaction log](/Images/generate transaction.png)


Compute New Recommendation
-----------------------------
To computer new recoomendations, press the "Compute_new_recommendations" button from the Recommendation Engine application. This will launch the recommendation processor (LINK explaining recommendation processor) on the transaction log to get recommendations. 

![Compute new recommendations](/Images/compute new.png)

Keep the default settings or you may change them as you wish. Further description on the parameters are explained here: (LINK)

![Parameters](/Images/minSupport.png)


Load Recommendations from S3
---------------------------
The final step to receiving the recommendations is to load the recommendations from S3 to the Web Store. To do so, go back to the Web Store applications and click the "Load_recommendations_from_S3" button. Keep the default settings, or if you have changed the filename previously, make sure it stays consistent. Congratulations, you can now see your starter kit in action!

![Load recommendations](/Images/load recommendations.png)


To View Your Recommendations
----------------------------
To view your recommendations, open up the Web Store URL link located in the Web Store application. 
![web store url link](/Images/url link.png)

Buy an item, click on the shopping cart icon in the top right corner, and you should be able to see your recommendations! 

![recommendations in action](/Images/initial recommendations.png)


**Next Chapter:** [Chapter 5- Summary and What's Next?](Chapter%205.md)


