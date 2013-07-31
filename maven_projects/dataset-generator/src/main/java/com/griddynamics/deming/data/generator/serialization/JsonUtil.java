package com.griddynamics.deming.data.generator.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class JsonUtil {

    public static <T> T read(Class<T> tClass, InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(inputStream, tClass);
    }
}
