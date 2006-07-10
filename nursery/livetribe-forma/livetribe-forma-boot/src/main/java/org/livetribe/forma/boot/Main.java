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

import org.livetribe.util.logging.LoggingManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Registry;
import org.livetribe.ioc.DefaultContainer;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ShutDownManager;

/**
 * @version $Rev$ $Date$
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Logging is a generic service that does not have dependencies on forma.
        LoggingManager logging = new LoggingManager();
        logging.start();

        // Avoid multiple instances of this application on the same host
        StartUpManager startUp = new StartUpManager();
        startUp.start();

        ManagerRegistry managers = new DefaultManagerRegistry();
        managers.put("managerRegistry", ManagerRegistry.class, managers);

        Registry registry = new DefaultContainerManager(managers);
        Container containerManager = new DefaultContainer(registry);
        managers.put("containerManager", Container.class, containerManager);

        PluginManager plugin = new DefaultPluginManager(containerManager);
        managers.put("pluginManager", PluginManager.class, plugin);

        DefaultShutDownManager shutDown = new DefaultShutDownManager(plugin);
        managers.put("shutDownManager", ShutDownManager.class, shutDown);

        PluginClassLoader classLoader = new PluginClassLoader(plugin);
        Thread.currentThread().setContextClassLoader(classLoader);

        // Scan plugins directories
        PluginLoader pluginLoader = new PluginLoader(new File("plugins"), plugin, containerManager);
        pluginLoader.start();
    }
}
