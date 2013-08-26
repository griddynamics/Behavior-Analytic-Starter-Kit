package com.griddynamics.deming.ecommerce.order.service;

import com.griddynamics.deming.ecommerce.order.domain.DmgOrder;
import com.griddynamics.deming.ecommerce.recommendation.RecommendationService;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderServiceImpl;
import org.broadleafcommerce.profile.core.domain.Customer;

import javax.annotation.Resource;

public class DmgOrderService extends OrderServiceImpl {

    @Resource(name = "dmgRecommendationService")
    protected RecommendationService recommendationService;

    @Override
    public Order findCartForCustomer(Customer customer) {
        DmgOrder cartForCustomer = (DmgOrder) super.findCartForCustomer(customer);

        if (cartForCustomer != null) {
            recommendationService.populateRecommendations(cartForCustomer);
        }

        return cartForCustomer;
    }
}
