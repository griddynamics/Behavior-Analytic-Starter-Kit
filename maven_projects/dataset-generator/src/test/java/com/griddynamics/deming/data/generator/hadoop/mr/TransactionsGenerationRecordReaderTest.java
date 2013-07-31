package com.griddynamics.deming.data.generator.hadoop.mr;

import com.google.common.collect.Lists;
import com.griddynamics.deming.data.generator.common.Category;
import com.griddynamics.deming.data.generator.hadoop.ConfigStorageUtil;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategy;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategyFactory;
import com.griddynamics.deming.data.generator.strategies.GeneratorConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigStorageUtil.class, GenerationStrategyFactory.class})
public class TransactionsGenerationRecordReaderTest {

    private TransactionsGenerationRecordReader testee;

    @Mock
    private TaskAttemptContext context;

    @Mock
    private Configuration configuration;

    @Mock
    private GeneratorConfig config;

    @Mock
    private Category catalog;

    @Before
    public void before() throws IOException {
        testee = new TransactionsGenerationRecordReader();

        when(context.getConfiguration()).thenReturn(configuration);

        PowerMockito.mockStatic(ConfigStorageUtil.class);
        PowerMockito.mockStatic(GenerationStrategyFactory.class);

        PowerMockito.when(ConfigStorageUtil.readConfigFromCache(configuration)).thenReturn(config);
        PowerMockito.when(ConfigStorageUtil.readCatalogFromCache(configuration)).thenReturn(catalog);

        PowerMockito
                .when(GenerationStrategyFactory.createStrategy(config, catalog))
                .thenReturn(new FakeGenerationStrategy());
    }

    @After
    public void after() throws IOException {
        Mockito.verify(context, times(2)).getConfiguration();

        PowerMockito.verifyStatic();
        ConfigStorageUtil.readConfigFromCache(configuration);

        PowerMockito.verifyStatic();
        ConfigStorageUtil.readCatalogFromCache(configuration);

        PowerMockito.verifyStatic();
        GenerationStrategyFactory.createStrategy(config, catalog);
    }

    @Test
    public void test() throws IOException, InterruptedException {
        // Given
        TransactionsGenerationSplit inputSplit = new TransactionsGenerationSplit(0, 2);

        testee.initialize(inputSplit, context);

        // When
        boolean result = testee.nextKeyValue();

        // Then
        assertTrue(result);
        assertThat(testee.getCurrentKey().get(), is(1L));
        assertThat(testee.getCurrentValue().toString(), equalTo("1, 2, 3"));
        assertThat(testee.getProgress(), equalTo(0.5f));

        // When
        result = testee.nextKeyValue();

        // Then
        assertTrue(result);
        assertThat(testee.getCurrentKey().get(), is(2L));
        assertThat(testee.getCurrentValue().toString(), equalTo("4, 5, 6"));
        assertThat(testee.getProgress(), equalTo(1.0f));

        // When
        result = testee.nextKeyValue();

        // Then
        assertFalse(result);
    }

    private class FakeGenerationStrategy implements GenerationStrategy {

        private long currentValue;

        @Override
        public List<Long> generateTransaction() {
            List<Long> transaction = Lists.newArrayList();

            for (int i = 0; i < 3; ++i) {
                transaction.add(++currentValue);
            }

            return transaction;
        }
    }
}
