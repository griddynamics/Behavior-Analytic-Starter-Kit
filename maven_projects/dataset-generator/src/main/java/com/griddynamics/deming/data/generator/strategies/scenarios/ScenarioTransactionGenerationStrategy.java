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

package com.griddynamics.deming.data.generator.strategies.scenarios;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.griddynamics.deming.data.generator.common.Category;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategy;
import com.griddynamics.deming.data.generator.common.Product;
import com.griddynamics.deming.data.generator.math.CumulativeDistribution;
import com.griddynamics.deming.data.generator.math.GaussianProductIdsGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.*;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class ScenarioTransactionGenerationStrategy implements GenerationStrategy {

    private final Random random = new MersenneTwisterRNG();

    private final Map<String, GaussianProductIdsGenerator> productCatalog = Maps.newHashMap();

    private ScenarioConfig config;
    private CumulativeDistribution scenariosDistribution;

    public ScenarioTransactionGenerationStrategy(ScenarioConfig config, Category catalog) {
        this.config = config;

        List<Double> probabilities = Lists.transform(config.scenarios, new Function<Scenario, Double>() {
            @Override
            public Double apply(Scenario scenario) {
                return scenario.probability;
            }
        });

        scenariosDistribution = new CumulativeDistribution(probabilities);

        populateProductCatalog(catalog, "");
    }

    public List<Long> generateTransaction() {
        Set<Long> transaction = Sets.newHashSet();

        int scenarioNumber = scenariosDistribution.getIndexByRandomValue(random.nextDouble());
        Scenario scenario = config.scenarios.get(scenarioNumber);

        for (Scenario.BehaviorItem behaviorItem: scenario.behavior) {
            if (random.nextDouble() < behaviorItem.probability) {
                String categoryName = behaviorItem.productCategory.toLowerCase();

                if (!productCatalog.containsKey(categoryName)) {
                    throw new IllegalStateException(
                            String.format("Category name '%s' not found in product catalog.", categoryName));
                }

                GaussianProductIdsGenerator productIdsGenerator = productCatalog.get(categoryName);

                transaction.add(productIdsGenerator.getNextProductId());
            }
        }

        List<Long> result = Lists.newArrayList(transaction);
        Collections.shuffle(result, random);

        return result;
    }

    private void populateProductCatalog(Category category, String fullCategoryName) {
        if (CollectionUtils.isNotEmpty(category.subcategories)) {
            for (Category subcategory : category.subcategories) {
                String categoryName = subcategory.name.toLowerCase();

                if (StringUtils.isNotEmpty(fullCategoryName)) {
                    categoryName = fullCategoryName + "->" + categoryName;
                }

                populateProductCatalog(subcategory, categoryName);
            }
        }

        if (CollectionUtils.isNotEmpty(category.products)) {
            List<Long> productIds = Lists.transform(category.products, new Function<Product, Long>() {
                @Override
                public Long apply(Product product) {
                    return product.id;
                }
            });

            productCatalog.put(fullCategoryName, new GaussianProductIdsGenerator(productIds, random));
        }
    }
}
