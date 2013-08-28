Product catalog
---------------

We have added an ability to upload custom product catalog to Web Store of Behavior Analytics Starter Kit. Product catalog (.zip file) should be placed in your S3 bucket.
Default parameters refers to the initial Grid Dynamics bucket (read only) and default product catalog filename.
![load_product_catalog](images/Developer%20Guide/load_product_catalog.png)
![load_product_catalog_dialog](images/Developer%20Guide/load_product_catalog_dialog.png)
Format of product catalog for Web Store you can see 
[here](Developer-Guide--Appendix--File-Formats.md#product-catalog-for-web-store).

Also we have created [product catalog](https://s3.amazonaws.com/gd-bask/catalog.zip) based on 
[Magento (TM) product catalog](http://www.magentocommerce.com/knowledge-base/entry/installing-the-sample-data-for-magento)
that is distributed under [OSL 3.0 licence](http://opensource.org/licenses/OSL-3.0).

Product catalog structure contains only the information necessary to the trasaction log generator.

Format of product catalog structure for transaction log generator you can see 
[here](Developer-Guide--Appendix--File-Formats.md#product-catalog-information-for-transaction-log-generator).

Web Store can extract product catalog structure from product catalog.
So, you can import product catalog from Amazon S3 and then export product catalog structure 
for transaction log generator to Amazon S3.
