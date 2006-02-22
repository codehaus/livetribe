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

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

import org.livetribe.jmx.remote.MBeanServerConnectionForwarder;
import org.livetribe.jmx.util.I18NJMX;

/**
 * $Rev$
 */
public class I18NClientForwarder implements MBeanServerConnectionForwarder
{
    private MBeanServerConnection mbeanServerConnection;

    public void setMBeanServerConnection(MBeanServerConnection connection)
    {
        this.mbeanServerConnection = connection;
    }

    public MBeanServerConnection getMBeanServerConnection()
    {
        return mbeanServerConnection;
    }

    public ObjectInstance createMBean(String className, ObjectName name) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
    {
        return createMBean(className, name, null, null);
    }

    public ObjectInstance createMBean(String className, ObjectName name, Object params[], String signature[]) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().createMBean(className, mangled);
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
    {
        return createMBean(className, name, loaderName, null, null);
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Object params[], String signature[]) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().createMBean(className, mangled, loaderName, params, signature);
    }

    public void unregisterMBean(ObjectName name) throws InstanceNotFoundException, MBeanRegistrationException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().unregisterMBean(mangled);
    }

    public Object getAttribute(ObjectName name, String attribute) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().getAttribute(mangled, attribute);
    }

    public AttributeList getAttributes(ObjectName name, String[] attributes) throws InstanceNotFoundException, ReflectionException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().getAttributes(mangled, attributes);
    }

    public void setAttribute(ObjectName name, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().setAttribute(mangled, attribute);
    }

    public AttributeList setAttributes(ObjectName name, AttributeList attributes) throws InstanceNotFoundException, ReflectionException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().setAttributes(mangled, attributes);
    }

    public Object invoke(ObjectName name, String operationName, Object params[], String signature[]) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().invoke(mangled, operationName, params, signature);
    }

    public void addNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().addNotificationListener(mangled, listener, filter, handback);
    }

    public void addNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().addNotificationListener(mangled, listener, filter, handback);
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener) throws InstanceNotFoundException, ListenerNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().removeNotificationListener(mangled, listener);
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().removeNotificationListener(mangled, listener, filter, handback);
    }

    public void removeNotificationListener(ObjectName name, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().removeNotificationListener(mangled, listener);
    }

    public void removeNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException, IOException
    {
        ObjectName mangled = mangle(name);
        getMBeanServerConnection().removeNotificationListener(mangled, listener, filter, handback);
    }

    public MBeanInfo getMBeanInfo(ObjectName name) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException
    {
        ObjectName mangled = mangle(name);
        return getMBeanServerConnection().getMBeanInfo(mangled);
    }

    public boolean isInstanceOf(ObjectName name, String className) throws InstanceNotFoundException, IOException
    {
        return getMBeanServerConnection().isInstanceOf(name, className);
    }

    public ObjectInstance getObjectInstance(ObjectName name) throws InstanceNotFoundException, IOException
    {
        return getMBeanServerConnection().getObjectInstance(name);
    }

    public Set queryMBeans(ObjectName name, QueryExp query) throws IOException
    {
        return getMBeanServerConnection().queryMBeans(name, query);
    }

    public Set queryNames(ObjectName name, QueryExp query) throws IOException
    {
        return getMBeanServerConnection().queryNames(name, query);
    }

    public boolean isRegistered(ObjectName name) throws IOException
    {
        return getMBeanServerConnection().isRegistered(name);
    }

    public Integer getMBeanCount() throws IOException
    {
        return getMBeanServerConnection().getMBeanCount();
    }

    public String getDefaultDomain() throws IOException
    {
        return getMBeanServerConnection().getDefaultDomain();
    }

    public String[] getDomains() throws IOException
    {
        return getMBeanServerConnection().getDomains();
    }

    private ObjectName mangle(ObjectName objectName)
    {
        if (objectName == null) return null;
        Locale locale = I18NJMX.getLocale();
        if (locale == null) return objectName;
        Hashtable properties = new Hashtable(objectName.getKeyPropertyList());
        properties.put(I18NJMX.OBJECT_NAME_PROPERTY, locale.toString());
        return createObjectName(objectName.getDomain(), properties);
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
}
