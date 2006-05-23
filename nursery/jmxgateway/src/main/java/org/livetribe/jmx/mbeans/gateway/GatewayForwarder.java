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
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import javax.management.remote.MBeanServerForwarder;

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

    public void addNotificationListener(ObjectName objectName, ObjectName listenerObjectName, NotificationFilter filter, Object callback) throws InstanceNotFoundException
    {
        // TODO: this is not easy: in general, it can be of the form:
        // TODO: addNotificationListener("path1/dom1:k=v", "path2/path3/dom2:k=v", null, null)
        // We can put the limit that routing paths must be equal
        throw new AssertionError("NYI");
    }

    public void addNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object callback) throws InstanceNotFoundException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                gate.addNotificationListener(translated.getTranslatedObjectName(), listener, filter, callback);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            mbeanServer.addNotificationListener(objectName, listener, filter, callback);
        }
    }

    public void removeNotificationListener(ObjectName objectName, ObjectName listenerObjectName) throws InstanceNotFoundException, ListenerNotFoundException
    {
        throw new AssertionError("NYI");
    }

    public void removeNotificationListener(ObjectName objectName, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                gate.removeNotificationListener(translated.getTranslatedObjectName(), listener);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            mbeanServer.removeNotificationListener(objectName, listener);
        }
    }

    public void removeNotificationListener(ObjectName objectName, ObjectName listenerObjectName, NotificationFilter filter, Object callback) throws InstanceNotFoundException, ListenerNotFoundException
    {
        throw new AssertionError("NYI");
    }

    public void removeNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object callback) throws InstanceNotFoundException, ListenerNotFoundException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                gate.removeNotificationListener(translated.getTranslatedObjectName(), listener, filter, callback);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            mbeanServer.removeNotificationListener(objectName, listener, filter, callback);
        }
    }

    public MBeanInfo getMBeanInfo(ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.getMBeanInfo(translated.getTranslatedObjectName());
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            return mbeanServer.getMBeanInfo(objectName);
        }
    }

    public boolean isInstanceOf(ObjectName objectName, String className) throws InstanceNotFoundException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.isInstanceOf(translated.getTranslatedObjectName(), className);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            return mbeanServer.isInstanceOf(objectName, className);
        }
    }

    public ObjectInstance createMBean(String string, ObjectName objectName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        throw new AssertionError("NYI");
    }

    public ObjectInstance createMBean(String string, ObjectName objectName, ObjectName objectName1) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        throw new AssertionError("NYI");
    }

    public ObjectInstance createMBean(String string, ObjectName objectName, Object[] objects, String[] strings) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        throw new AssertionError("NYI");
    }

    public ObjectInstance createMBean(String string, ObjectName objectName, ObjectName objectName1, Object[] objects, String[] strings) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        throw new AssertionError("NYI");
    }

    public void unregisterMBean(ObjectName objectName) throws InstanceNotFoundException, MBeanRegistrationException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                gate.unregisterMBean(translated.getTranslatedObjectName());
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            mbeanServer.unregisterMBean(objectName);
        }
    }

    public void setAttribute(ObjectName objectName, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                gate.setAttribute(translated.getTranslatedObjectName(), attribute);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            mbeanServer.setAttribute(objectName, attribute);
        }
    }

    public Object getAttribute(ObjectName objectName, String attributeName) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.getAttribute(translated.getTranslatedObjectName(), attributeName);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            return mbeanServer.getAttribute(objectName, attributeName);
        }
    }

    public AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException
    {
        throw new AssertionError("NYI");
    }

    public AttributeList getAttributes(ObjectName objectName, String[] strings) throws InstanceNotFoundException, ReflectionException
    {
        throw new AssertionError("NYI");
    }

    public Object invoke(ObjectName objectName, String operationName, Object[] arguments, String[] signature) throws InstanceNotFoundException, MBeanException, ReflectionException
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.invoke(translated.getTranslatedObjectName(), operationName, arguments, signature);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            return mbeanServer.invoke(objectName, operationName, arguments, signature);
        }
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
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) throw new InstanceNotFoundException(objectName.getCanonicalName());

            try
            {
                return gate.getObjectInstance(translated.getTranslatedObjectName());
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            return mbeanServer.getObjectInstance(objectName);
        }
    }

    public Set queryMBeans(ObjectName objectName, QueryExp queryExp)
    {
        TranslatedObjectName translated = translateObjectName(objectName);
        if (translated != null)
        {
            Gate gate = gateway.getGate(translated.getTranslationPath());
            if (gate == null) return Collections.emptySet();

            try
            {
                return gate.queryMBeans(translated.getTranslatedObjectName(), queryExp);
            }
            catch (IOException x)
            {
                throw new RuntimeIOException(x);
            }
        }
        else
        {
            return mbeanServer.queryMBeans(objectName, queryExp);
        }
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
        else
        {
            return mbeanServer.queryNames(objectName, queryExp);
        }
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
        if (TranslatedObjectName.isTranslatable(objectName))
        {
            // If the objectName refers to a remote MBean, it's not possible to serialize its classloader
            // from the remote location to here. The only hope is that this server has the class in its
            // classpath and so here the context classloader is returned instead.
            return Thread.currentThread().getContextClassLoader();
        }
        else
        {
            return mbeanServer.getClassLoaderFor(objectName);
        }
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
            if (objectName == null) return false;
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

    private static class TranslatorListener implements NotificationListener
    {
        private final NotificationListener listener;

        public TranslatorListener(NotificationListener listener)
        {
            this.listener = listener;
        }

        public void handleNotification(Notification notification, Object obj)
        {
            // TODO
//            Object source = notification.getSource();
//            Object newSource = GatewayTranslator.recursivelyTranslateObjectNames(source);
//            notification.setSource(newSource);
        }

        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final TranslatorListener that = (TranslatorListener)obj;
            return listener.equals(that.listener);
        }

        public int hashCode()
        {
            return listener.hashCode();
        }
    }
}
