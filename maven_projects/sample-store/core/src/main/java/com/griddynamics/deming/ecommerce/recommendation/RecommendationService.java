package com.griddynamics.deming.ecommerce.recommendation;

import com.griddynamics.deming.ecommerce.order.domain.DmgOrder;

import java.io.IOException;
import java.io.InputStream;

public interface RecommendationService {

    void loadRecommendations(InputStream inputStream) throws IOException;
    void populateRecommendations(DmgOrder order);
}
