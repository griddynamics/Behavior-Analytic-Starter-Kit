package com.griddynamics.deming.ecommerce.web.dialect;

import com.google.common.collect.Sets;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

public class DmgDialect extends AbstractDialect {

    private Set<IProcessor> processors = Sets.newHashSet();

    @Override
    public String getPrefix() {
        return "dmg";
    }

    @Override
    public boolean isLenient() {
        return true;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(Set<IProcessor> processors) {
        this.processors = processors;
    }
}
