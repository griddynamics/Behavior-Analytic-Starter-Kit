Behavior Analytics Starter Kit
==============================

Behavior analytics platform is based on Hadoop cluster (HDFS and Map/Reduce) with recommendation processor
based on [Apache Mahout](http://mahout.apache.org) and additional Transaction Log generator.
Hadoop cluster uses [Cloudera Hadoop Distribution CDH4](http://www.cloudera.com/content/cloudera/en/products/cdh.html).

Sample store is a java application based on [Broadleaf framework](http://www.broadleafcommerce.org).
It uses wide range of well known technologies such as [Spring](http://www.springsource.org),
[Hibernate](http://www.hibernate.org) and [Apache Solr](http://lucene.apache.org/solr).

This repository contains:

* manifests ( deployment manifests for behavior analytic platform and web store (sample store))
* cookbooks ( chef cookbooks )
* maven_projects  (dataset-generator, recommendation-processor, sample-store)
* configs  ( sample configs)

**_License:_** Apache License 2.0


Getting started
---------------

* [Get Behavior Analytics Starter Kit](#get-bask)
* [Get Recommendations to Web Store](#get-recommendations)

Documentation
-------------

* Behavior analytics starter kit overview
    - Starter Kit architecture diagram
    - Starter Kit components overview
* Starter kit components
    - Sample store
        - [Sample store overview](docs/Developer-Guide--Web-Store--Overview.md)
        - Broadleaf Commerce framework
        - [Product catalog](docs/Developer-Guide--Web-Store--Product-Catalog.md)
        - [Recommendation engine](docs/Developer-Guide--Web-Store--Recommendation-Engine.md)
        - [Recommendations](docs/Developer-Guide--Web-Store--Recommendations.md)
        - [REST API](docs/Developer-Guide--Web-Store--REST-API.md)
    - Behavior analytics platform
        - Behavior analytics platform overview
        - Hadoop cluster description
        - [Transaction log generator](docs/Developer-Guide--Behavior-Analytics-Platform--Transaction-Log-Generator.md)
        - [Recommendation processor](docs/Developer-Guide--Behavior-Analytics-Platform--Recommendation-Processor.md)
    - Ganglia monitoring
* Appendix
    - [File formats](docs/Developer-Guide--Appendix--File-Formats.md)
        - [Product catalog for web store](docs/Developer-Guide--Appendix--File-Formats.md#product-catalog-for-web-store)
        - [Product catalog information for transaction log generator](docs/Developer-Guide--Appendix--File-Formats.md#product-catalog-information-for-transaction-log-generator)
        - [Transaction log](docs/Developer-Guide--Appendix--File-Formats.md#transaction-log)
        - [Recommendation file](docs/Developer-Guide--Appendix--File-Formats.md#recommendations-file)
    - Main cookbooks
    - Glossary
