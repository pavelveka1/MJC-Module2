package com.epam.esm.configuration;

import com.epam.esm.service.configuration.ServiceConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Class WebConfiguration
 */
public class WebConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Method getRootConfigClasses
     *
     * @return root config classes
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    /**
     * Method getServletConfigClasses
     *
     * @return array of classes for Servlet configuration
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DBConfig.class, ServiceConfiguration.class};
    }

    /**
     * Method getServletMapping
     *
     * @return mapping which is set
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
