
#### Behavior Analytic Starter Kit
“Behavior Analytics Starter Kit" is the sample application, that implement common use case of personalisation/behavior analytics in eCommerce domain area: recommend what items usually purchased together with selected item(s). It is only subtask in wide recommendations related analytics area.   
To perform recommendations “Behavior Analytics Starter Kit" generate sample Transaction Log using custom Transaction Log generator, then proceed this Transaction Log using recommendation processor based on Apache Mahout and show results via Web UI (sample store mockup). “Behavior Analytics Starter Kit" uses custom Transaction Log generator because of suitable Transaction Logs aren’t public.

Behavior Analytics Start Kit consists of two main blocks:
* Behavior analytics platform
* Sample store (as web UI)

Behavior analytics platform is based on Hadoop cluster (HDFS and Map/Reduce) with recommendation processor based on Apache Mahout and additional Transaction Log generator. Hadoop cluster uses Cloudera Hadoop Distribution CDH4.
Sample store is a sample java sample store application based on Broadleaf framework. It uses wide range of well known technologies such as Spring, Hibernate and Apache Solr.

Behavior Analytics Start Kit uses Qubell for deployment and management purposes. You can learn more from detailed Qubell documentation qubell.com.
Amazon EC2 is used to launch VMs for Analytic application and sample store. Amazon S3 is used to store product catalog and recommendations. So you need to be an Amazon user to evaluate Behavior Analytics Start Kit.


“Behavior Analytics Starter Kit" provide following functionality:
* Launching a new sandbox instance of “Analytics Platform" (Hadoop cluster, Transaction Log generator, Recommendation processor).
* Launching a new sandbox instance of “sample store"
* Put product catalog from S3 to “sample store"
* Launching Transaction Log generation job.
* Launching recommendation processor job.
* Pull recommendations from S3 to the “sample store"
* View recommendations via sample store
* Destroying applications instances (“Analytics Platform" and “sample store") and cleaning resources when done.

“Behavior Analytics Starter Kit" key features:
* provide advanced recommendations for items groups.
* recommendations dynamically updates after each change of shopping cart.
* customisable scenario is used to generate Transaction Log.
* product catalog, Transaction Log generator scenario configurations, settings of recommendation processor could be changed.


### Content

* manifests ( deployment manifests for behavior analytic platform and web store (sample store))
* cookbooks ( chef cookbooks )
* maven_projects  (dataset-generator, recommendation-processor, sample-store)


