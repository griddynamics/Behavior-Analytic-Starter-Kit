Web Store Overview
==================

Web Store is simple shopping cart application based on [demo web application](https://github.com/BroadleafCommerce/DemoSite)
of [Broadleaf Commerce framework](http://www.broadleafcommerce.org).
In this Web Store we have added a simple recommendation engine that shows recommendations to cart content. 

To use the recommendation engine you should upload recommendations to Web Store via REST API. 
To do this, you need to:

1. save **_product catalog information for transaction log generator_** to s3
2. deploy **_Behavior analytics platform_** using **_qubell platform_** (if you haven't already done so)
3. launch **_transaction log generator_**
4. launch **_recommendation processor_** that will load **_recommendations_** to s3
5. load **_recommendations_** from s3 to Web Store

Also you can upload custom product catalog using REST API.

    Note: You need to calculate new recommendations after uploading custom product catalog!

More detailed information on each component of Web Store can be found in the following sections:
* [Custom Product Catalog](Developer-Guide--Web-Store--Product-Catalog.md)
* [Recommendation engine](Developer-Guide--Web-Store--Recommendation-Engine.md)
* [Recommendations](Developer-Guide--Web-Store--Recommendations.md)
* [REST API](Developer-Guide--Web-Store--REST-API.md)


How to extend a Web Store
-------------------------

You can extend the Web Store application by change source code
in [sample-store module](../maven_projects/sample-store).

More information about extending of Web Store you can see in
[Broadleaf documentation](http://docs.broadleafcommerce.org/core/current).
