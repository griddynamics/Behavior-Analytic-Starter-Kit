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

package com.griddynamics.deming.analytics.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;

import java.io.IOException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class PfpAggregateStageCombiner extends Reducer<Text, TopKStringPatterns, Text, TopKStringPatterns> {

    private int maxHeapSize;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);

        Configuration conf = context.getConfiguration();
        Parameters params = new Parameters(conf.get("pfp.parameters", ""));
        maxHeapSize = Integer.valueOf(params.get("maxHeapSize", "50"));
    }

    @Override
    protected void reduce(Text key, Iterable<TopKStringPatterns> values, Context context)
            throws IOException, InterruptedException {

        TopKStringPatterns topKPatterns = new TopKStringPatterns();

        for (TopKStringPatterns value : values) {
            context.setStatus("Reducer of aggregate stage: Selecting TopK patterns for: " + key);
            topKPatterns = topKPatterns.merge(value, maxHeapSize);
        }

        context.write(key, topKPatterns);
    }
}
