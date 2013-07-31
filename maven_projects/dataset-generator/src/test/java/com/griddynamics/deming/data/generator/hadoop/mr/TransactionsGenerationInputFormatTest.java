package com.griddynamics.deming.data.generator.hadoop.mr;

import com.google.common.collect.ImmutableList;
import com.griddynamics.deming.data.generator.hadoop.ConfigStorageUtil;
import com.griddynamics.deming.data.generator.strategies.GeneratorConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
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
import static org.mockito.Mockito.when;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransactionsGenerationInputFormat.class, ConfigStorageUtil.class, JobClient.class})
public class TransactionsGenerationInputFormatTest {

    private static final String SETTINGS_FILE_PATH = "settings_file_path.txt";

    private TransactionsGenerationInputFormat testee;

    private GeneratorConfig generatorConfig;

    private boolean shouldException;

    @Mock
    private JobContext jobContext;

    @Mock
    private Configuration configuration;

    @Mock
    private ClusterStatus clusterStatus;

    @Before
    public void before() throws Exception {
        Mockito.when(jobContext.getConfiguration()).thenReturn(configuration);

        Mockito.when(configuration.get(TransactionsGenerationInputFormat.GENERATOR_SETTINGS_PATH))
                .thenReturn(SETTINGS_FILE_PATH);

        JobClient jobClient = PowerMockito.mock(JobClient.class);
        PowerMockito.when(jobClient.getClusterStatus()).thenReturn(clusterStatus);

        PowerMockito.whenNew(JobClient.class)
                .withArguments(configuration)
                .thenReturn(jobClient);

        testee = new TransactionsGenerationInputFormat();
        generatorConfig = new GeneratorConfig();
        shouldException = false;

        PowerMockito.mockStatic(ConfigStorageUtil.class);

        PowerMockito.when(ConfigStorageUtil.readFromPath(GeneratorConfig.class, configuration, SETTINGS_FILE_PATH))
                .thenReturn(generatorConfig);
    }

    @After
    public void after() throws IOException {
        Mockito.verify(jobContext).getConfiguration();
        Mockito.verify(configuration).getInt(TransactionsGenerationInputFormat.MAPS_PER_HOST, 1);

        if (!shouldException) {
            Mockito.verify(configuration).get(TransactionsGenerationInputFormat.GENERATOR_SETTINGS_PATH);

            PowerMockito.verifyStatic();
            ConfigStorageUtil.readFromPath(GeneratorConfig.class, configuration, SETTINGS_FILE_PATH);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void getSplitsWith0MapsPerHost() throws IOException, InterruptedException {
        // Given
        when(configuration.getInt(TransactionsGenerationInputFormat.MAPS_PER_HOST, 1)).thenReturn(0);
        shouldException = true;

        // When
        testee.getSplits(jobContext);
    }

    @Test
    public void getSplitsWith10Transactions5TaskTrackersAnd1MapsPerHost() throws IOException, InterruptedException {
        // Given
        generatorConfig.transactionCount = 10;

        when(configuration.getInt(TransactionsGenerationInputFormat.MAPS_PER_HOST, 1)).thenReturn(1);
        when(clusterStatus.getTaskTrackers()).thenReturn(5);

        // When
        List<InputSplit> splits = testee.getSplits(jobContext);

        // Then
        assertThat(splits.size(), is(5));

        List<InputSplit> expectedSplits = ImmutableList.<InputSplit>of(
                new TransactionsGenerationSplit(0, 2),
                new TransactionsGenerationSplit(2, 2),
                new TransactionsGenerationSplit(4, 2),
                new TransactionsGenerationSplit(6, 2),
                new TransactionsGenerationSplit(8, 2)
        );

        assertThat(splits, equalTo(expectedSplits));

        Mockito.verify(configuration).getInt(TransactionsGenerationInputFormat.MAPS_PER_HOST, 1);
        Mockito.verify(clusterStatus).getTaskTrackers();
    }

    @Test
    public void getSplitsWith1000Transactions5TaskTrackersAnd1MapsPerHost() throws IOException, InterruptedException {
        // Given
        generatorConfig.transactionCount = 1000;

        when(configuration.getInt(TransactionsGenerationInputFormat.MAPS_PER_HOST, 1)).thenReturn(2);
        when(clusterStatus.getTaskTrackers()).thenReturn(3);

        // When
        List<InputSplit> splits = testee.getSplits(jobContext);

        // Then
        assertThat(splits.size(), is(6));

        List<InputSplit> expectedSplits = ImmutableList.<InputSplit>of(
                new TransactionsGenerationSplit(0, 167),
                new TransactionsGenerationSplit(167, 167),
                new TransactionsGenerationSplit(334, 167),
                new TransactionsGenerationSplit(501, 167),
                new TransactionsGenerationSplit(668, 167),
                new TransactionsGenerationSplit(835, 165)
        );

        assertThat(splits, equalTo(expectedSplits));

        Mockito.verify(configuration).getInt(TransactionsGenerationInputFormat.MAPS_PER_HOST, 1);
        Mockito.verify(clusterStatus).getTaskTrackers();
    }
}
