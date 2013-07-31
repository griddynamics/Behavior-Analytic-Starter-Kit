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
