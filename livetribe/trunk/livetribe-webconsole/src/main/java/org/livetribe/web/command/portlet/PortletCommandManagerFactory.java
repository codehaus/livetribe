/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.web.command.portlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * $Rev: 51 $
 */
public class PortletCommandManagerFactory
{
    private static final String CONFIGURATION_FILE = "portlet-commands.properties";
    private static final Log logger = LogFactory.getLog(PortletCommandManagerFactory.class);
    private static final PortletCommandManagerFactory instance = new PortletCommandManagerFactory();

    private Properties configuration;

    private PortletCommandManagerFactory()
    {
        try
        {
            InputStream configStream = getClass().getClassLoader().getResourceAsStream(CONFIGURATION_FILE);
            if (configStream == null) throw new FileNotFoundException("Could not find " + CONFIGURATION_FILE + " in classpath");
            configuration = new Properties();
            configuration.load(configStream);
        }
        catch (IOException x)
        {
            throw new RuntimeException(x);
        }
    }

    public static PortletCommandManager newCommandManager()
    {
        String className = instance.configuration.getProperty("portletCommandManager");
        PortletCommandManager portletCommandManager = null;
        if (className != null)
        {
            try
            {
                Class cls = instance.getClass().getClassLoader().loadClass(className);
                portletCommandManager = (PortletCommandManager)cls.newInstance();
            }
            catch (ClassNotFoundException x)
            {
                if (logger.isInfoEnabled()) logger.info("Could not find class " + className + ", using standard PortletCommandManager");
            }
            catch (IllegalAccessException e)
            {
                if (logger.isInfoEnabled()) logger.info("Could not instantiate class " + className + ", using standard PortletCommandManager");
            }
            catch (InstantiationException e)
            {
                if (logger.isInfoEnabled()) logger.info("Could not instantiate class " + className + ", using standard PortletCommandManager");
            }
        }
        else
        {
            portletCommandManager = new StandardPortletCommandManager();
        }
        portletCommandManager.setConfiguration(Collections.unmodifiableMap(instance.configuration));
        return portletCommandManager;
    }
}
