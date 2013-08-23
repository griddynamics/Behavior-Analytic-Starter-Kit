Product catalog
---------------

We have added an ability to upload custom product catalog to Web Store of Behavior Analytics Starter Kit.
Format of product catalog for Web Store you can see 
[here](Developer-Guide--Appendix--File-Formats.md#product-catalog-for-web-store).

Also we have created [product catalog](https://s3.amazonaws.com/gd-bask/magento_catalog.zip) based on 
[Magento (TM) product catalog](http://www.magentocommerce.com/knowledge-base/entry/installing-the-sample-data-for-magento)
that is distributed under [OSL 3.0 licence](http://opensource.org/licenses/OSL-3.0).

Besides "product catalog" for Web Store there is "product catalog structure" for transaction log generator.
<<<<<<< HEAD
Product catalog structure contains only the information necessary to the trasaction log generator.
=======
Product catalog structure contains only the information necessary to the trasaction log generator.
>>>>>>> 9a17c0f465e593fff0d80c62e57443921ca786c3
Format of product catalog structure for transaction log generator you can see 
[here](Developer-Guide--Appendix--File-Formats.md#product-catalog-information-for-transaction-log-generator).

Web Store can extract product catalog structure from product catalog.
So, you can import product catalog from Amazon S3 and then export product catalog structure 
for transaction log generator to Amazon S3.
