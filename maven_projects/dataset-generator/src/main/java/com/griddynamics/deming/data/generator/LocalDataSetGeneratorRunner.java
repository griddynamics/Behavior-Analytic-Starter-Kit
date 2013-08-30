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

package com.griddynamics.deming.data.generator;

import com.griddynamics.deming.data.generator.common.Category;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategy;
import com.griddynamics.deming.data.generator.strategies.GenerationStrategyFactory;
import com.griddynamics.deming.data.generator.strategies.GeneratorConfig;
import com.griddynamics.deming.data.generator.serialization.JsonUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class LocalDataSetGeneratorRunner {

    public static void main(String[] args) throws IOException {
        Category catalog = readFromResource(Category.class, "/product-catalog.json");
        GeneratorConfig config = readFromResource(GeneratorConfig.class, "/scenario-config.json");

        GenerationStrategy generationStrategy = GenerationStrategyFactory.createStrategy(config, catalog);

        for (int i = 0; i < 10; ++i) {
            System.out.println(generationStrategy.generateTransaction());
        }
    }

    private static <T> T readFromResource(Class<T> tClass, String resource) throws IOException {
        InputStream inputStream = LocalDataSetGeneratorRunner.class.getResourceAsStream(resource);
        try {
            return JsonUtil.read(tClass, inputStream);
        } finally {
            inputStream.close();
        }
    }
}
