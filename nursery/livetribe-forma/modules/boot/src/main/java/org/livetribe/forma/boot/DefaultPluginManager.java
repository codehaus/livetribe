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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.EventQueue;

import org.livetribe.ioc.Container;
import org.livetribe.forma.Plugin;
import org.livetribe.forma.PluginInfo;

/**
 * @version $Rev: 118 $ $Date$
 */
public class DefaultPluginManager implements PluginManager
{
    private final Container containerManager;
    private final Lock lock = new ReentrantLock();
    private final Map<String, PluginInfo> pluginInfos = new HashMap<String, PluginInfo>();
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
        List<PluginInfo> sortedPluginInfos = sort(pluginInfos);
        for (PluginInfo pluginInfo : sortedPluginInfos)
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
                plugin.init();
            }
        });
    }

    private void startPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                plugin.start();
            }
        });
    }

    private void stopPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                plugin.stop();
            }
        });
    }

    private void destroyPlugin(final Plugin plugin)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                plugin.destroy();
            }
        });
    }

    /**
     * Returns a sorted copy of the given plugin infos.
     * The sort is a partial ordered sort, since plugin infos cannot in
     * general be compared using a less-than operator.
     * However, plugins that declare dependencies may be compared, hence
     * the partial ordered sort.
     */
    private List<PluginInfo> sort(Map<String, PluginInfo> pluginInfos)
    {
        Set<String> dead = new LinkedHashSet<String>();
        List<PluginInfo> result = new LinkedList<PluginInfo>();
        for (PluginInfo pluginInfo : pluginInfos.values()) visit(pluginInfos, pluginInfo, dead, result);
        return result;
    }

    private void visit(Map<String, PluginInfo> pluginInfos, PluginInfo pluginInfo, Set<String> dead, List<PluginInfo> result)
    {
        String pluginId = pluginInfo.getPluginId();
        if (!dead.add(pluginId))
        {
            if (result.contains(pluginInfo)) return;

            StringBuilder builder = new StringBuilder();
            for (String deadPluginId : dead) builder.append(deadPluginId).append(" < ");
            builder.append(pluginId);
            throw new PluginException("Cyclic dependency among plugins ('<' means 'depends on'): " + builder);
        }

        for (String childPluginId : pluginInfo.getRequiredPluginIds())
        {
            PluginInfo childPluginInfo = pluginInfos.get(childPluginId);
            if (childPluginInfo == null) throw new PluginException("Broken dependency " + childPluginId + " for plugin " + pluginId);
            visit(pluginInfos, childPluginInfo, dead, result);
        }
        result.add(pluginInfo);
    }
}
