package com.griddynamics.deming.ecommerce.api.wrapper;

import com.google.common.collect.Lists;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.web.api.wrapper.APIWrapper;
import org.broadleafcommerce.core.web.api.wrapper.BaseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This is a JAXB wrapper class for wrapping a catalog.
 */
@XmlRootElement(name = "catalog")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SimpleCatalogWrapper extends SimpleCategoryWrapper {

}
