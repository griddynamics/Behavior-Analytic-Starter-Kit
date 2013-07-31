package com.griddynamics.deming.hadoop;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class MapReduceJobFailureException extends RuntimeException {

    public MapReduceJobFailureException(String message) {
        super(message);
    }
}
