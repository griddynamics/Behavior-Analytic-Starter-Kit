Transaction log generator
=========================

Transaction log generator is designed to generate transaction log using different strategies.
Now it supported two generation strategy:

* Random
* Scenario

Each strategy represents by its own configuration file that describes customer behavior model.

Random generation strategy take [random-config.json](../maven_projects/dataset-generator/src/main/resources/random-config.json)
that contains following parameters:

* transactionCount - amount of transactions in target transaction log
* transactionLength - mean number of products in each transaction
* patternCount - number of random patterns (product ids that will appear in transaction log frequently)
* patternLength - mean number of products in randomly generated patterns
* confidence - now not supported
* variation - now not supported

Scenario generation strategy take [scenario-config.json](../maven_projects/dataset-generator/src/main/resources/scenario-config.json)
that contains following parameters:

* transactionCount - amount of transactions in target transaction log
* scenarios - list of scenarios

You can modify transaction log generator scenario configuration files and upload it on your S3.
You can add own generation strategy by change source code
in [dataset-generator module](../maven_projects/dataset-generator)
