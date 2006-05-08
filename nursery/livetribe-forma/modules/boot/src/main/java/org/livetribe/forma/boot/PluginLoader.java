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
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.livetribe.ioc.Container;
import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.PluginInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class PluginLoader
{
    private static final String PLUGIN_CONFIG_FILE = "plugin.xml";

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final File pluginsDirectory;
    private final PluginManager pluginManager;
    private Container containerManager;

    public PluginLoader(File pluginsDirectory, PluginManager pluginManager, Container containerManager)
    {
        this.pluginsDirectory = pluginsDirectory;
        this.pluginManager = pluginManager;
        this.containerManager = containerManager;
    }

    public void start() throws Exception
    {
        if (!pluginsDirectory.exists()) throw new FileNotFoundException("Could not find plugins directory " + pluginsDirectory);

        Set<String> pluginIds = new HashSet<String>();
        File[] pluginDirs = listPluginDirs(pluginsDirectory);
        for (File pluginDir : pluginDirs)
        {
            PluginInfo pluginInfo = new PluginInfo();
            pluginInfo.setPluginDirectory(pluginDir);

            File pluginClasses = new File(pluginDir, "classes");
            File pluginConfig = new File(pluginClasses, PLUGIN_CONFIG_FILE);
            if (pluginClasses.exists())
            {
                if (!pluginConfig.exists()) throw new FileNotFoundException("Could not find " + PLUGIN_CONFIG_FILE + " in plugin directory " + pluginDir);
                if (logger.isLoggable(Level.CONFIG)) logger.config("Found " + PLUGIN_CONFIG_FILE + " in plugin directory " + pluginDir);
                pluginInfo.setConfigurationFile(pluginConfig);

                if (logger.isLoggable(Level.CONFIG)) logger.config("Found plugin classes in plugin directory " + pluginDir);
                pluginInfo.setClassesDirectory(pluginClasses);
            }

            File pluginLibs = new File(pluginDir, "lib");
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
                    if (logger.isLoggable(Level.CONFIG)) logger.config("Found plugin library in plugin directory " + pluginLibs.getName() + ": " + jar.getCanonicalPath());
                    pluginInfo.addLibrary(new JarFile(jar));
                }
            }

            if (logger.isLoggable(Level.CONFIG)) logger.config("Parsing plugin configuration " + pluginConfig);
            Document pluginDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pluginConfig);
            XPath xpath = XPathFactory.newInstance().newXPath();

            String pluginId = xpath.evaluate("/plugin/@id", pluginDocument);
            if (pluginId == null) throw new PluginException("Missing required attribute 'id' of element 'plugin' in " + pluginConfig);
            if (!pluginIds.add(pluginId)) throw new PluginException("Duplicate plugin id " + pluginId + " in " + pluginConfig);
            pluginInfo.setPluginId(pluginId);

            // Afterwards we are loading classes from this plugin, so we need the PluginClassLoader, via the PluginManager,
            // to be aware of this plugin. But before we must set the pluginId, as it is used as key in hash structures.
            pluginManager.addPluginInfo(pluginInfo);

            String pluginClassName = xpath.evaluate("/plugin/@plugin-class", pluginDocument);
            if (pluginClassName == null) throw new PluginException("Missing required attribute 'plugin-class' of element 'plugin' in " + pluginConfig);
            pluginInfo.setPluginClassName(pluginClassName);
            if (logger.isLoggable(Level.CONFIG)) logger.config("Found plugin '" + pluginId + "', implementation is '" + pluginClassName + "'");

            String bundleName = xpath.evaluate("/plugin/@bundle", pluginDocument);
            if (bundleName != null)
            {
                try
                {
                    ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
                    pluginInfo.setResourceBundle(resourceBundle);
                }
                catch (MissingResourceException x)
                {
                    if (logger.isLoggable(Level.CONFIG)) logger.config("Could not find bundle '" + bundleName + "' for plugin '" + pluginId + "'");
                }
            }

            NodeList dependencies = (NodeList)xpath.evaluate("/plugin/dependencies/dependency", pluginDocument, XPathConstants.NODESET);
            for (int i = 0; i < dependencies.getLength(); ++i)
            {
                Element dependency = (Element)dependencies.item(i);
                String requiredPluginId = xpath.evaluate("text()", dependency);
                pluginInfo.addRequiredPluginId(requiredPluginId);
                if (logger.isLoggable(Level.CONFIG)) logger.config("Found dependency: plugin '" + pluginId + "' depends on plugin '" + requiredPluginId + "'");
            }

            String pluginName = xpath.evaluate("/plugin/name/@bundleKey", pluginDocument);
            if (pluginName == null) pluginName = xpath.evaluate("/plugin/name/text()", pluginDocument);
            pluginInfo.setPluginName(pluginName);

            NodeList extensions = (NodeList)xpath.evaluate("/plugin/extensions/extension", pluginDocument, XPathConstants.NODESET);
            for (int i = 0; i < extensions.getLength(); ++i)
            {
                Element extensionElement = (Element)extensions.item(i);
                ExtensionInfo extensionInfo = new ExtensionInfo(pluginInfo);

                String extensionId = ExtensionParser.evaluateId(xpath.evaluate("@id", extensionElement));
                if (extensionId == null) throw new PluginException("Missing required attribute 'id' of element 'extension' in " + pluginConfig);
                extensionInfo.setExtensionId(extensionId);

                String extensionParserClassName = xpath.evaluate("@parser-class", extensionElement);
                if (extensionParserClassName == null) throw new PluginException("Missing required attribute 'parser-class' of element 'extension' in " + pluginConfig);

                ExtensionParser extensionParser = newExtensionParser(extensionParserClassName);
                if (logger.isLoggable(Level.CONFIG)) logger.config("Parsing extension '" + extensionId + "'");
                extensionParser.parse(extensionElement, extensionInfo);
            }
        }
        if (logger.isLoggable(Level.CONFIG)) logger.config("Plugin scanning and parsing completed");

        pluginManager.initPlugins();
        if (logger.isLoggable(Level.FINE)) logger.fine("Plugins initialized");
        pluginManager.startPlugins();
        if (logger.isLoggable(Level.FINE)) logger.fine("Plugins started");
    }

    private File[] listPluginDirs(File pluginsDirectory)
    {
        return pluginsDirectory.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                return file.isDirectory() && !file.isHidden();
            }
        });
    }

    private ExtensionParser newExtensionParser(String className)
    {
        try
        {
            ExtensionParser parser = (ExtensionParser)Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
            containerManager.resolve(parser);
            return parser;
        }
        catch (Exception x)
        {
            throw new PluginException(x);
        }
    }
}
