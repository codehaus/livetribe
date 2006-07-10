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

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.livetribe.forma.Plugin;
import org.livetribe.forma.PluginInfo;
import org.livetribe.ioc.Container;

/**
 * @version $Rev: 118 $ $Date$
 */
public class DefaultPluginManager implements PluginManager
{
    private final Container containerManager;
    private final Lock lock = new ReentrantLock();
    private final Map<String, PluginInfo> pluginInfos = new LinkedHashMap<String, PluginInfo>();
    private final List<Plugin> plugins = new ArrayList<Plugin>();

    public DefaultPluginManager(Container containerManager)
    {
        this.containerManager = containerManager;
    }

    public void addPluginInfo(PluginInfo pluginInfo)
    {
        lock.lock();
        try
        {
            String pluginId = pluginInfo.getPluginId();
            if (pluginInfos.containsKey(pluginId)) throw new PluginException("Duplicate plugin id " + pluginId + " in " + pluginInfo.getConfigurationFile());
            pluginInfos.put(pluginId, pluginInfo);
        }
        finally
        {
            lock.unlock();
        }
    }

    public List<PluginInfo> getPluginInfos()
    {
        List<PluginInfo> result = new ArrayList<PluginInfo>();
        lock.lock();
        try
        {
            result.addAll(pluginInfos.values());
        }
        finally
        {
            lock.unlock();
        }
        return result;
    }

    public void initPlugins()
    {
        for (PluginInfo pluginInfo : pluginInfos.values())
        {
            Plugin plugin = newPlugin(pluginInfo);
            plugins.add(plugin);
        }
        for (Plugin plugin : plugins)
        {
            initPlugin(plugin);
        }
    }

    public void startPlugins()
    {
        for (Plugin plugin : plugins)
        {
            startPlugin(plugin);
        }
    }

    public void stopPlugins()
    {
        for (ListIterator<Plugin> iterator = plugins.listIterator(plugins.size()); iterator.hasPrevious();)
        {
            Plugin plugin = iterator.previous();
            stopPlugin(plugin);
        }
    }

    public void destroyPlugins()
    {
        for (ListIterator<Plugin> iterator = plugins.listIterator(plugins.size()); iterator.hasPrevious();)
        {
            Plugin plugin = iterator.previous();
            destroyPlugin(plugin);
        }
    }

    private Plugin newPlugin(PluginInfo pluginInfo)
    {
        try
        {
            Plugin plugin = (Plugin)Thread.currentThread().getContextClassLoader().loadClass(pluginInfo.getPluginClassName()).newInstance();
            containerManager.resolve(plugin);
            return plugin;
        }
        catch (Exception x)
        {
            throw new PluginException(x);
        }
    }

    private void initPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    plugin.init();
                }
                catch (Exception x)
                {
                    throw new PluginException(x);
                }
            }
        });
    }

    private void startPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    plugin.start();
                }
                catch (Exception x)
                {
                    throw new PluginException(x);
                }
            }
        });
    }

    private void stopPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    plugin.stop();
                }
                catch (Exception x)
                {
                    throw new PluginException(x);
                }
            }
        });
    }

    private void destroyPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    plugin.destroy();
                }
                catch (Exception x)
                {
                    throw new PluginException(x);
                }
            }
        });
    }
}
