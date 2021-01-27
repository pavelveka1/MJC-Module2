package com.epam.esm.configuration;

import com.epam.esm.service.configuration.ServiceConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @Class WebConfiguration
 */
public class WebConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * @return root config classes
     * @method getRootConfigClasses
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    /**
     * @return array of classes for Servlet configuration
     * @method getServletConfigClasses
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DBConfig.class, ServiceConfiguration.class};
    }

    /**
     * @return mapping which is set
     * @method getServletMapping
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
