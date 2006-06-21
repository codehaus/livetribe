/**
 *
 * Copyright 2006 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.arm.web;

import javax.servlet.ServletContext;
import java.util.logging.Level;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.livetribe.util.servlet.ServletContextListenerBean;


/**
 * Heavily borrowed code from the Spring Framework
 *
 * @version $Revision$ $Date$
 */
public class ArmFramework extends ServletContextListenerBean
{
    public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet-context";
    public static final String SERVLET_CONTEXT_PREFIX = ArmFramework.class.getName() + ".CONTEXT.";
    private boolean publishContext = true;
    private Class contextClass;
    private String namespace;
    private String contextConfigLocation;


    public boolean isPublishContext()
    {
        return publishContext;
    }

    public void setPublishContext(boolean publishContext)
    {
        this.publishContext = publishContext;
    }

    public Class getContextClass()
    {
        return contextClass;
    }

    public void setContextClass(Class contextClass)
    {
        this.contextClass = contextClass;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public String getContextConfigLocation()
    {
        return contextConfigLocation;
    }

    public void setContextConfigLocation(String contextConfigLocation)
    {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * Return the namespace for this servlet, falling back to default scheme if
     * no custom namespace was set: e.g. "test-servlet" for a servlet context named "test".
     */
    public String constructNamespace(String servletContextName)
    {
        return (this.namespace != null) ? this.namespace : servletContextName + DEFAULT_NAMESPACE_SUFFIX;
    }

    protected void initServletContextListenerBean(ServletContext servletContext)
    {
    }

    /**
     * Initialize and publish the WebApplicationContext for this servlet.
     * Delegates to createWebApplicationContext for actual creation.
     * Can be overridden in subclasses.
     *
     * @throws org.springframework.beans.BeansException
     *          if the context couldn't be initialized
     * @see #createWebApplicationContext
     */
    protected WebApplicationContext initWebApplicationContext(ServletContext servletContext) throws BeansException
    {
        String servletContextName = servletContext.getServletContextName();
        servletContext.log("Loading WebApplicationContext for Spring Framework Servlet Context '" + servletContextName + "'");

        WebApplicationContext parent = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        WebApplicationContext wac = createWebApplicationContext(servletContext, parent);
        if (logger.isLoggable(Level.INFO))
        {
            logger.info("Using context class [" + wac.getClass().getName() + "] for servlet '" + servletContextName + "'");
        }

        if (isPublishContext())
        {
            // Publish the context as a servlet context attribute.
            String attrName = constructServletContextAttributeName(servletContextName);
            servletContext.setAttribute(attrName, wac);
            if (logger.isLoggable(Level.FINE))
            {
                logger.log(Level.FINE, "Published WebApplicationContext of servlet context '" + servletContextName +
                                       "' as ServletContext attribute with name [" + attrName + "]");
            }
        }

        return wac;
    }

    /**
     * Instantiate the WebApplicationContext for this servlet, either a default
     * XmlWebApplicationContext or a custom context class if set. This implementation
     * expects custom contexts to implement ConfigurableWebApplicationContext.
     * Can be overridden in subclasses.
     *
     * @param parent the parent ApplicationContext to use, or <code>null</code> if none
     * @return the WebApplicationContext for this servlet
     * @throws BeansException if the context couldn't be initialized
     * @see org.springframework.web.context.support.XmlWebApplicationContext
     */
    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, WebApplicationContext parent) throws BeansException
    {
        String servletContextName = servletContext.getServletContextName();

        if (logger.isLoggable(Level.FINE))
        {
            logger.log(Level.FINE, "Servlet with name '" + servletContextName +
                                   "' will try to create custom WebApplicationContext context of class '" +
                                   getContextClass().getName() + "'" + ", using parent context [" + parent + "]");
        }

        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(getContextClass()))
        {
            throw new ApplicationContextException(
                    "Fatal initialization error in servlet with name '" + servletContextName +
                    "': custom WebApplicationContext class [" + getContextClass().getName() +
                    "] is not of type ConfigurableWebApplicationContext");
        }

        ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(getContextClass());

        wac.setParent(parent);
        wac.setServletContext(servletContext);
        wac.setNamespace(constructNamespace(servletContextName));
        if (getContextConfigLocation() != null)
        {
            String[] locations = StringUtils.tokenizeToStringArray(getContextConfigLocation(), ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
            wac.setConfigLocations(locations);
        }

        wac.refresh();

        return wac;
    }

    protected String constructServletContextAttributeName(String servletContextName)
    {
        return SERVLET_CONTEXT_PREFIX + servletContextName;
    }
}
