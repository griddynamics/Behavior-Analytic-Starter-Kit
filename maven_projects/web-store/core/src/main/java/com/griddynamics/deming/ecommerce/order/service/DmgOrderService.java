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
