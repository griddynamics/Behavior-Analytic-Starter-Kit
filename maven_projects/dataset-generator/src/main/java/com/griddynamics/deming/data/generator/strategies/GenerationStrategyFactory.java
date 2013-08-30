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
