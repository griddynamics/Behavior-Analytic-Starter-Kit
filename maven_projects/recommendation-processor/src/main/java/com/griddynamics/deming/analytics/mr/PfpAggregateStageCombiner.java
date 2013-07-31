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
