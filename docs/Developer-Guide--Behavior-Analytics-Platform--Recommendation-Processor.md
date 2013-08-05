Recommendation processor
========================

Recommendation processor is designed to produce [recommendations](Developer-Guide--Web-Store--Recommendations.md) (what items often bought together).
Currently It find frequent patterns from transaction log using PFP-Growth algorithm from Apache Mahout library.
Mahout is a machine learning library. It contains the set of algorithms that can work on top of Hadoop MapReduce. 
We change aggregation stage of PFP-Growth algorithm to produce association rules as text file
because output of PFP-Growth algorithm is binary file with frequent patterns.
Also we have created own job runner that can take hadoop parameters
(e.g. -Dmapred.job.map.memory.mb=2048 and so on) and pfp parameters (minSupport, groups).

You can change current recommendations generation algorithm by change source code
in [recommendation-processor module](../maven_projects/recommendation-processor).

Following parameters should be provided to recommendation processor:
* URL to recommendation processor job

* HDFS path to transaction log,

* S3 bucket and filename for recommendations output on your private S3
 
* minSupport - minimal support level for PFP-growth algorithm. 
Minimal support N uses to select itemsets that appeаrs in transaction log more or equal than N times. 
You should use minimal support value depending on your transaction log. 
With low value PFP-Growth will produce more association rules (many of them may mean nothing). 
With big value PFP-Grow will produce smaller amount rules (but you may lost interesting rules).
You can experiment with minimal support value to get statistically significant recommendations. 
Minimal support value is 3 by default.

* groups - number of groups (number of independent groups to which the transaction log will be split. 
Separate FP-Growth will be launched for each group). 
The more data in transaction log, the greater the number of groups is necessary to specify to ensure if there is enough memory to handle a particular group on a separate machine.
Recommendation processor consists of three map/reduce jobs, which are executed sequentially. 
So, the result of running of recommendation processor is association rules in form of dictionary, 
where the key is selected item(s) and the value is set of items usually purchased together with selected item(s).
