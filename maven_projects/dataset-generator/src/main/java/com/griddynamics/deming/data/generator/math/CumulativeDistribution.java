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
