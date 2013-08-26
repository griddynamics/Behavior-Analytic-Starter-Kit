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

        Option inputOption = new Option("i", "input", true, "Input HDFS directory with transactions");
        inputOption.setRequired(true);
        inputOption.setArgName("hdfs path");

        Option outputOption = new Option("o", "output", true,
                "Not existing output HDFS directory where will be store result");
        outputOption.setRequired(true);
        outputOption.setArgName("hdfs path");

        Option minSupportOption = new Option("s", "min-support", true,
                "(Optional) The minimum number of times a co-occurrence must be present. " +
                        "Default Value: " + minSupport);
        minSupportOption.setArgName("support");

        Option groupsOption = new Option("g", "groups", true,
                "(Optional) Number of groups the features should be divided in the map-reduce version. " +
                        "Default Value: " + numGroups);
        groupsOption.setArgName("num of groups");

        Options options = new Options()
                .addOption(inputOption)
                .addOption(outputOption)
                .addOption(minSupportOption)
                .addOption(groupsOption);

        CommandLineParser commandLineParser = new GnuParser();

        try {
            CommandLine commandLine = commandLineParser.parse(options, commandLineArguments);

            if (commandLine.hasOption("i")) {
                input = commandLine.getOptionValue("i").trim();
            } else if (commandLine.hasOption("input")) {
                input = commandLine.getOptionValue("input").trim();
            } else {
                throw new IllegalArgumentException("Input directory is required!");
            }

            if (commandLine.hasOption("o")) {
                output = commandLine.getOptionValue("o").trim();
            } else if (commandLine.hasOption("output")) {
                output = commandLine.getOptionValue("output").trim();
            } else {
                throw new IllegalArgumentException("Output directory is required!");
            }

            if (commandLine.hasOption("s")) {
                minSupport = Integer.parseInt(commandLine.getOptionValue("s").trim());
            } else if (commandLine.hasOption("min-support")) {
                minSupport = Integer.parseInt(commandLine.getOptionValue("min-support").trim());
            } else {
                System.out.println("Using default minSupport: " + minSupport);
            }

            if (commandLine.hasOption("g")) {
                numGroups = Integer.parseInt(commandLine.getOptionValue("g").trim());
            } else if (commandLine.hasOption("groups")) {
                numGroups = Integer.parseInt(commandLine.getOptionValue("groups").trim());
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
        helpFormatter.printHelp("recommendation-processor.jar", options);
    }

    public static void main(String[] args) throws Exception {
        int status = ToolRunner.run(new AnalyticsRunner(), args);
        System.exit(status);
    }
}
