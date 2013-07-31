package com.griddynamics.deming.ecommerce.catalog.domain;

import org.broadleafcommerce.core.catalog.domain.Product;

import java.io.Serializable;

public interface RecommendedProduct extends Serializable {

    public Long getId();
    public Product getProduct();

    public void setId(Long id);
    public void setProduct(Product product);
}
