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

import org.uncommons.maths.random.GaussianGenerator;

import java.util.List;
import java.util.Random;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class GaussianProductIdsGenerator {

    private final GaussianGenerator gaussianGenerator;
    private final List<Long> productIds;

    public GaussianProductIdsGenerator(List<Long> productIds, Random random) {
        this.productIds = productIds;

        int halfSize = productIds.size() / 2;
        gaussianGenerator = new GaussianGenerator(halfSize, halfSize, random);
    }

    public Long getNextProductId() {
        int index = gaussianGenerator.nextValue().intValue();

        index = Math.max(0, index);
        index = Math.min(productIds.size() -1, index);

        return productIds.get(index);
    }
}
