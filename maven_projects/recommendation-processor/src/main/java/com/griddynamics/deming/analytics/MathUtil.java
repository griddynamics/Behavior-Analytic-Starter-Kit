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

package com.griddynamics.deming.analytics;

import org.apache.commons.lang.Validate;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class MathUtil {

    public static boolean[] createInitialCombination(int n, int k) {
        Validate.isTrue(n > 0, "You should pass n: n > 0");
        Validate.isTrue(k >= 0 && k <= n, "You should pass k: 0 <= k <= n");

        boolean[] combination = new boolean[n];

        for (int i = 0; i < k; ++i) {
            combination[i] = true;
        }

        return combination;
    }

    public static boolean nextCombination(boolean[] combination) {
        int n = combination.length;
        int trues = 0;

        for (int i = n - 2; i >=0; --i) {
            if (combination[i + 1]) {
                ++trues;
            }

            if (combination[i] && !combination[i + 1]) {
                combination[i] = false;
                combination[i + 1] = true;

                if (trues != n - i - 2) {
                    for (int j = i + 2; j < n; ++j) {
                        if (trues > 0) {
                            combination[j] = true;
                            --trues;
                        } else {
                            combination[j] = false;
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }
}
