/**
 *
 * Copyright 2006 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.arm40.classloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.xbean.classloader.MultiParentClassLoader;
import org.apache.xbean.classloader.NamedClassLoader;


/**
 * @version $Revision$ $Date$
 */
public class IsolatingClassLoader extends MultiParentClassLoader
{
    private final static ThreadLocal inside = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return Boolean.FALSE;
        }
    };
    private final ClassLoader internal;

    public IsolatingClassLoader(String name, URL[] urls, ClassLoader parent, ClassLoader ancestor)
    {
        super(name, urls, new ClassLoader[]{new InternalClassLoader(name + ".Internal",
                                                                    urls,
                                                                    (parent instanceof IsolatingClassLoader ? ((IsolatingClassLoader) parent).getInternal() : parent),
                                                                    ancestor),
                                            ancestor});

        internal = getParents()[0];
    }

    protected ClassLoader getInternal()
    {
        return internal;
    }

    private static class InternalClassLoader extends NamedClassLoader
    {
        private final static ThreadLocal inside = new ThreadLocal()
        {
            protected Object initialValue()
            {
                return Boolean.FALSE;
            }
        };
        private ClassLoader parent;
        private ClassLoader ancestor;

        /**
         * Creates a named class loader as a child of the specified parents.
         *
         * @param name     the name of this class loader
         * @param urls     the urls from which this class loader will classes and resources
         * @param parent   the parent of this class loader
         * @param ancestor the shared ancestor of this class loader
         */
        public InternalClassLoader(String name, URL[] urls, ClassLoader parent, ClassLoader ancestor)
        {
            super(name, urls, parent);
            this.parent = parent;
            this.ancestor = ancestor;
        }

        /**
         * Creates a named class loader as a child of the specified parents and using the specified URLStreamHandlerFactory
         * for accessing the urls..
         *
         * @param name     the name of this class loader
         * @param urls     the urls from which this class loader will classes and resources
         * @param parent   the parent of this class loader
         * @param ancestor the shared ancestor of this class loader
         * @param factory  the URLStreamHandlerFactory used to access the urls
         */
        public InternalClassLoader(String name, URL[] urls, ClassLoader parent, ClassLoader ancestor, URLStreamHandlerFactory factory)
        {
            super(name, urls, parent, factory);
            this.parent = parent;
            this.ancestor = ancestor;
        }

        /**
         * {@inheritDoc}
         */
        protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
        {
            //
            // Check if class is in the loaded classes cache
            //
            Class cachedClass = findLoadedClass(name);
            if (cachedClass != null)
            {
                return resolveClass(cachedClass, resolve);
            }

            try
            {
                Class clazz = parent.loadClass(name);

                return resolveClass(clazz, resolve);
            }
            catch (ClassNotFoundException ignored)
            {
            }

            if (!isDestroyed())
            {
                Object saved = inside.get();
                inside.set(Boolean.TRUE);
                try
                {
                    Class clazz = findClass(name);
                    return resolveClass(clazz, resolve);
                }
                catch (ClassNotFoundException ignored)
                {
                }
                finally
                {
                    inside.set(saved);
                }
            }

            if (inside.get() == Boolean.TRUE)
            {
                try
                {
                    Class clazz = ancestor.loadClass(name);

                    return resolveClass(clazz, resolve);
                }
                catch (ClassNotFoundException ignored)
                {
                }
            }

            throw new ClassNotFoundException(name + " in classloader " + name);
        }

        /**
         * {@inheritDoc}
         */
        public URL getResource(String name)
        {
            if (isDestroyed()) return null;

            URL url = parent.getResource(name);
            if (url != null) return url;

            Object saved = inside.get();
            inside.set(Boolean.TRUE);
            try
            {
                url = findResource(name);
                if (url != null) return url;
            }
            finally
            {
                inside.set(saved);
            }

            if (inside.get() == Boolean.TRUE)
            {
                url = ancestor.getResource(name);
                if (url != null) return url;
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        public Enumeration findResources(String name) throws IOException
        {
            if (isDestroyed()) return Collections.enumeration(Collections.EMPTY_SET);

            List resources = new ArrayList();

            List parentResources = Collections.list(parent.getResources(name));
            resources.addAll(parentResources);

            Object saved = inside.get();
            inside.set(Boolean.TRUE);
            try
            {
                List myResources = Collections.list(super.findResources(name));
                resources.addAll(myResources);
            }
            finally
            {
                inside.set(saved);
            }

            parentResources = Collections.list(ancestor.getResources(name));
            resources.addAll(parentResources);

            return Collections.enumeration(resources);
        }

        /**
         * {@inheritDoc}
         */
        public String toString()
        {
            return "[" + getClass().getName() + ":" +
                   " name=" + getName() +
                   " urls=" + Arrays.asList(getURLs()) +
                   " parent=" + parent +
                   " ancestor=" + ancestor +
                   "]";
        }

        private Class resolveClass(Class clazz, boolean resolve)
        {
            if (resolve)
            {
                resolveClass(clazz);
            }
            return clazz;
        }
    }
}