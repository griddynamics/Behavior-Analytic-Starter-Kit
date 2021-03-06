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

package com.griddynamics.deming.analytics;

import com.griddynamics.deming.analytics.mr.PfpAggregateStageCombiner;
import com.griddynamics.deming.analytics.mr.PfpAggregateStageMapper;
import com.griddynamics.deming.analytics.mr.PfpAggregateStageReducer;
import com.griddynamics.deming.hadoop.JobUtil;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.fpm.pfpgrowth.PFPGrowth;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;

import java.io.IOException;
import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class AnalyticsRunner extends Configured implements Tool {

    private static final Log LOG = LogFactory.getLog(AnalyticsRunner.class);

    private String input;
    private String output;
    private Integer numGroups = PFPGrowth.NUM_GROUPS_DEFAULT;
    private Integer minSupport = 3;

    @Override
    public int run(String[] args) throws Exception {

        if (!parseArguments(args)) {
            return -1;
        }

        Parameters params = new Parameters();
        params.set(PFPGrowth.INPUT, input);
        params.set(PFPGrowth.OUTPUT, output);
        params.set(PFPGrowth.MIN_SUPPORT, minSupport.toString());
        params.set(PFPGrowth.NUM_GROUPS, numGroups.toString());

        Configuration conf = getConf();
        conf.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization," +
                "org.apache.hadoop.io.serializer.WritableSerialization");

        LOG.info("Counting the frequencies of various features");
        PFPGrowth.startParallelCounting(params, conf);

        LOG.info("Saving feature list to distributed cache");
        List<Pair<String,Long>> fList = PFPGrowth.readFList(params);
        PFPGrowth.saveFList(fList, params, conf);

        LOG.info("Calculating group size");
        int maxPerGroup = fList.size() / numGroups + (fList.size() % numGroups != 0 ? 1 : 0);
        params.set(PFPGrowth.MAX_PER_GROUP, Integer.toString(maxPerGroup));

        LOG.info("Running PFP-Growth");
        PFPGrowth.startParallelFPGrowth(params, conf);

        LOG.info("Aggregating PFP-Growth results");
        startAggregating(params, conf);

//        LOG.info("Saving frequent pattern as map.");
//        saveFrequentPatternsAsMap(conf, params);

        return 0;
    }

    public void startAggregating(Parameters params, Configuration conf)
            throws IOException, InterruptedException, ClassNotFoundException {

        Path fpGrowthOutputPath = new Path(params.get(PFPGrowth.OUTPUT), PFPGrowth.FPGROWTH);

        Job job = new Job(conf, "PFP Aggregation stage");
        job.setJarByClass(this.getClass());

        // Input
        job.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job, fpGrowthOutputPath);

        Configuration configuration = job.getConfiguration();
        configuration.set(PFPGrowth.PFP_PARAMETERS, params.toString());
        configuration.set("mapred.compress.map.output", "true");
        configuration.set("mapred.output.compression.type", "BLOCK");

        // Mapper
        job.setMapperClass(PfpAggregateStageMapper.class);
        job.setCombinerClass(PfpAggregateStageCombiner.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TopKStringPatterns.class);

        // Reducer
        job.setReducerClass(PfpAggregateStageReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Output
        job.setOutputFormatClass(TextOutputFormat.class);

        Path outputPath = new Path(params.get(PFPGrowth.OUTPUT), PFPGrowth.FREQUENT_PATTERNS);
        FileOutputFormat.setOutputPath(job, outputPath);

        // Run job
        JobUtil.runJob(job);
    }

    private boolean parseArguments(String[] commandLineArguments) {
        Options options = new Options();
        options.addOption("i", "input", true, "Input HDFS directory with transactions");
        options.addOption("o", "output", true, "Not existing output HDFS directory where will be store result");
        options.addOption("s", "minSupport", true, "(Optional) The minimum number of times a co-occurrence must be present. Default Value: " + minSupport);
        options.addOption("g", "numGroups", true, "(Optional) Number of groups the features should be divided in the map-reduce version. Default Value: " + numGroups);

        CommandLineParser commandLineParser = new GnuParser();

        try {
            CommandLine commandLine = commandLineParser.parse(options, commandLineArguments);

            if (commandLine.hasOption("i")) {
                input = commandLine.getOptionValue("i").trim();
            } else {
                throw new IllegalArgumentException("Input directory is required!");
            }

            if (commandLine.hasOption("o")) {
                output = commandLine.getOptionValue("o").trim();
            } else {
                throw new IllegalArgumentException("Output directory is required!");
            }

            if (commandLine.hasOption("s")) {
                minSupport = Integer.parseInt(commandLine.getOptionValue("s").trim());
            } else {
                System.out.println("Using default minSupport: " + minSupport);
            }

            if (commandLine.hasOption("g")) {
                numGroups = Integer.parseInt(commandLine.getOptionValue("g").trim());
            } else {
                System.out.println("Using default numGroups: " + numGroups);
            }

            return true;

        } catch (ParseException e) {
            System.err.println("Encountered exception while parsing command line:\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument: " + e.getMessage());
        }

        printHelp(options);
        return false;
    }

    /**
     * Print help information
     *
     * @param options   Command-line options to be part of usage.
     */
    public void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("analytics.jar", options);
    }

    public static void main(String[] args) throws Exception {
        int status = ToolRunner.run(new AnalyticsRunner(), args);
        System.exit(status);
    }
}
