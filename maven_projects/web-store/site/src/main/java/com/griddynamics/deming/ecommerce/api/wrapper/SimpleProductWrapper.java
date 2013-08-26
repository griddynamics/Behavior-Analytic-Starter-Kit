package com.griddynamics.deming.ecommerce.api.wrapper;

import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.web.api.wrapper.APIWrapper;
import org.broadleafcommerce.core.web.api.wrapper.BaseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a JAXB wrapper around Product.
 */
@XmlRootElement(name = "product")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SimpleProductWrapper extends BaseWrapper implements APIWrapper<Product>  {

    @XmlElement
    protected Long id;

    @XmlElement
    protected String name;

    @Override
    public void wrapDetails(Product model, HttpServletRequest request) {
        this.id = model.getId();
        this.name = model.getName();
    }

    @Override
    public void wrapSummary(Product model, HttpServletRequest request) {
        wrapDetails(model, request);
    }
}
