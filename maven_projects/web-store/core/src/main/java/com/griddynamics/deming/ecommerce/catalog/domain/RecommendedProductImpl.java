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

package com.griddynamics.deming.ecommerce.catalog.domain;

import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="DMG_PRODUCT_RECOMMENDATION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
public class RecommendedProductImpl implements RecommendedProduct {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "DmgRecommendedProductId")
    @GenericGenerator(
            name="DmgRecommendedProductId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="RecommendedProductImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="com.griddynamics.deming.ecommerce.catalog.domain.RecommendedProductImpl")
            }
    )
    @Column(name = "DMG_RECOMMENDED_PRODUCT_ID")
    protected Long id;

    @ManyToOne(targetEntity = ProductImpl.class)
    @JoinColumn(name = "PRODUCT_ID")
    @Index(name="DMG_RECOMMENDED_INDEX", columnNames={"PRODUCT_ID"})
    protected Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
