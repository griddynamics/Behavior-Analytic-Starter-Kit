Starter kit components overview
-------------------------------

1. **Web store**
    * _Web store web UI_ - web application where you can add or remove products to/from your shopping cart and immediate view relevant recommendations.
    * _Product catalog_ - contains information about products and its categories in json format and products images.
2. **Behavior analytics platform**
    * _Hadoop cluster_ - provides infrastructure to launch transaction log generating and recommendation processor jobs.
    * _Ganglia monitoring for Hadoop cluster_ - collects wide range of Hadoop cluster related metrics.
    * _Transaction Log generator_ - generates transaction log using probabilistic customer behavior scenario.
    * _Recommendation processor_ - implements improved version of PFP-growth algorithm and extracts associative rules out of transaction log and present them as recommendations.
