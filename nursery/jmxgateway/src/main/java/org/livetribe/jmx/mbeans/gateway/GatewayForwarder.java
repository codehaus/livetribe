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
package org.livetribe.jmx.mbeans.gateway;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Set;
import javax.management.remote.MBeanServerForwarder;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.ReflectionException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.OperationsException;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanInfo;
import javax.management.IntrospectionException;
import javax.management.Attribute;
import javax.management.InvalidAttributeValueException;
import javax.management.AttributeList;
import javax.management.QueryExp;
import javax.management.loading.ClassLoaderRepository;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @version $Rev$ $Date$
 */
public class GatewayForwarder implements MBeanServerForwarder
{
    private final Gateway gateway;
    private MBeanServer mbeanServer;

    public GatewayForwarder(Gateway gateway)
    {
        this.gateway = gateway;
    }

    public MBeanServer getMBeanServer()
    {
        return mbeanServer;
    }

    public void setMBeanServer(MBeanServer mbeanServer)
    {
        this.mbeanServer = mbeanServer;
    }

    public void addNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object obj) throws InstanceNotFoundException
    {
    }

    public void addNotificationListener(ObjectName objectName, ObjectName objectName1, NotificationFilter filter, Object obj) throws InstanceNotFoundException
    {
    }

    public void removeNotificationListener(ObjectName objectName, ObjectName objectName1) throws InstanceNotFoundException, ListenerNotFoundException
    {
    }

    public void removeNotificationListener(ObjectName objectName, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException
    {
    }

    public void removeNotificationListener(ObjectName objectName, ObjectName objectName1, NotificationFilter filter, Object obj) throws InstanceNotFoundException, ListenerNotFoundException
    {
    }

    public void removeNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object obj) throws InstanceNotFoundException, ListenerNotFoundException
    {
    }

    public MBeanInfo getMBeanInfo(ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException
    {
        return null;
    }

    public boolean isInstanceOf(ObjectName objectName, String string) throws InstanceNotFoundException
    {
        return false;
    }

    public ObjectInstance createMBean(String string, ObjectName objectName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return null;
    }

    public ObjectInstance createMBean(String string, ObjectName objectName, ObjectName objectName1) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return null;
    }

    public ObjectInstance createMBean(String string, ObjectName objectName, Object[] objects, String[] strings) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return null;
    }

    public ObjectInstance createMBean(String string, ObjectName objectName, ObjectName objectName1, Object[] objects, String[] strings) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return null;
    }

    public void unregisterMBean(ObjectName objectName) throws InstanceNotFoundException, MBeanRegistrationException
    {
    }

    public void setAttribute(ObjectName objectName, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
    }

    public Object getAttribute(ObjectName objectName, String string) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.getAttribute(translated.getTranslatedObjectName(), string);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        return mbeanServer.getAttribute(objectName, string);
    }

    public AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException
    {
        return null;
    }

    public AttributeList getAttributes(ObjectName objectName, String[] strings) throws InstanceNotFoundException, ReflectionException
    {
        return null;
    }

    public Object invoke(ObjectName objectName, String string, Object[] objects, String[] strings) throws InstanceNotFoundException, MBeanException, ReflectionException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.invoke(translated.getTranslatedObjectName(), string, objects, strings);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        return mbeanServer.invoke(objectName, string, objects, strings);
    }

    public boolean isRegistered(ObjectName objectName)
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) return false;

            try
            {
                return gate.isRegistered(translated.getTranslatedObjectName());
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        return mbeanServer.isRegistered(objectName);
    }

    public ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException
    {
        return null;
    }

    public Set queryMBeans(ObjectName objectName, QueryExp queryExp)
    {
        return null;
    }

    public Set queryNames(ObjectName objectName, QueryExp queryExp)
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) return Collections.emptySet();

            try
            {
                return gate.queryNames(translated.getTranslatedObjectName(), queryExp);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        return mbeanServer.queryNames(objectName, queryExp);
    }

    public String[] getDomains()
    {
        return mbeanServer.getDomains();
    }

    public String getDefaultDomain()
    {
        return mbeanServer.getDefaultDomain();
    }

    public Integer getMBeanCount()
    {
        return mbeanServer.getMBeanCount();
    }

    public Object instantiate(String string) throws ReflectionException, MBeanException
    {
        return mbeanServer.instantiate(string);
    }

    public Object instantiate(String string, ObjectName objectName) throws ReflectionException, MBeanException, InstanceNotFoundException
    {
        return mbeanServer.instantiate(string, objectName);
    }

    public Object instantiate(String string, Object[] objects, String[] strings) throws ReflectionException, MBeanException
    {
        return mbeanServer.instantiate(string, objects, strings);
    }

    public Object instantiate(String string, ObjectName objectName, Object[] objects, String[] strings) throws ReflectionException, MBeanException, InstanceNotFoundException
    {
        return mbeanServer.instantiate(string, objectName, objects, strings);
    }

    public ObjectInstance registerMBean(Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
    {
        return mbeanServer.registerMBean(obj, objectName);
    }

    public ObjectInputStream deserialize(String string, ObjectName objectName, byte[] bytes) throws InstanceNotFoundException, OperationsException, ReflectionException
    {
        return mbeanServer.deserialize(string, objectName, bytes);
    }

    public ObjectInputStream deserialize(String string, byte[] bytes) throws OperationsException, ReflectionException
    {
        return mbeanServer.deserialize(string, bytes);
    }

    public ObjectInputStream deserialize(ObjectName objectName, byte[] bytes) throws InstanceNotFoundException, OperationsException
    {
        return mbeanServer.deserialize(objectName, bytes);
    }

    public ClassLoader getClassLoaderFor(ObjectName objectName) throws InstanceNotFoundException
    {
        return mbeanServer.getClassLoaderFor(objectName);
    }

    public ClassLoader getClassLoader(ObjectName objectName) throws InstanceNotFoundException
    {
        return mbeanServer.getClassLoader(objectName);
    }

    public ClassLoaderRepository getClassLoaderRepository()
    {
        return mbeanServer.getClassLoaderRepository();
    }

    private TranslatedObjectName translateObjectName(ObjectName objectName)
    {
        if (TranslatedObjectName.isTranslatable(objectName))
        {
            return new TranslatedObjectName(objectName);
        }
        else
        {
            return null;
        }
    }

    private static class TranslatedObjectName
    {
        private static final String SEPARATOR = "/";
        private String translationPath;
        private ObjectName translatedObjectName;

        public TranslatedObjectName(ObjectName objectName)
        {
            String domain = objectName.getDomain();
            int separatorIndex = getSeparatorIndex(domain);
            translationPath = domain.substring(0, separatorIndex);
            translatedObjectName = translateObjectName(domain.substring(separatorIndex + SEPARATOR.length()), objectName.getKeyPropertyList());
        }

        public static boolean isTranslatable(ObjectName objectName)
        {
            return getSeparatorIndex(objectName.getDomain()) >= 0;
        }

        private static int getSeparatorIndex(String domain)
        {
            return domain.indexOf(SEPARATOR);
        }

        public String getTranslationPath()
        {
            return translationPath;
        }

        public ObjectName getTranslatedObjectName()
        {
            return translatedObjectName;
        }

        private ObjectName translateObjectName(String domain, Hashtable properties)
        {
            try
            {
                return ObjectName.getInstance(domain, properties);
            }
            catch (MalformedObjectNameException x)
            {
                throw new AssertionError(x);
            }
        }
    }
}
