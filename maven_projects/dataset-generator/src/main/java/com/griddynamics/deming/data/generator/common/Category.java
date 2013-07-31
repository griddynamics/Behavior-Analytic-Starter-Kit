package com.griddynamics.deming.data.generator.common;

import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class Category {
    public Long id;
    public String name;
    public List<Category> subcategories;
    public List<Product> products;
}
