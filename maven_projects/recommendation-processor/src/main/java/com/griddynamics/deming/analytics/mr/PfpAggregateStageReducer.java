package com.griddynamics.deming.analytics.mr;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.griddynamics.deming.analytics.core.AnalyticsCounters;
import com.griddynamics.deming.analytics.core.TopPatternsWithKeySupport;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class PfpAggregateStageReducer extends Reducer<Text, TopPatternsWithKeySupport, Text, Text> {

    private static final Joiner commaJoiner = Joiner.on(',');

    private final Text VALUE = new Text();

    private int maxHeapSize;
    private double minConfidence;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);

        Configuration conf = context.getConfiguration();
        Parameters params = new Parameters(conf.get("pfp.parameters", ""));
        maxHeapSize = Integer.valueOf(params.get("maxHeapSize", "50"));
        minConfidence = Double.valueOf(params.get("minConfidence", "0"));
    }

    @Override
    protected void reduce(Text key, Iterable<TopPatternsWithKeySupport> values, Context context)
            throws IOException, InterruptedException {

        TopPatternsWithKeySupport topPatternsWithKeySupport = new TopPatternsWithKeySupport();

        for (TopPatternsWithKeySupport value : values) {
            context.setStatus("Reducer of aggregate stage: Selecting TopK patterns for: " + key);
            topPatternsWithKeySupport = topPatternsWithKeySupport.merge(value, maxHeapSize);
        }

        Set<String> items = Sets.newLinkedHashSet();

        for (Pair<List<String>, Long> pattern : topPatternsWithKeySupport.getPatterns()) {

            long patternSupport = pattern.getSecond();
            long keySupport = topPatternsWithKeySupport.getKeySupport();
            double confidence = (double) patternSupport / keySupport;

            if (confidence < minConfidence) {
                context.getCounter(AnalyticsCounters.SKIPPED_BY_CONFIDENCE).increment(1);
                continue; // skip association rule with little value of confidence
            }

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
