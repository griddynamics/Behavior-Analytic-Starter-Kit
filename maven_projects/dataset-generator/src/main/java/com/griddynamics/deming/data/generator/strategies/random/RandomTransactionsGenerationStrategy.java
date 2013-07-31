package com.griddynamics.deming.data.generator.strategies.random;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategy;
import com.griddynamics.deming.data.generator.common.Product;
import com.griddynamics.deming.data.generator.strategies.Utils;
import com.griddynamics.deming.data.generator.common.Category;
import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.PoissonGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Max Morozov
 */
public class RandomTransactionsGenerationStrategy implements GenerationStrategy {

    private final Random random = new MersenneTwisterRNG();
    private final List<Pattern<Long>> patterns = Lists.newArrayList();

    private RandomConfig config;
    private Category catalog;

    public RandomTransactionsGenerationStrategy(RandomConfig config, Category catalog) {
        this.config = config;
        this.catalog = catalog;

        generatePatterns();
    }

    @Override
    public List<Long> generateTransaction() {
        Set<Long> transaction = Sets.newHashSet();

        while (transaction.size() < config.transactionLength) {
            int patternNumber = random.nextInt(patterns.size());
            Pattern<Long> pattern = patterns.get(patternNumber);

            List<Long> items = Lists.newArrayList(pattern.getItems());

            // Find noise
            items.addAll(findProductIds(catalog, random.nextInt(3) + 1));

            int remaining = config.transactionLength - transaction.size();

            if (items.size() > remaining) {
                for (int pos = 0; pos < items.size() && remaining > 0; ++pos) {
                    if (transaction.add(items.get(pos))) {
                        --remaining;
                    }
                }
            } else
                transaction.addAll(items);
        }

        List<Long> result = Lists.newArrayList(transaction);
        Collections.shuffle(result, random);

        return result;
    }

    private void generatePatterns() {
        PoissonGenerator lengthGenerator = new PoissonGenerator(config.patternLength - 1, random);
        ExponentialGenerator frequencyGenerator = new ExponentialGenerator(1, random);
        GaussianGenerator confidenceGenerator = new GaussianGenerator(config.confidence, config.variation, random);

        for (int i = 0; i < config.patternCount; ++i) {
            int patternLength = lengthGenerator.nextValue() + 1;

            List<Long> productIds = findProductIds(catalog, patternLength);
            double probability = frequencyGenerator.nextValue();
            double confidence = confidenceGenerator.nextValue();

            patterns.add(new Pattern<Long>(productIds, probability, confidence));
        }

        Utils.normalize(patterns);
    }

    private List<Long> findProductIds(Category category, int count) {
        Set<Long> productIds = Sets.newTreeSet();
        Set<Long> visited = Sets.newHashSet();

        findProductIds(category, count, productIds, visited);

        return Lists.newArrayList(productIds);
    }

    private void findProductIds(Category category, int count, Set<Long> productIds, Set<Long> visited) {
        if (category.subcategories != null && !category.subcategories.isEmpty()) {
            for (int i = 0; i < count; ++i) {
                int categoryIndex = random.nextInt(category.subcategories.size());
                Category subcategory = category.subcategories.get(categoryIndex);

                if (visited.contains(subcategory.id)) {
                    continue;
                }

                if (subcategory.products != null && !subcategory.products.isEmpty()) {
                    int productIndex = random.nextInt(subcategory.products.size());
                    Product product = subcategory.products.get(productIndex);
                    productIds.add(product.id);
                    visited.add(subcategory.id);
                } else {
                    findProductIds(subcategory, count, productIds, visited);
                }

                if (productIds.size() == count) {
                    break;
                }
            }
        }
    }
}
