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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.livetribe.forma.ShutDownManager;

/**
 * @version $Rev$ $Date$
 */
public class DefaultShutDownManager implements ShutDownManager
{
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final PluginManager pluginManager;

    public DefaultShutDownManager(PluginManager pluginManager)
    {
        this.pluginManager = pluginManager;
    }

    public void shutdown()
    {
        pluginManager.stopPlugins();
        pluginManager.destroyPlugins();

        if (logger.isLoggable(Level.FINE)) logger.fine("JVM is exiting now");
        System.exit(0);
    }
}
