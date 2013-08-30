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

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class PfpAggregateStageReducer extends Reducer<Text, TopKStringPatterns, Text, Text> {

    private static final Joiner commaJoiner = Joiner.on(',');

    private final Text VALUE = new Text();

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

        Set<String> items = Sets.newLinkedHashSet();

        for (Pair<List<String>, Long> pattern : topKPatterns.getPatterns()) {
            List<String> patternItems = pattern.getFirst();

            if (items.size() + patternItems.size() <= maxHeapSize) {
                items.addAll(patternItems);
            } else {
                for (String item : patternItems) {
                    items.add(item);

                    if (items.size() >= maxHeapSize) {
                        break;
                    }
                }
            }
        }

        VALUE.set(commaJoiner.join(items));
        context.write(key, VALUE);
    }
}
