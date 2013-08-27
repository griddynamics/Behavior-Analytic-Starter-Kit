package com.griddynamics.deming.analytics.mr;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.griddynamics.deming.analytics.core.MathUtil;
import com.griddynamics.deming.analytics.core.TopPatternsWithKeySupport;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.common.Pair;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;

import java.io.IOException;
import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class PfpAggregateStageMapper extends Mapper<Text, TopKStringPatterns, Text, TopPatternsWithKeySupport> {

    private static final Joiner commaJoiner = Joiner.on(',');

    private final Text KEY = new Text();

    @Override
    protected void map(Text key, TopKStringPatterns values, Context context) throws IOException, InterruptedException {
        for (Pair<List<String>, Long> pattern : values.getPatterns()) {
            List<String> items = pattern.getFirst();

            for (int k = 1; k < items.size(); ++k) {
                context.setStatus("Mapper of aggregate stage: Process combination from " + items.size() + " by " + k);

                boolean[] combination = MathUtil.createInitialCombination(items.size(), k);

                do {
                    List<String> keyItems = Lists.newArrayList();
                    List<String> valueItems = Lists.newArrayList();

                    for (int i = 0; i < items.size(); ++i) {
                        if (combination[i]) {
                            keyItems.add(items.get(i));
                        } else {
                            valueItems.add(items.get(i));
                        }
                    }

                    KEY.set(commaJoiner.join(keyItems));

                    List<Pair<List<String>, Long>> patternSingularList = Lists.newArrayList();
                    patternSingularList.add(Pair.of(valueItems, pattern.getSecond()));

                    context.write(KEY, new TopPatternsWithKeySupport(pattern.getSecond(), patternSingularList));

                } while (MathUtil.nextCombination(combination));
            }
        }
    }
}
