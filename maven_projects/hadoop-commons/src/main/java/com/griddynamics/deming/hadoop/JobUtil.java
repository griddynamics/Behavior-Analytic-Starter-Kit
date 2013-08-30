/*
* Copyright 2013 Grid Dynamics, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
