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

package com.griddynamics.deming.data.generator;

import com.google.common.collect.Lists;
import com.griddynamics.deming.data.generator.hadoop.ConfigStorageUtil;
import com.griddynamics.deming.data.generator.hadoop.mr.TransactionsGenerationInputFormat;
import com.griddynamics.deming.data.generator.hadoop.mr.TransactionsGenerationMapper;
import com.griddynamics.deming.hadoop.JobUtil;
import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class HadoopDataSetGeneratorRunner extends Configured implements Tool {

    private static final Logger LOGGER = LoggerFactory.getLogger(HadoopDataSetGeneratorRunner.class);

    public static final String GENERATOR_TITLE = "TransactionLogGenerator";

    private final List<Path> pathsToDelete = Lists.newArrayList();

    private String configFileName;
    private String catalogFileName;
    private String output;

    @Override
    public int run(String[] args) throws Exception {

        if (!parseArguments(args)) {
            return -1;
        }

        try {
            startGeneratingJob(getConf());
        } finally {
            for (Path path : pathsToDelete) {
                FileSystem fileSystem = path.getFileSystem(getConf());

                if (fileSystem.exists(path)) {
                    fileSystem.delete(path, true);
                }
            }
        }

        return 0;
    }

    private void startGeneratingJob(Configuration conf)
            throws IOException, ClassNotFoundException, InterruptedException {

        Job job = new Job(conf, GENERATOR_TITLE);
        job.setJarByClass(this.getClass());

        // Input
        job.setInputFormatClass(TransactionsGenerationInputFormat.class);

        Path configHdfsPath = ConfigStorageUtil.addConfigToDistributedCache(job, this.configFileName);
        Path catalogHdfsPath = ConfigStorageUtil.addCatalogToDistributedCache(job, this.catalogFileName);

        pathsToDelete.add(configHdfsPath);
        pathsToDelete.add(catalogHdfsPath);

        // Mapper
        job.setMapperClass(TransactionsGenerationMapper.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        // Reducer
        job.setNumReduceTasks(0);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        // Output
        FileOutputFormat.setOutputPath(job, new Path(output));

        // Run job
        JobUtil.runJob(job);
    }

    /**
     * Parses command line arguments and populates fields
     *
     * @param commandLineArguments Command-line arguments to be processed with Gnu-style parser
     * @return Success of parsing.
     */

    private boolean parseArguments(String[] commandLineArguments) {
        Options options = new Options();
        options.addOption("c", "conf", true, "Configuration file");
        options.addOption("p", "product-catalog", true, "Product catalog file");
        options.addOption("o", "output", true, "Output HDFS directory (not existing)");

        CommandLineParser commandLineParser = new GnuParser();

        try {
            CommandLine commandLine = commandLineParser.parse(options, commandLineArguments);

            if (commandLine.hasOption("c")) {
                configFileName = commandLine.getOptionValue("c").trim();
            } else {
                throw new IllegalArgumentException("Configuration file is required!");
            }

            if (commandLine.hasOption("p")) {
                catalogFileName = commandLine.getOptionValue("p").trim();
            } else {
                throw new IllegalArgumentException("Product catalog file is required!");
            }

            if (commandLine.hasOption("o")) {
                output = commandLine.getOptionValue("o").trim();
            } else {
                throw new IllegalArgumentException("Output directory is required!");
            }

            return true;

        } catch (ParseException e) {
            LOGGER.error("Encountered exception while parsing command line.", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal argument: {}", e.getMessage());
        }

        printHelp(options);
        return false;
    }

    /**
     * Prints help information
     *
     * @param options   Command-line options to be part of help.
     */
    public void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(GENERATOR_TITLE, options);
    }

    public static void main(String[] args) throws Exception {
        int status = ToolRunner.run(new HadoopDataSetGeneratorRunner(), args);
        System.exit(status);
    }
}
