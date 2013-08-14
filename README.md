Behavior Analytic Starter Kit
=============================
Open source personalization engine for e-commerce powered by [Qubell][qubell_platform]

Why it's cool?
-------------
- Open source - you can adapt it for your current needs.
- Fast setup - don't waste time for development!
- Get product recommendations in 3 steps.
- Don't want use real transaction log? We'll generate sample for you - just export your catalog.

Technology stack
----------------
[![Qubell][qubell_logo]][qubell_site]
[![Opscode Chef][chef_logo]][chef_site]
[![Java programming language][java_logo]][java_site]
[![Broadleaf Commerce Framework][broadleaf_logo]][broadleaf_site]

[![Apache Hadoop][hadoop_logo]][hadoop_site]
[![Apache Mahout][mahout_logo]][mahout_site]

How it works?
-------------
1. Put your product catalog and transaction log to your private Amazon S3 bucket
2. Run Recommendation Processor job
3. Get your recommendations

![work_diagram][work_diagram]

Components
----------

* Web store is a java application based on [Broadleaf framework][broadleaf_site].
It uses wide range of well known technologies such as [Spring][spring_site],
[Hibernate][hibernate_site] and [Apache Solr][solr_site].
You can see your recommendations results in demo web store application.

* Behavior analytics platform is based on Hadoop cluster (HDFS and Map/Reduce) with recommendation processor
based on [Apache Mahout][mahout_site] and additional Transaction Log generator.
Hadoop cluster uses [Cloudera Hadoop Distribution (CDH4)][cdh_site].
Behavior analytics platform run Hadoop cluster on your Amazon EC2 account, generate transaction log and recommendations for you.

This repository contains:

* manifests ( deployment manifests for behavior analytic platform and web store (sample store))
* cookbooks ( chef cookbooks )
* maven_projects  (dataset-generator, recommendation-processor, sample-store)
* configs  ( sample configs )

Please read [developer documentation](Documentation.md) for more information

How can I try Starter Kit? (Getting started)
-------------------
1. [Setup your Qubell Account][qubell_setup]

2. Press "Get it now" button on [Starter Kit web site][qubell_site]. Log in or sign up if necessary.
Read short overview in new window, choose organization, edit default names for applications and
push “Add” button to load application manifests for Behavior Analytic Starter Kit to you Qubell Account.

3. [Launch WebStore and Behavior analytics platform applications](docs/Getting-started--Launch-applications.md)
4. [Load product catalog to Web Store](docs/Getting-started--Load-product-catalog.md)
5. [Put product catalog information to S3](docs/Getting-started--Put-product-catalog-information-to-s3.md)
6. [Generate sample transaction log](docs/Getting-started--Generate-sample-transaction-log.md)
7. [Launch recommendation processor](docs/Getting-started--Run-recommendation-processor.md)
8. [Get recommendations](docs/Getting-started--Get-recommendations-from-s3.md)
    

How can I modify?
-----------------


We are providing to you all components of our engine:

1. **Manifests for Qubell**
    - [WebStore][webstore_manifest]
    - [Behavior analytics platform]
2. **Chef cookbooks and their dependenses**
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
5. **WebStore sample application.** Simple shopping cart application based on demo web application (HeatClinic) of Broadleaf Commerce framework. In this web store we have added a simple recommendation engine that shows recommendations to cart content using recommendations from recommendation-processor application.
Also we have added following features to web store that available via [REST API](docs/Developer-Guide--Web-Store--REST-API.md):
    - Export product catalog information for transaction log generator
    - Import recommendations
    - Import product catalog (we have created own product catalog based on magento catalog)


[Known issues](docs/Known_Issues.md)
--------------

[qubell_platform]: http://qubell.com/product/qubell-platform/ "Qubell platform"

[qubell_site]: http://qubell.com "Qubell official site"
[chef_site]: http://www.opscode.com/chef "Opscode Chef official site"
[java_site]: http://www.oracle.com/technetwork/java/index.html "Java official site"
[broadleaf_site]: http://www.broadleafcommerce.org "Broadleaf commerce official site"
[hadoop_site]: http://hadoop.apache.org "Apache Hadoop official site"
[mahout_site]: http://mahout.apache.org "Apache Mahout official site"
[cdh_site]: http://www.cloudera.com/content/cloudera/en/products/cdh.html "Cloudera's Hadoop distribution official site"
[spring_site]: http://www.springsource.org "Spring framework official site"
[hibernate_site]: http://www.hibernate.org "Hibernate official site"
[solr_site]: http://lucene.apache.org/solr "Apache Solr official site"

[qubell_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/qubell_logo.png "Qubell platform"
[chef_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/chef_logo.png "Opscode Chef"
[java_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/java_logo.png "Java programming language"
[broadleaf_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/broadleaf_logo.png "Broadleaf Commerce Framework"
[hadoop_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/hadoop_logo.png "Apache Hadoop"
[mahout_logo]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/mahout_logo.png "Apache Mahout"

[work_diagram]: https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/how_it_work.png "That how it works"

[qubell_setup]: https://github.com/griddynamics/Behavior-Analytic-Starter-Kit/blob/master/docs/Qubell-setup-guide.md

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

