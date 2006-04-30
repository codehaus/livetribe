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
package org.livetribe.forma.boot;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @version $Rev$ $Date$
 */
public class PluginsLoader implements ApplicationContextAware
{
    private static final String PLUGINS_CONFIG_FILE = "plugins.xml";
    private static final String PLUGIN_SERVICES_CONFIG_FILE = "services.xml";

    private Logger logger = Logger.getLogger(getClass().getName());
    private ApplicationContext bootApplicationContext;
    private File pluginsDirectory;
    private FileSystemXmlApplicationContext applicationContext;
    private PluginClassLoader pluginClassLoader;
    private IPluginRegistry pluginRegistry;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        bootApplicationContext = applicationContext;
    }

    public void setPluginsDirectory(File modulesDirectory)
    {
        this.pluginsDirectory = modulesDirectory;
    }

    public void setPluginClassLoader(PluginClassLoader classLoader)
    {
        this.pluginClassLoader = classLoader;
    }

    public void setPluginRegistry(IPluginRegistry pluginRegistry)
    {
        this.pluginRegistry = pluginRegistry;
    }

    public void start() throws Exception
    {
        File pluginsConfiguration = new File(pluginsDirectory, PLUGINS_CONFIG_FILE);
        if (!pluginsConfiguration.exists()) throw new FileNotFoundException("Could not find plugins configuration file " + pluginsConfiguration);

        if (logger.isLoggable(Level.CONFIG)) logger.config("Reading configuration file " + pluginsConfiguration.getCanonicalPath());
        List<String> pluginNames = new ArrayList<String>();
        List<File> serviceFiles = new ArrayList<File>();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        InputSource source = new InputSource(new FileReader(pluginsConfiguration));
        NodeList plugins = (NodeList)xpath.evaluate("/plugins/plugin", source, XPathConstants.NODESET);
        for (int i = 0; i < plugins.getLength(); ++i)
        {
            Element plugin = (Element)plugins.item(i);
            String pluginName = xpath.evaluate("@name", plugin);
            if (pluginName != null)
            {
                if (logger.isLoggable(Level.CONFIG)) logger.config("Configuring plugin '" + pluginName + "'");
                pluginNames.add(pluginName);

                File pluginDirectory = new File(pluginsDirectory, pluginName);
                if (pluginDirectory.exists())
                {
                    PluginInfo info = new PluginInfo();

                    File pluginClasses = new File(pluginDirectory, "classes");
                    if (pluginClasses.exists())
                    {
                        if (logger.isLoggable(Level.CONFIG)) logger.config("Found plugin classes for plugin '" + pluginName + "'");
                        info.setClasses(pluginClasses);

                        File pluginServices = new File(pluginClasses, PLUGIN_SERVICES_CONFIG_FILE);
                        if (pluginServices.exists())
                        {
                            if (logger.isLoggable(Level.CONFIG)) logger.config("Found plugin services for plugin '" + pluginName + "'");
                            info.addServices(pluginServices);
                            serviceFiles.add(pluginServices.getCanonicalFile());
                        }
                    }

                    File pluginLibs = new File(pluginDirectory, "lib");
                    if (pluginLibs.exists())
                    {
                        File[] jars = pluginLibs.listFiles(new FileFilter()
                        {
                            public boolean accept(File file)
                            {
                                return !file.isHidden() && file.isFile() && file.getName().endsWith(".jar");
                            }
                        });
                        for (File jar : jars)
                        {
                            if (logger.isLoggable(Level.CONFIG)) logger.config("Found plugin library for plugin '" + pluginName + "': " + jar.getCanonicalPath());
                            info.addLibrary(new JarFile(jar));
                        }
                    }

                    pluginRegistry.addPluginInfo(info);
                }
                else
                {
                    if (logger.isLoggable(Level.WARNING))
                        logger.warning("Plugin '" + pluginName + "' is defined in " + PLUGINS_CONFIG_FILE + ", but no such directory is found under " + pluginsDirectory.getCanonicalPath());
                }
            }
        }

        List<String> servicesResources = relativize(serviceFiles, new File("."));

        Thread.currentThread().setContextClassLoader(pluginClassLoader);
        if (logger.isLoggable(Level.CONFIG)) logger.config("Configuring plugins " + pluginNames);
        applicationContext = new FileSystemXmlApplicationContext(servicesResources.toArray(new String[servicesResources.size()]), bootApplicationContext);
    }

    private List<String> relativize(List<File> serviceFiles, File reference) throws IOException
    {
        List<String> result = new ArrayList<String>(serviceFiles.size());
        for (File file: serviceFiles) result.add(relativize(file, reference));
        return result;
    }

    private String relativize(File file, File reference) throws IOException
    {
        // We just support the case where the reference is an ancestor of file
        String canonical = file.getCanonicalPath();
        String canonicalReference = reference.getCanonicalPath();
        if (canonical.startsWith(canonicalReference))
        {
            return canonical.substring(canonicalReference.length());
        }
        throw new IOException("Could not relativize " + canonical + " to " + canonicalReference);
    }

    public void stop()
    {
        applicationContext.close();
    }
}
