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
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.StandardMBean;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;

/**
 * @version $Rev$ $Date$
 */
public class Gate extends StandardMBean implements GateMBean
{
    private final MountPoint mountPoint;
    private final Map environment;
    private boolean enabled;
    private JMXConnector jmxConnector;

    public Gate(MountPoint mountPoint, Map environment) throws NotCompliantMBeanException
    {
        super(GateMBean.class);
        this.mountPoint = mountPoint;
        this.environment = environment;
    }

    public void connect() throws IOException
    {
        jmxConnector = JMXConnectorFactory.connect(mountPoint.getJMXServiceURL(), environment);
        setEnabled(true);
    }

    public void close() throws IOException
    {
        if (jmxConnector != null) jmxConnector.close();
    }

    public void addNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object callback)
            throws IOException, InstanceNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        jmxConnection.addNotificationListener(objectName, listener, filter, callback);
    }

    public void removeNotificationListener(ObjectName objectName, NotificationListener listener)
            throws IOException, ListenerNotFoundException, InstanceNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        jmxConnection.removeNotificationListener(objectName, listener);
    }

    public void removeNotificationListener(ObjectName objectName, NotificationListener listener, NotificationFilter filter, Object callback)
            throws IOException, ListenerNotFoundException, InstanceNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        jmxConnection.removeNotificationListener(objectName, listener, filter, callback);
    }

    public MBeanInfo getMBeanInfo(ObjectName objectName)
            throws IOException, ReflectionException, InstanceNotFoundException, IntrospectionException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.getMBeanInfo(objectName);
    }

    public Object getAttribute(ObjectName objectName, String attributeName)
            throws IOException, ReflectionException, InstanceNotFoundException, MBeanException, AttributeNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.getAttribute(objectName, attributeName);
    }

    public void setAttribute(ObjectName objectName, Attribute attribute)
            throws IOException, ReflectionException, InstanceNotFoundException, MBeanException, AttributeNotFoundException, InvalidAttributeValueException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        jmxConnection.setAttribute(objectName, attribute);
    }

    public Object invoke(ObjectName objectName, String operationName, Object[] arguments, String[] signature)
            throws IOException, ReflectionException, InstanceNotFoundException, MBeanException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.invoke(objectName, operationName, arguments, signature);
    }

    public boolean isRegistered(ObjectName objectName)
            throws IOException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.isRegistered(objectName);
    }

    public boolean isInstanceOf(ObjectName objectName, String className)
            throws IOException, InstanceNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.isInstanceOf(objectName, className);
    }

    public ObjectInstance getObjectInstance(ObjectName objectName)
            throws IOException, InstanceNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.getObjectInstance(objectName);
    }

    public void unregisterMBean(ObjectName objectName)
            throws IOException, MBeanRegistrationException, InstanceNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        jmxConnection.unregisterMBean(objectName);
    }

    public Set queryMBeans(ObjectName pattern, QueryExp expression)
            throws IOException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.queryMBeans(pattern, expression);
    }

    public Set queryNames(ObjectName pattern, QueryExp expression)
            throws IOException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.queryNames(pattern, expression);
    }

    public String getName()
    {
        return mountPoint.getName();
    }

    public void setEnabled(boolean value)
    {
        this.enabled = value;
    }

    public MountPoint getMountPoint()
    {
        return mountPoint;
    }
}
