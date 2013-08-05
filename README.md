Behavior Analytic Starter Kit
=============================
Open source personalization engine for ecommerce powered by [Qubell](http://qubell.com/)

Why it's cool?
-------------
- Open source - you can adapt it for your current needs.
- Fast setup - don't waste time for development!
- Get product recommendations in 3 steps
- Don't want use real transaction log? We'll generate sample for you - just export your catalog.

Technologies
------------
![Broadleaf][Broadleaf_logo]![Hadoop][Hadoop_logo]![Mahout][Mahout_logo]

How it works?
-------------
1. Put your product catalog and transation log to Amazon S3
2. Run Recommendation Processor job
3. Get your recommendations

![work_diagram][work_diagram]

Components
----------
- Sample Store - we can show you results on our demo store application
- Behavior analytics platform - setup and run Hadoop cluster on your Amazon EC2 account, generate recommendations for you

How can I try demo?
-------------------
1. Setup your Qubell Account (url to instraction)
2. Run Hadoop and WebStore applications
3. Setup product catalog for WebStore
    - Just press "Get product calatog from S3" button
    - Press "Upload catalog to S3" button and select your S3 bucket
4. Generate sample transaction log
    - Press "Lauch_transation_log_generator" button and select S3 bucket, where you uploaded product catalog
5. Run recommendation processor
    - Press "Lauch_recommendation_processor" button and select S3 bucket for saving recommendations
6. Get recommendations
    - Press "Get_recommendations_from_S3" button and select bucket with saved recommendations
    

How can I modify?
-----------------
We are provideing to you all components of our engine:

1. **Manifests for Qubell**
    - [WebStore][webstore_manifest]
    - [Behavior analytics platform]
2. **Chef cookbooks and there dependenses**
    - [hadoop_manage] - Manage services, hdfs and mapreduce jobs
    - [cloudera_noha] - Install and configure CDH 4.2 in noHA mode
    - [s3manage] - Upload/Download Amazon S3 files
    - [webstore][webstore_cookbook] - Install and configure webstore demo application
    - [ganglia] - Install ganglia server and ganglia clients
3. **Transactions Log Generator**
    Transaction log generator is designed to generate transaction log using different strategies. Now it supported two generation strategy:
    - Random generation strategy take [random-config.json] that contains following parameters:
        <table>
            <tr>
                <th>Parameter</th>
                <th>Description</th>
                <th>Default</th>
            </tr>
            <tr>
                <td><tt>transactionCount</tt></td>
                <td>amount of transactions in target transaction log</td>
                <td>10000</td>
            </tr> 
            <tr>
                <td><tt>transactionLength</tt></td>
                <td>mean average number of products in each transaction</td>
                <td>10</td>
            </tr>
            <tr>
                <td><tt>patternCount</tt></td>
                <td>number of random patterns (product ids that will appear in transaction log frequently</td>
                <td>100</td>
            </tr>
            <tr>
                <td><tt>patternLength</tt></td>
                <td>mean number of products in randomly generated patterns</td>
                <td>4</td>
            </tr>
            <tr>
                <td><tt>confidence</tt></td>
                <td>now not supported</td>
                <td>0.7</td>
            </tr>
            <tr>
                <td><tt>variation</tt></td>
                <td>now not supported</td>
                <td>0.1</td>
            </tr>
        </table>
    - Scenario generation strategy take [scenario-config.json] that contains following parameters:
    <table>
            <tr>
                <th>Parameter</th>
                <th>Description</th>
                <th>Default</th>
            </tr>
            <tr>
                <td><tt>transactionCount</tt></td>
                <td>amount of transactions in target transaction log</td>
                <td>10000000</td>
            </tr> 
            <tr>
                <td><tt>scenarios</tt></td>
                <td>list of scenarios</td>
                <td></td>
            </tr>
    </table>

    You can add own generation strategy by change source code in [transaction-log-generator module]

4. **Recommendation processor.** Designed to produce recommendations (what items often bought together). Currently It find frequent patterns from transaction log using PFP-Growth algorithm from Apache Mahout library. We change aggregation stage of PFP-Growth algorithm to produce association rules as text file because output of PFP-Growth algorithm is binary file with frequent patterns.
Also we have created own job runner that can take hadoop parameters (e.g. -Dmapred.job.map.memory.mb=2048 and so on) and pfp parameters (minSupport, groups).

    You can change current recommendations generation algorithm by change source code in [recommendation-processor module].
5. **WebStore sample application.**

    
[broadleaf_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/broadleaf_logo.png "Broadleaf Framework"
[Hadoop_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/hadoop_logo.jpg "Apache Hadoop"
[Mahout_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/mahout_logo.png "Apache Mahout"

[work_diagram]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/how_it_work.png "That how it works"

[webstore_manifest]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/manifests/webstore.yaml
[Behavior analytics platform]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/manifests/behavior_analytics_platform.yaml

[hadoop_manage]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/cookbooks/hadoop_manage/README.md
[cloudera_noha]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/cookbooks/cloudera_noha/README.md
[s3manage]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/cookbooks/s3manage/README.md
[webstore_cookbook]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/cookbooks/webstore/README.md
[ganglia]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/cookbooks/ganglia/README.md

[random-config.json]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/maven_projects/dataset-generator/src/main/resources/random-config.json
[transaction-log-generator module]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/tree/master/maven_projects/dataset-generator
[recommendation-processor module]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/tree/master/maven_projects/recommendation-processor
[scenario-config.json]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/maven_projects/dataset-generator/src/main/resources/scenario-config.json

[product_catalog_source]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/maven_projects/dataset-generator/src/main/resources/product-catalog.json

