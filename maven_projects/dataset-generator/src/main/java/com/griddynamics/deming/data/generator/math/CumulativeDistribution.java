package com.griddynamics.deming.data.generator.math;

import com.google.common.primitives.Doubles;

import java.util.Arrays;
import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class CumulativeDistribution {

    private final double[] probabilities;
    private final double[] cumulativeProbability;

    public CumulativeDistribution(List<Double> probabilityList) {
        probabilities = Doubles.toArray(probabilityList);

        // Normalize probabilities
        double probabilitySum = 0;

        for (double probability : probabilities) {
            probabilitySum += probability;
        }

        for (int i = 0; i < probabilities.length; ++i) {
            probabilities[i] = probabilities[i] / probabilitySum;
        }

        // Calculate cumulative probability
        cumulativeProbability = new double[probabilities.length];

        double previous = 0;

        for (int i = 0; i < probabilities.length; ++i) {
            cumulativeProbability[i] = previous + probabilities[i];
            previous = cumulativeProbability[i];
        }
    }

    public int getIndexByRandomValue(double randomValue) {
        int index = Arrays.binarySearch(cumulativeProbability, randomValue);

        if (index < 0) {
            index =  -index -1;
        }

        return index < probabilities.length ?  index : probabilities.length -1;
    }
}
