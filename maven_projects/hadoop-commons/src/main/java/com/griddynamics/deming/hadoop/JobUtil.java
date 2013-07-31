package com.griddynamics.deming.hadoop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class JobUtil {

    private static final Log LOG = LogFactory.getLog(JobUtil.class);

    public static void runJob(Job job) throws InterruptedException, IOException, ClassNotFoundException {

        long startTime = System.currentTimeMillis();
        boolean success = job.waitForCompletion(true);
        long endTime = System.currentTimeMillis();

        LOG.info(job.getJobName() + " took " + Long.toString((endTime - startTime) / 1000) + " seconds.");

        if (!success) {
            throw new MapReduceJobFailureException(job.getJobName() + " was failed.");
        }
    }
}
