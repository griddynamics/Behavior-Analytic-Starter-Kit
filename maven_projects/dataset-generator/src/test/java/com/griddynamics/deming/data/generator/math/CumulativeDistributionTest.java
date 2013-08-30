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

import com.google.common.collect.Lists;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
@RunWith(Theories.class)
public class CumulativeDistributionTest {

    private List<Double> probabilities = Lists.newArrayList(0.2, 0.5, 0.3);

    @DataPoints
    public static Object[][] data() {
        return new Object[][] {
                {0.0, 0},
                {0.1, 0},
                {0.19, 0},
                {0.2, 0},
                {0.21, 1},
                {0.3, 1},
                {0.4, 1},
                {0.5, 1},
                {0.6, 1},
                {0.69, 1},
                {0.7, 1},
                {0.71, 2},
                {0.8, 2},
                {0.9, 2},
                {0.99, 2},
                {1.0, 2},
                {1.1, 2},
                {10.0, 2},
        };
    }

    @Theory
    public void test(final Object... testData) {
        // Given
        double randomValue = (Double) testData[0];
        int expectedIndex = (Integer) testData[1];

        CumulativeDistribution distribution = new CumulativeDistribution(probabilities);

        // When
        int actualIndex = distribution.getIndexByRandomValue(randomValue);

        // Then
        assertThat(actualIndex, is(expectedIndex));
    }
}
