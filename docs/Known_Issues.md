Known Issues
------------

### Qubell ###

1. We have two "Display name" fields on add application form: first one for [Behavior Analytic Platform](Developer-Guide--Behavior-Analytics-Platform--Overview.md), second for [Web Store](Developer-Guide--Web-Store--Overview.md)
1. HTML isn't working on add application form, so license link displays incorrect. [Correct license](/LICENSE) here.

### Deployment ###

1. Cookbook tarball now placing in our Amazon S3 bucket. It will switch to github tarball in next release.

### Web store ###

1. Web Store, Sample Store and ecask-site are different names of the same thing - [Web Store](Developer-Guide--Web-Store--Overview.md)
1. Checkout of shopping cart is not supported in Web store now, because ssl is not configured.
1. Product catalog has simple information without price and so on.
1. In web store recommendations are transient objects (aren't persistent in db).
1. In web store recommendations are stored as HashMap. Prefix tree is more optimal data structure for this purpose.

### Analytics platform ###

1. HDFS file browsing throw HTTP not working now 
1. Need to delete hdfs directory (using manage_hdfs_dirs button) before relaunching job from application (launch_transaction_log_generator, launch_recommendation_processor)
1. Config of transaction log generator supports hierarchy of categories only (tree, not graph)
