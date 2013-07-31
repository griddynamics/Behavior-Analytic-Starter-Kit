package com.griddynamics.deming.ecommerce.api.wrapper;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.web.api.wrapper.APIWrapper;
import org.broadleafcommerce.core.web.api.wrapper.BaseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * This is a JAXB wrapper class for wrapping a category.
 */
@XmlRootElement(name = "category")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SimpleCategoryWrapper extends BaseWrapper implements APIWrapper<Category> {

    @XmlElement
    protected Long id;

    @XmlElement
    protected String name;

    @XmlElement(name = "category")
    @XmlElementWrapper(name = "subcategories")
    protected List<SimpleCategoryWrapper> subcategories;

    @XmlElement(name = "product")
    @XmlElementWrapper(name = "products")
    protected List<SimpleProductWrapper> products;

    @Override
    public void wrapDetails(Category category, HttpServletRequest request) {
        id = category.getId();
        name = category.getName();

        buildDetailedSubcategoryTree(category, request);
        buildDetailedProductList(category, request);
    }

    @Override
    public void wrapSummary(Category category, HttpServletRequest request) {
        wrapDetails(category, request);
    }

    protected List<SimpleCategoryWrapper> buildDetailedSubcategoryTree(
            Category root, HttpServletRequest request) {

        CatalogService catalogService = (CatalogService) context.getBean("blCatalogService");
        List<Category> activeSubcategories = catalogService.findActiveSubCategoriesByCategory(root);

        if (CollectionUtils.isEmpty(activeSubcategories)) {
            return null;
        }

        if (subcategories == null) {
            subcategories = Lists.newArrayList();
        }

        for (Category category : activeSubcategories) {
            SimpleCategoryWrapper subcategoryWrapper = createSimpleCategoryWrapper();
            subcategoryWrapper.wrapDetails(category, request);
            subcategories.add(subcategoryWrapper);
        }

        return subcategories;
    }

    protected void buildDetailedProductList(Category category, HttpServletRequest request) {
        CatalogService catalogService = (CatalogService) context.getBean("blCatalogService");
        List<Product> categoryProducts = catalogService.findProductsForCategory(category);

        if (CollectionUtils.isEmpty(categoryProducts)) {
            return;
        }

        if (products == null) {
            products = Lists.newArrayList();
        }

        for (Product product : categoryProducts) {
            SimpleProductWrapper productWrapper = createSimpleProductWrapper();
            productWrapper.wrapSummary(product, request);
            products.add(productWrapper);
        }
    }

    private SimpleCategoryWrapper createSimpleCategoryWrapper() {
        return (SimpleCategoryWrapper) context.getBean(SimpleCategoryWrapper.class.getName());
    }

    private SimpleProductWrapper createSimpleProductWrapper() {
        return (SimpleProductWrapper) context.getBean(SimpleProductWrapper.class.getName());
    }
}
