Starter kit components overview
-------------------------------

1. Behavior analytics platform

* Transaction Log generator - generates transaction log using probabilistic customer behavior scenario.
* Hadoop cluster - provides infrastructure to launch transaction log generating and recommendation processor jobs.
* Ganglia monitoring for Hadoop cluster - collects wide range of Hadoop cluster related metrics.
* Recommendation processor - implements improved version of PFP-growth algorithm and extracts associative rules out of transaction log and present them as recommendations.

2. Web store

* Product catalog - contains information about products and its categories in json format and products images.
* Web store web UI - application where you can add or remove products to/from your shopping cart and immediate view relevant recommendations.
