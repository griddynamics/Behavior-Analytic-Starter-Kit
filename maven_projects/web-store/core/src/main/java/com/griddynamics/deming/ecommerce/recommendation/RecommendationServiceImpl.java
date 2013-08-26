package com.griddynamics.deming.ecommerce.recommendation;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.griddynamics.deming.ecommerce.catalog.domain.RecommendedProduct;
import com.griddynamics.deming.ecommerce.catalog.domain.RecommendedProductImpl;
import com.griddynamics.deming.ecommerce.order.domain.DmgOrder;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RecommendationServiceImpl implements RecommendationService {

    public final Map<String, String> fbp = Maps.newTreeMap();

    @Resource(name = "blCatalogService")
    private CatalogService catalogService;

    public RecommendationServiceImpl() {
        fbp.put("1", "2,3,4,5");
        fbp.put("2", "1,3,4,5");
        fbp.put("3", "1,2,4,5");
        fbp.put("4", "1,2,3,5");
        fbp.put("5", "1,2,3,4");
        fbp.put("1,2", "3,4,5");
        fbp.put("1,3", "2,4,5");
        fbp.put("1,4", "2,3,5");
        fbp.put("1,5", "2,3,4");
    }

    @Override
    public void loadRecommendations(InputStream inputStream) throws IOException {
        fbp.clear();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            String[] split = line.split("\\s+");

            String[] items = split[0].split(",");
            Arrays.sort(items);
            String key = Joiner.on(",").join(items);

            items = split[1].split(",");
            Arrays.sort(items);
            String value = Joiner.on(",").join(items);

            fbp.put(key, value);
        }
    }

    @Override
    public void populateRecommendations(DmgOrder cart) {

        List<String> productIdList = Lists.newArrayList();

        for (DiscreteOrderItem item : cart.getDiscreteOrderItems()) {
            String productId = item.getProduct().getId().toString();
            productIdList.add(productId);
        }

        Collections.sort(productIdList);

        String key = Joiner.on(',').join(productIdList);

        List<RecommendedProduct> recommendations = Lists.newArrayList();

        if (fbp.containsKey(key)) {

            String value = fbp.get(key);

            for (String id : value.split(",")) {
                Product product = catalogService.findProductById(Long.valueOf(id));

                if (product != null) {
                    RecommendedProduct recommendedProduct = new RecommendedProductImpl();
                    recommendedProduct.setId(product.getId());
                    recommendedProduct.setProduct(product);

                    recommendations.add(recommendedProduct);
                }
            }
        }

        cart.setRecommendations(recommendations);
    }
}
