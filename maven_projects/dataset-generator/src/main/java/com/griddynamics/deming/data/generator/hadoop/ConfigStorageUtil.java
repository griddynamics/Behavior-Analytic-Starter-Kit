package com.griddynamics.deming.data.generator.hadoop;

import com.griddynamics.deming.data.generator.common.Category;
import com.griddynamics.deming.data.generator.hadoop.mr.TransactionsGenerationInputFormat;
import com.griddynamics.deming.data.generator.strategies.GeneratorConfig;
import com.griddynamics.deming.data.generator.serialization.JsonUtil;
import com.griddynamics.deming.hadoop.CacheUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class ConfigStorageUtil {

    public static final String CACHED_CONFIG_FILE_NAME = "configFile";
    public static final String CACHED_CATALOG_FILE_NAME = "catalogFile";

    public static Path addConfigToDistributedCache(Job job, String localPath) throws IOException {
        return addToDistributedCache(job, localPath, CACHED_CONFIG_FILE_NAME,
                TransactionsGenerationInputFormat.GENERATOR_SETTINGS_PATH);
    }

    public static Path addCatalogToDistributedCache(Job job, String localPath) throws IOException {
        return addToDistributedCache(job, localPath, CACHED_CATALOG_FILE_NAME, null);
    }

    public static Path addToDistributedCache(Job job, String localPath, String symlink, String configKey)
            throws IOException {

        String baseName = FilenameUtils.getBaseName(localPath);
        String tempCatalogFileName = String.format("_" + baseName + "_" + System.currentTimeMillis());
        Path path = new Path(job.getWorkingDirectory(), tempCatalogFileName);

        CacheUtil.addLocalFileToCache(localPath, path, job.getConfiguration(), symlink);

        if (StringUtils.isNotEmpty(configKey)) {
            job.getConfiguration().set(configKey, path.toString());
        }

        return path;
    }

    public static GeneratorConfig readConfigFromCache(Configuration conf) {
        return readFromPath(GeneratorConfig.class, conf, CacheUtil.getLocalPath(CACHED_CONFIG_FILE_NAME));
    }

    public static Category readCatalogFromCache(Configuration conf) {
        return readFromPath(Category.class, conf, CacheUtil.getLocalPath(CACHED_CATALOG_FILE_NAME));
    }

    public static <T> T readFromPath(Class<T> tClass, Configuration conf, String filePath) {
        try {
            Path path = new Path(filePath);
            FileSystem fileSystem = path.getFileSystem(conf);
            FSDataInputStream fsDataInputStream = fileSystem.open(path);
            try {
                return JsonUtil.read(tClass, fsDataInputStream);
            } finally {
                IOUtils.closeStream(fsDataInputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to read " + tClass.getName() + " from file: " + filePath, e);
        }
    }
}
