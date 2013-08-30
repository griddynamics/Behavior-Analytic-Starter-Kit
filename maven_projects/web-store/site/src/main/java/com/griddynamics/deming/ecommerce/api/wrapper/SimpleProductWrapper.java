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
