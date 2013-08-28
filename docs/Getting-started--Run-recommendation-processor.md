Compute new recommendations
---------------------------

Press “Compute new recommendations” button on behavior analytics platform application output panel to launch 
[recommendation processor](Developer-Guide--Behavior-Analytics-Platform--Recommendation-Processor.md) on transaction log and get recommendations. 
You can configure recommendation processor default parameters before job start. 

![launch recommendation processor][launch_recommendation_processor]

Following parameters can be configured: 
* URL to recommendation processor job, 
* HDFS path to the directory contains transaction log, 
* Filename for recommendations output on your private S3, 
* Minimal support level for PFP algorithm, 
* Number of groups for parallel processing 
After job completed recommendations will be automatically put to your S3 bucket.

![launch recommendation proccessor dialog][launch_recommendation_proccessor_dialog]

[launch_recommendation_proccessor_dialog]: images/Developer%20Guide/launch_recommendation_proccessor_dialog.png
[launch_recommendation_processor]: images/Developer%20Guide/launch_recommendation_processor.png
