Generate new transaction log
-------------------------------

Press “Generate new transaction log” button on behavior analytics platform application output panel to generate synthetic transaction log. 
In fact [transaction log generator job](Developer-Guide--Behavior-Analytics-Platform--Transaction-Log-Generator.md) will be launched. You can configure transaction log generator default parameters before job start. 

![launch transaction log gen][launch_transaction_log_gen]

Following parameters can be configured: 
* URLs to transaction log generator job and transaction log generator scenario configuration file (JSON). 
* Filename for product catalog structure on S3 that would be used to generate transaction log.

![launch transaction log gen dialog][launch_transaction_log_gen_dialog]


[launch_transaction_log_gen]: images/Developer%20Guide/launch_transaction_log_gen.png
[launch_transaction_log_gen_dialog]: images/Developer%20Guide/launch_transaction_log_gen_dialog.png
