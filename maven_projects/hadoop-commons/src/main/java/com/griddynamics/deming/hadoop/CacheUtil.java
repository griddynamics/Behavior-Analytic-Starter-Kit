package com.griddynamics.deming.hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class CacheUtil {

    private static final Log LOG = LogFactory.getLog(CacheUtil.class);

    private static final String SYMLINK_DELIMITER = "#";

    public static void addCacheFile(Configuration configuration, String file) throws IOException {
        addCacheFile(configuration, new Path(file));
    }

    public static void addCacheFile(Configuration configuration, Path path) throws IOException {
        FileSystem fileSystem = path.getFileSystem(configuration);
        path.makeQualified(fileSystem);

        URI cacheUri;
        try {
            cacheUri = new URI(path.toString());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        DistributedCache.addCacheFile(cacheUri, configuration);

        LOG.info(cacheUri + " was added to distributed cache.");
    }

    public static void addCacheFile(Configuration configuration, String file, String symlink) throws IOException {
        addCacheFile(configuration, file + SYMLINK_DELIMITER + symlink);
        DistributedCache.createSymlink(configuration);
    }

    public static void addCacheFile(Configuration configuration, Path path, String symlink) throws IOException {
        addCacheFile(configuration, path.toString(), symlink);
    }

    public static void addLocalFileToCache(String localFilePath, Path hdfsFilePath, Configuration conf, String symlink)
            throws IOException {

        FileSystem fileSystem = hdfsFilePath.getFileSystem(conf);
        FSDataOutputStream fsDataOutputStream = fileSystem.create(hdfsFilePath);

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(localFilePath));

            try {
                IOUtils.copy(inputStream, fsDataOutputStream);
            } finally {
                inputStream.close();
            }
        } finally {
            fsDataOutputStream.close();
        }

        CacheUtil.addCacheFile(conf, hdfsFilePath, symlink);
    }

    public static boolean exist(String symlink) {
        return new File(symlink).exists();
    }

    public static String getLocalPath(String symlink) {
        File file = new File(symlink);

        if (!file.exists()) {
            throw new IllegalStateException(String.format("Symlink '%s' not exists.", symlink));
        }

        return file.toURI().toString();
    }
}
