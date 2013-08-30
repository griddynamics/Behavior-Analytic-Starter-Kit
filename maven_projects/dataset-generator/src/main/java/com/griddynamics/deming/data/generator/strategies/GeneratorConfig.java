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
