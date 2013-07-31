package com.griddynamics.deming.ecommerce.web.processor;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.common.web.dialect.AbstractModelVariableModifierProcessor;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryXref;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

import javax.annotation.Resource;
import java.util.List;

public class SubcategoriesProcessor extends AbstractModelVariableModifierProcessor {

    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    public SubcategoriesProcessor() {
        super("subcategories");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String resultVar = element.getAttributeValue("resultVar");
        String unparsedParentCategoryId = element.getAttributeValue("parentCategoryId");
        String unparsedMaxResults = element.getAttributeValue("maxResults");

        Long parentCategoryId = (Long) StandardExpressionProcessor.processExpression(arguments, unparsedParentCategoryId);

        Category category = catalogService.findCategoryById(parentCategoryId);

        if (category != null) {
            List<CategoryXref> subcategories = category.getChildCategoryXrefs();

            if (CollectionUtils.isNotEmpty(subcategories)) {
                if (StringUtils.isNotEmpty(unparsedMaxResults)) {
                    int maxResults = Integer.parseInt(unparsedMaxResults);
                    if (subcategories.size() > maxResults) {
                        subcategories = subcategories.subList(0, maxResults);
                    }
                }

                List<Category> results = Lists.transform(subcategories, new Function<CategoryXref, Category>() {
                    @Override
                    public Category apply(CategoryXref categoryXref) {
                        return categoryXref.getSubCategory();
                    }
                });

                addToModel(arguments, resultVar, results);
            }
        }
    }
}
