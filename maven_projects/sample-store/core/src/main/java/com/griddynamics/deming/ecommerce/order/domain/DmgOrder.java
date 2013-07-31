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
