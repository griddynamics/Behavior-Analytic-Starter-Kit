package com.griddynamics.deming.ecommerce.web.processor;

import com.griddynamics.deming.ecommerce.order.domain.DmgOrder;
import com.griddynamics.deming.ecommerce.recommendation.RecommendationService;
import org.broadleafcommerce.common.web.dialect.AbstractModelVariableModifierProcessor;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.web.order.CartState;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import javax.annotation.Resource;

public class RecommendationsProcessor extends AbstractModelVariableModifierProcessor {

    @Resource(name = "dmgRecommendationService")
    protected RecommendationService recommendationService;

    public RecommendationsProcessor() {
        super("set_recommendations");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        Order cart = CartState.getCart();

        if (cart instanceof DmgOrder) {
            recommendationService.populateRecommendations((DmgOrder) cart);
        }

        addToModel(arguments, "cart", cart);
    }
}
