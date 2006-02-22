/*
 * Copyright 2005 the original author or authors
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
package org.livetribe.jmx.remote.ltw;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;
import java.io.ObjectInputStream;
import javax.management.remote.MBeanServerForwarder;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanException;
import javax.management.NotCompliantMBeanException;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.QueryExp;
import javax.management.AttributeNotFoundException;
import javax.management.AttributeList;
import javax.management.Attribute;
import javax.management.InvalidAttributeValueException;
import javax.management.OperationsException;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanInfo;
import javax.management.IntrospectionException;
import javax.management.loading.ClassLoaderRepository;

import org.livetribe.jmx.util.I18NJMX;

/**
 * $Rev$
 */
public class I18NServerForwarder implements MBeanServerForwarder
{
    private final MBeanServerForwarder forwarder;
    private MBeanServer mbeanServer;

    public I18NServerForwarder()
    {
        forwarder = null;
    }

    public I18NServerForwarder(MBeanServerForwarder forwarder)
    {
        this.forwarder = forwarder;
    }

    public MBeanServer getMBeanServer()
    {
        return forwarder == null ? mbeanServer : forwarder.getMBeanServer();
    }

    public void setMBeanServer(MBeanServer mbs)
    {
        if (forwarder == null)
            mbeanServer = mbs;
        else
            forwarder.setMBeanServer(mbs);
    }

    public ObjectInstance createMBean(String className, ObjectName name) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return createMBean(className, name, null, null);
    }

    public ObjectInstance createMBean(String className, ObjectName name, Object params[], String signature[]) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().createMBean(className, holder.objectName, params, signature);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return createMBean(className, name, loaderName, null, null);
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Object params[], String signature[]) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().createMBean(className, holder.objectName, loaderName, params, signature);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public ObjectInstance registerMBean(Object object, ObjectName name) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().registerMBean(object, holder.objectName);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void unregisterMBean(ObjectName name) throws InstanceNotFoundException, MBeanRegistrationException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().unregisterMBean(holder.objectName);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public Object getAttribute(ObjectName name, String attribute) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().getAttribute(holder.objectName, attribute);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public AttributeList getAttributes(ObjectName name, String[] attributes) throws InstanceNotFoundException, ReflectionException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().getAttributes(holder.objectName, attributes);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void setAttribute(ObjectName name, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().setAttribute(holder.objectName, attribute);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public AttributeList setAttributes(ObjectName name, AttributeList attributes) throws InstanceNotFoundException, ReflectionException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().setAttributes(holder.objectName, attributes);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public Object invoke(ObjectName name, String operationName, Object params[], String signature[]) throws InstanceNotFoundException, MBeanException, ReflectionException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().invoke(holder.objectName, operationName, params, signature);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void addNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().addNotificationListener(holder.objectName, listener, filter, handback);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void addNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().addNotificationListener(holder.objectName, listener, filter, handback);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener) throws InstanceNotFoundException, ListenerNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().removeNotificationListener(holder.objectName, listener);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().removeNotificationListener(holder.objectName, listener, filter, handback);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void removeNotificationListener(ObjectName name, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().removeNotificationListener(holder.objectName, listener);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public void removeNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            getMBeanServer().removeNotificationListener(holder.objectName, listener, filter, handback);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public MBeanInfo getMBeanInfo(ObjectName name) throws InstanceNotFoundException, IntrospectionException, ReflectionException
    {
        I18NHolder holder = unmangle(name);
        try
        {
            I18NJMX.setLocale(holder.locale);
            return getMBeanServer().getMBeanInfo(holder.objectName);
        }
        finally
        {
            I18NJMX.setLocale(null);
        }
    }

    public Object instantiate(String className) throws ReflectionException, MBeanException
    {
        return getMBeanServer().instantiate(className);
    }

    public Object instantiate(String className, ObjectName loaderName) throws ReflectionException, MBeanException, InstanceNotFoundException
    {
        return getMBeanServer().instantiate(className, loaderName);
    }

    public Object instantiate(String className, Object params[], String signature[]) throws ReflectionException, MBeanException
    {
        return getMBeanServer().instantiate(className, params, signature);
    }

    public Object instantiate(String className, ObjectName loaderName, Object params[], String signature[]) throws ReflectionException, MBeanException, InstanceNotFoundException
    {
        return getMBeanServer().instantiate(className, loaderName, params, signature);
    }

    public ObjectInputStream deserialize(ObjectName name, byte[] data) throws InstanceNotFoundException, OperationsException
    {
        return getMBeanServer().deserialize(name, data);
    }

    public ObjectInputStream deserialize(String className, byte[] data) throws OperationsException, ReflectionException
    {
        return getMBeanServer().deserialize(className, data);
    }

    public ObjectInputStream deserialize(String className, ObjectName loaderName, byte[] data) throws InstanceNotFoundException, OperationsException, ReflectionException
    {
        return getMBeanServer().deserialize(className, loaderName, data);
    }

    public ClassLoader getClassLoaderFor(ObjectName mbeanName) throws InstanceNotFoundException
    {
        return getMBeanServer().getClassLoaderFor(mbeanName);
    }

    public ClassLoader getClassLoader(ObjectName loaderName) throws InstanceNotFoundException
    {
        return getMBeanServer().getClassLoader(loaderName);
    }

    public ClassLoaderRepository getClassLoaderRepository()
    {
        return getMBeanServer().getClassLoaderRepository();
    }

    public boolean isInstanceOf(ObjectName name, String className) throws InstanceNotFoundException
    {
        return getMBeanServer().isInstanceOf(name, className);
    }

    public ObjectInstance getObjectInstance(ObjectName name) throws InstanceNotFoundException
    {
        return getMBeanServer().getObjectInstance(name);
    }

    public Set queryMBeans(ObjectName name, QueryExp query)
    {
        return getMBeanServer().queryMBeans(name, query);
    }

    public Set queryNames(ObjectName name, QueryExp query)
    {
        return getMBeanServer().queryNames(name, query);
    }

    public boolean isRegistered(ObjectName name)
    {
        return getMBeanServer().isRegistered(name);
    }

    public Integer getMBeanCount()
    {
        return getMBeanServer().getMBeanCount();
    }

    public String getDefaultDomain()
    {
        return getMBeanServer().getDefaultDomain();
    }

    public String[] getDomains()
    {
        return getMBeanServer().getDomains();
    }

    private I18NHolder unmangle(ObjectName objectName)
    {
        if (objectName == null) return null;
        I18NHolder holder = new I18NHolder();
        String localeString = objectName.getKeyProperty(I18NJMX.OBJECT_NAME_PROPERTY);
        if (localeString == null)
        {
            holder.locale = null;
            holder.objectName = objectName;
        }
        else
        {
            holder.locale = I18NJMX.convert(localeString);
            Hashtable properties = new Hashtable(objectName.getKeyPropertyList());
            properties.remove(I18NJMX.OBJECT_NAME_PROPERTY);
            holder.objectName = createObjectName(objectName.getDomain(), properties);
        }
        return holder;
    }

    private ObjectName createObjectName(String domain, Hashtable properties)
    {
        try
        {
            return ObjectName.getInstance(domain, properties);
        }
        catch (MalformedObjectNameException x)
        {
            throw new IllegalArgumentException(x);
        }
    }

    private static class I18NHolder
    {
        private ObjectName objectName;
        private Locale locale;
    }
}
