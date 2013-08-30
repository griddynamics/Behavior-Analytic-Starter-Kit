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

import com.google.common.base.Joiner;
import com.griddynamics.deming.data.generator.common.Category;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategy;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategyFactory;
import com.griddynamics.deming.data.generator.strategies.GeneratorConfig;
import com.griddynamics.deming.data.generator.hadoop.ConfigStorageUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class TransactionsGenerationRecordReader extends RecordReader<LongWritable, Text> {

    private final Joiner COMMA_JOINER = Joiner.on(", ");

    private final LongWritable KEY = new LongWritable();
    private final Text VALUE = new Text();

    private GenerationStrategy generationStrategy;

    private TransactionsGenerationSplit transactionsGenerationSplit;
    private long currentTransactionNumber;
    private long stopTransactionNumber;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext context)
            throws IOException, InterruptedException {

        transactionsGenerationSplit = (TransactionsGenerationSplit) inputSplit;
        currentTransactionNumber = transactionsGenerationSplit.getStartPos();
        stopTransactionNumber = transactionsGenerationSplit.getStartPos() + transactionsGenerationSplit.getLength();

        GeneratorConfig config = ConfigStorageUtil.readConfigFromCache(context.getConfiguration());
        Category catalog = ConfigStorageUtil.readCatalogFromCache(context.getConfiguration());

        generationStrategy = GenerationStrategyFactory.createStrategy(config, catalog);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (currentTransactionNumber < stopTransactionNumber) {
            ++currentTransactionNumber;
            KEY.set(currentTransactionNumber);

            List<Long> transaction = generationStrategy.generateTransaction();
            VALUE.set(COMMA_JOINER.join(transaction));

            return true;
        }
        return false;
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return KEY;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return VALUE;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return (float) (currentTransactionNumber - transactionsGenerationSplit.getStartPos()) /
                transactionsGenerationSplit.getLength();
    }

    @Override
    public void close() throws IOException {
        // nothing
    }
}
