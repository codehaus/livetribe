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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version $Rev$ $Date$
 */
public class PluginRegistry implements IPluginRegistry
{
    private final Lock lock = new ReentrantLock();
    private final Set<IPluginInfo> pluginInfos = new HashSet<IPluginInfo>();

    public void addPluginInfo(IPluginInfo pluginInfo)
    {
        lock.lock();
        try
        {
            boolean added = pluginInfos.add(pluginInfo);
            if (!added) throw new IllegalArgumentException("Could not add plugin " + pluginInfo + ", it already exists");
        }
        finally
        {
            lock.unlock();
        }
    }

    public Set<IPluginInfo> getPluginInfos()
    {
        Set<IPluginInfo> result = new HashSet<IPluginInfo>();
        lock.lock();
        try
        {
            result.addAll(pluginInfos);
        }
        finally
        {
            lock.unlock();
        }
        return result;
    }
}
