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

import java.util.Set;

/**
 * A registry for plugins.
 * @see PluginsLoader
 * @version $Rev$ $Date$
 */
public interface IPluginRegistry
{
    /**
     * Adds a plugin to this registry
     * @param pluginInfo The plugin information to be added
     */
    public void addPluginInfo(IPluginInfo pluginInfo);

    /**
     * Returns a copy of the plugins contained in this registry.
     */
    public Set<IPluginInfo> getPluginInfos();
}
