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
