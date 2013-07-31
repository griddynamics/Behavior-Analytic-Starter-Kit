package com.griddynamics.deming.data.generator.strategies;

import com.griddynamics.deming.data.generator.common.Category;
import com.griddynamics.deming.data.generator.strategies.random.RandomTransactionsGenerationStrategy;
import com.griddynamics.deming.data.generator.strategies.scenarios.ScenarioTransactionGenerationStrategy;

import java.io.IOException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class GenerationStrategyFactory {

    /**
     * Create appropriate generation strategy according the configuration file
     *
     * @param config  the generator configuration file
     * @return {@link GenerationStrategy} implementation
     * @throws java.io.IOException
     */
    public static GenerationStrategy createStrategy(GeneratorConfig config, Category catalog) throws IOException {
        if (config.randomConfig != null) {
            return new RandomTransactionsGenerationStrategy(config.randomConfig, catalog);
        } else if (config.scenarioConfig != null) {
            return new ScenarioTransactionGenerationStrategy(config.scenarioConfig, catalog);
        } else {
            throw new RuntimeException("Suitable config not found.");
        }
    }
}
