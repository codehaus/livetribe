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

import java.net.URL;
import java.security.SecureClassLoader;

/**
 * @version $Rev$ $Date$
 */
public class PluginClassLoader extends SecureClassLoader
{
    private IPluginRegistry pluginRegistry;

    public PluginClassLoader()
    {
        this(Thread.currentThread().getContextClassLoader());
    }

    public PluginClassLoader(ClassLoader parent)
    {
        super(parent);
    }

    public void setPluginRegistry(IPluginRegistry pluginRegistry)
    {
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        // Keep synchronized to respect ClassLoader.loadClass(), which is synchronized
        synchronized (this)
        {
            // First, look locally into the JVM cache of this classloader
            Class<?> result = findLoadedClass(name);
            if (result == null)
            {
                // Second, look if this classloader can load the class
                result = findClassLocally(name);
                if (result == null)
                {
                    // Third, delegate to parent classloader
                    result = super.loadClass(name, resolve);
                }
            }
            if (resolve) resolveClass(result);
            return result;
        }
    }

    private Class<?> findClassLocally(String name) throws ClassNotFoundException
    {
        for (IPluginInfo info : pluginRegistry.getPluginInfos())
        {
            byte[] classBytes = info.getClassBytes(name);
            if (classBytes != null)
            {
                // TODO: handle codesource/protectiondomain
                return defineClass(name, classBytes, 0, classBytes.length);
            }
        }
        return null;
    }

    @Override
    public URL getResource(String name)
    {
        URL result = findResourceLocally(name);
        if (result == null)
        {
            result = super.getResource(name);
        }
        return result;
    }

    private URL findResourceLocally(String name)
    {
        for (IPluginInfo info : pluginRegistry.getPluginInfos())
        {
            URL resource = info.getResource(name);
            if (resource != null) return resource;
        }
        return null;
    }
}
