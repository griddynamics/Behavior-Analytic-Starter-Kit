package com.griddynamics.deming.data.generator.strategies;

import com.griddynamics.deming.data.generator.strategies.random.RandomConfig;
import com.griddynamics.deming.data.generator.strategies.scenarios.ScenarioConfig;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class GeneratorConfig {
    public long transactionCount;

    public RandomConfig randomConfig;
    public ScenarioConfig scenarioConfig;
}
