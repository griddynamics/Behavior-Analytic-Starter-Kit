package com.griddynamics.deming.analytics.mr;

import com.griddynamics.deming.analytics.core.TopPatternsWithKeySupport;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.common.Parameters;

import java.io.IOException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class PfpAggregateStageCombiner extends Reducer<Text, TopPatternsWithKeySupport, Text, TopPatternsWithKeySupport> {

    private int maxHeapSize;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);

        Configuration conf = context.getConfiguration();
        Parameters params = new Parameters(conf.get("pfp.parameters", ""));
        maxHeapSize = Integer.valueOf(params.get("maxHeapSize", "50"));
    }

    @Override
    protected void reduce(Text key, Iterable<TopPatternsWithKeySupport> values, Context context)
            throws IOException, InterruptedException {

        TopPatternsWithKeySupport topPatternsWithKeySupport = new TopPatternsWithKeySupport();

        for (TopPatternsWithKeySupport value : values) {
            context.setStatus("Reducer of aggregate stage: Selecting TopK patterns for: " + key);
            topPatternsWithKeySupport = topPatternsWithKeySupport.merge(value, maxHeapSize);
        }

        context.write(key, topPatternsWithKeySupport);
    }
}
