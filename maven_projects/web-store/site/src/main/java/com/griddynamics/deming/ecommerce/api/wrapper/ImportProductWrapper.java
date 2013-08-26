package com.griddynamics.deming.ecommerce.api.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ImportProductWrapper {

    @XmlElement
    public String name;

    @XmlElement
    public String description;

    @XmlElement
    public String image;
}
