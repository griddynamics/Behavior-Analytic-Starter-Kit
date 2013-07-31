package com.griddynamics.deming.data.generator.strategies;

import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public interface GenerationStrategy {

    /**
     * Generates transaction items
     *
     * @return  transaction items
     */
    List<Long> generateTransaction();
}
