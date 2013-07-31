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
