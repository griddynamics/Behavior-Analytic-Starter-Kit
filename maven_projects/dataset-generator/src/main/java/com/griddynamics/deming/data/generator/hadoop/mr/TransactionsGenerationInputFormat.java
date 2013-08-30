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

package com.griddynamics.deming.data.generator.hadoop.mr;

import com.google.common.collect.Lists;
import com.griddynamics.deming.data.generator.strategies.GeneratorConfig;
import com.griddynamics.deming.data.generator.hadoop.ConfigStorageUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapreduce.*;

import java.io.IOException;
import java.util.List;

/**
 * This class inspired by org.apache.hadoop.examples.RandomWriter.RandomInputFormat
 *
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class TransactionsGenerationInputFormat extends InputFormat<LongWritable, Text> {

    public static final String MAPS_PER_HOST = "transactions.generator.mapsperhost";
    public static final String GENERATOR_SETTINGS_PATH = "transactions.generator.settings.path";

    @Override
    public List<InputSplit> getSplits(JobContext jobContext) throws IOException, InterruptedException {
        Configuration conf = jobContext.getConfiguration();

        int numMapsPerHost = conf.getInt(MAPS_PER_HOST, 1);

        if (numMapsPerHost < 1) {
            throw new IllegalStateException("MAPS_PER_HOST should be more or equal 1.");
        }

        JobClient client = new JobClient(conf);
        ClusterStatus cluster = client.getClusterStatus();
        int numSplits = cluster.getTaskTrackers() * numMapsPerHost;

        String settingsFilePath = conf.get(GENERATOR_SETTINGS_PATH);
        GeneratorConfig config = ConfigStorageUtil.readFromPath(GeneratorConfig.class, conf, settingsFilePath);

        long count = config.transactionCount;
        long block = (long) Math.ceil(count * 1.0 / numSplits);

        List<InputSplit> splits = Lists.newArrayList();

        for (int i = 0; i < numSplits; ++i) {
            long blockCount = count > block ? block : count;
            splits.add(new TransactionsGenerationSplit(i * block, blockCount));
            count -= block;
        }

        return splits;
    }

    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext context)
            throws IOException, InterruptedException {

        return new TransactionsGenerationRecordReader();
    }
}
