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

package com.griddynamics.deming.ecommerce.order.domain;

import com.google.common.collect.Lists;
import com.griddynamics.deming.ecommerce.catalog.domain.RecommendedProduct;
import org.broadleafcommerce.core.order.domain.OrderImpl;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "DMG_ORDER")
public class DmgOrder extends OrderImpl {

    private static final long serialVersionUID = 1L;

    @Transient
    protected List<RecommendedProduct> recommendations = Lists.newArrayList();

    public List<RecommendedProduct> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendedProduct> recommendations) {
        this.recommendations = recommendations;
    }
}
