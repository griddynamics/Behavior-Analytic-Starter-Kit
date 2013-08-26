package com.griddynamics.deming.ecommerce.api.wrapper;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "category")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ImportCategoryWrapper {

    @XmlElement
    public String name;

    @XmlElement
    public String description;

    @XmlElement(name = "subcategories")
    public List<ImportCategoryWrapper> subcategories;

    @XmlElement(name = "products")
    public List<ImportProductWrapper> products;
}
