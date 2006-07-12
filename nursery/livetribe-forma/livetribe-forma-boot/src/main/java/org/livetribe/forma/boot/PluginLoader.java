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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.AbstractInfo;
import org.livetribe.forma.ExtensionException;
import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.PluginInfo;
import org.livetribe.ioc.Container;
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
        if (!pluginsDirectory.exists())
            throw new FileNotFoundException("Could not find plugins directory " + pluginsDirectory);

        File[] pluginDirs = listPluginDirs(pluginsDirectory);
        Map<String, PluginInfo> pluginInfos = scanPluginDirectories(pluginDirs);
        // Sort the plugins following their dependencies
        List<PluginInfo> sortedPluginInfos = AbstractInfo.sort(pluginInfos);
        for (PluginInfo pluginInfo : sortedPluginInfos) pluginManager.addPluginInfo(pluginInfo);
        for (PluginInfo pluginInfo : sortedPluginInfos) parseExtensions(pluginInfo);
        if (logger.isLoggable(Level.CONFIG)) logger.config("Plugin scanning and parsing completed");

        pluginManager.initPlugins();
        pluginManager.startPlugins();
    }

    /**
     * Scans the plugin directories, looking for classes, libraries and the plugin configuration file.
     * It is important that this method does not perform any classloading, since the plugins directories
     * must first be all scanned, then dependencies resolved and only then classloading can be performed.
     *
     * @param pluginDirs The plugin directories to scan
     * @return An unordered collection of PluginInfos
     * @throws Exception
     */
    private Map<String, PluginInfo> scanPluginDirectories(File[] pluginDirs) throws Exception
    {
        Map<String, PluginInfo> result = new HashMap<String, PluginInfo>();
        for (File pluginDir : pluginDirs)
        {
            PluginInfo pluginInfo = new PluginInfo();
            pluginInfo.setPluginDirectory(pluginDir);

            File pluginClasses = new File(pluginDir, "classes");
            File pluginConfig = new File(pluginClasses, PLUGIN_CONFIG_FILE);
            if (pluginClasses.exists())
            {
                if (!pluginConfig.exists())
                    throw new FileNotFoundException("Could not find " + PLUGIN_CONFIG_FILE + " in plugin directory " + pluginDir);
                if (logger.isLoggable(Level.CONFIG))
                    logger.config("Found " + PLUGIN_CONFIG_FILE + " in plugin directory " + pluginDir);
                pluginInfo.setConfigurationFile(pluginConfig);

                if (logger.isLoggable(Level.CONFIG))
                    logger.config("Found plugin classes in plugin directory " + pluginDir);
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
                    if (logger.isLoggable(Level.CONFIG))
                        logger.config("Found plugin library in plugin directory " + pluginLibs.getName() + ": " + jar.getCanonicalPath());
                    pluginInfo.addLibrary(new JarFile(jar));
                }
            }

            if (logger.isLoggable(Level.CONFIG)) logger.config("Parsing plugin configuration " + pluginConfig);
            Document pluginDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pluginConfig);
            XPath xpath = XPathFactory.newInstance().newXPath();

            String pluginId = xpath.evaluate("/plugin/@id", pluginDocument);
            if (pluginId == null)
                throw new PluginException("Missing required attribute 'id' of element 'plugin' in " + pluginConfig);
            pluginInfo.setPluginId(pluginId);

            String pluginClassName = xpath.evaluate("/plugin/@plugin-class", pluginDocument);
            if (pluginClassName == null)
                throw new PluginException("Missing required attribute 'plugin-class' of element 'plugin' in " + pluginConfig);
            pluginInfo.setPluginClassName(pluginClassName);
            if (logger.isLoggable(Level.CONFIG))
                logger.config("Found plugin '" + pluginId + "', implementation is '" + pluginClassName + "'");

            String bundleName = xpath.evaluate("/plugin/@bundle", pluginDocument);
            pluginInfo.setResourceBundleName(bundleName);

            NodeList dependencies = (NodeList)xpath.evaluate("/plugin/dependencies/dependency", pluginDocument, XPathConstants.NODESET);
            for (int i = 0; i < dependencies.getLength(); ++i)
            {
                Element dependency = (Element)dependencies.item(i);
                String requiredPluginId = xpath.evaluate("text()", dependency);
                pluginInfo.addRequiredPluginId(requiredPluginId);
                if (logger.isLoggable(Level.CONFIG))
                    logger.config("Found dependency: plugin '" + pluginId + "' depends on plugin '" + requiredPluginId + "'");
            }

            String pluginName = xpath.evaluate("/plugin/name/@bundleKey", pluginDocument);
            if (pluginName == null) pluginName = xpath.evaluate("/plugin/name/text()", pluginDocument);
            pluginInfo.setPluginName(pluginName);

            if (result.containsKey(pluginId))
                throw new PluginException("Duplicate plugin id " + pluginId + " in " + pluginConfig);
            result.put(pluginId, pluginInfo);
        }
        return result;
    }

    private void parseExtensions(PluginInfo pluginInfo) throws Exception
    {
        File pluginConfig = pluginInfo.getConfigurationFile();
        Document pluginDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pluginConfig);
        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeList extensions = (NodeList)xpath.evaluate("/plugin/extensions/extension", pluginDocument, XPathConstants.NODESET);
        for (int i = 0; i < extensions.getLength(); ++i)
        {
            Element extensionElement = (Element)extensions.item(i);
            ExtensionInfo extensionInfo = new ExtensionInfo(pluginInfo);

            String extensionId = ExtensionParser.evaluateText(xpath.evaluate("@id", extensionElement));
            if (extensionId == null)
                throw new ExtensionException("Missing required attribute 'id' of element 'extension' in " + pluginConfig);
            extensionInfo.setExtensionId(extensionId);

            String extensionParserClassName = xpath.evaluate("@parser-class", extensionElement);
            if (extensionParserClassName == null)
                throw new ExtensionException("Missing required attribute 'parser-class' of element 'extension' in " + pluginConfig);

            ExtensionParser extensionParser = newExtensionParser(extensionParserClassName);
            if (logger.isLoggable(Level.CONFIG)) logger.config("Parsing extension '" + extensionId + "'");
            extensionParser.parse(extensionElement, extensionInfo);
        }
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
            throw new ExtensionException(x);
        }
    }
}
