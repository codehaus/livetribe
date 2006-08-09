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
package org.livetribe.util.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResourceLoader;


/**
 * Code stolen from Spring.
 *
 * @version $Revision$ $Date$
 */
public abstract class ServletContextListenerBean implements ServletContextListener
{
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final Set required = new HashSet();

    /**
     * Subclasses can invoke this method to specify that this property
     * (which must match a JavaBean property they expose) is mandatory,
     * and must be supplied as a config parameter. This should be called
     * from the constructor of a subclass.
     * <p>This method is only relevant in case of traditional initialization
     * driven by a ServletConfig instance.
     *
     * @param property name of the required property
     */
    protected final void addRequiredProperty(String property)
    {
        required.add(property);
    }

    /**
     * Initialize the BeanWrapper for this ServletContextListenerBean,
     * possibly with custom editors.
     * <p>This default implementation is empty.
     *
     * @param bw the BeanWrapper to initialize
     * @throws BeansException if thrown by BeanWrapper methods
     * @see org.springframework.beans.BeanWrapper#registerCustomEditor
     */
    protected void initBeanWrapper(BeanWrapper bw) throws BeansException
    {
    }

    /**
     * Subclasses may override this to perform custom initialization.
     * All bean properties of this servlet will have been set before this
     * method is invoked.
     * <p>This default implementation is empty.
     */
    protected void initServletContextListenerBean(ServletContext servletContext)
    {
    }

    /**
     * Subclasses may override this to perform custom teardown.
     * <p>This default implementation is empty.
     */
    protected void destroyServletContextListenerBean()
    {
    }

    /**
     * Map config parameters onto bean properties of this servlet, and
     * invoke subclass initialization.
     */
    public final void contextInitialized(ServletContextEvent servletContextEvent)
    {
        ServletContext servletContext = servletContextEvent.getServletContext();

        if (logger.isLoggable(Level.INFO))
        {
            logger.info("Initializing servlet context'" + servletContext.getServletContextName() + "'");
        }

        // Set bean properties from init parameters.
        try
        {
            PropertyValues pvs = new ServletContextPropertyValues(servletContext, required);
            BeanWrapper bw = new BeanWrapperImpl(this);
            ResourceLoader resourceLoader = new ServletContextResourceLoader(servletContext);
            bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader));
            initBeanWrapper(bw);
            bw.setPropertyValues(pvs, true);
        }
        catch (BeansException ex)
        {
            logger.log(Level.WARNING, "Failed to set bean properties on servlet context '" + servletContext.getServletContextName() + "'", ex);
            throw ex;
        }

        // Let subclasses do whatever initialization they like.
        initServletContextListenerBean(servletContext);

        if (logger.isLoggable(Level.INFO))
        {
            logger.info("Servlet config '" + servletContext.getServletContextName() + "' configured successfully");
        }
    }

    public final void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        destroyServletContextListenerBean();
    }

    /**
     * PropertyValues implementation created from ServletContext init parameters.
     */
    private static class ServletContextPropertyValues extends MutablePropertyValues
    {
        /**
         * Create new ServletContextPropertyValues.
         *
         * @param servletContext ServletContext we'll use to take PropertyValues from
         * @param required       set of property names we need, where
         *                       we can't accept default values
         */
        public ServletContextPropertyValues(ServletContext servletContext, Set required)
        {

            Set missingProps = (required != null && !required.isEmpty()) ? new HashSet(required) : null;

            Enumeration parameterNames = servletContext.getInitParameterNames();
            while (parameterNames.hasMoreElements())
            {
                String name = (String) parameterNames.nextElement();
                Object value = servletContext.getInitParameter(name);

                addPropertyValue(new PropertyValue(name, value));

                if (missingProps != null)
                {
                    missingProps.remove(name);
                }
            }

            // Fail if we are still missing properties.
            if (missingProps != null && missingProps.size() > 0)
            {
                throw new FatalBeanException(
                        "Initialization from ServletContext for servlet context '" + servletContext.getServletContextName() +
                        "' failed; the following required properties were missing: " +
                        StringUtils.collectionToDelimitedString(missingProps, ", "));
            }
        }
    }
}
