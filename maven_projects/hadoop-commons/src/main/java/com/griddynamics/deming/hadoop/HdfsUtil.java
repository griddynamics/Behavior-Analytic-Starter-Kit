package com.griddynamics.deming.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class HdfsUtil {

    public static String readToString(Path path, Configuration conf) throws IOException {
        FileSystem fileSystem = path.getFileSystem(conf);

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileSystem.open(path)));

        StringBuilder resultBuilder = new StringBuilder();

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            resultBuilder.append(line).append('\n');
        }

        return resultBuilder.toString();
    }
}
