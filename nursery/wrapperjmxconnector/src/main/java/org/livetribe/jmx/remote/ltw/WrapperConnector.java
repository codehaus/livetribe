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
package org.livetribe.jmx.remote.ltw;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;

/**
 * $Rev$ $Date$
 */
public class WrapperConnector implements JMXConnector
{
    private final JMXServiceURL serviceURL;
    private final Map environment;
    private JMXConnector wrappedJMXConnector;
    private List connections;

    public WrapperConnector(JMXServiceURL serviceURL, Map environment)
    {
        this.serviceURL = serviceURL;
        this.environment = environment;
    }

    public void connect() throws IOException
    {
        connect(null);
    }

    public void connect(Map env) throws IOException
    {
        connections = WrapperConnectorUtils.getClientForwarders(serviceURL, env, getClass().getClassLoader());
        JMXServiceURL wrappedJMXServiceURL = WrapperConnectorUtils.unwrap(serviceURL);
        wrappedJMXConnector = JMXConnectorFactory.newJMXConnector(wrappedJMXServiceURL, environment);
        wrappedJMXConnector.connect(env);
    }

    public MBeanServerConnection getMBeanServerConnection() throws IOException
    {
        return getMBeanServerConnection(null);
    }

    public MBeanServerConnection getMBeanServerConnection(Subject delegationSubject) throws IOException
    {
        if (wrappedJMXConnector == null)
            throw new IOException("Could not retrieve MBeanServerConnection, this connector is not connected");
        MBeanServerConnection wrapped = wrappedJMXConnector.getMBeanServerConnection(delegationSubject);
        WrapperMBeanServerConnection proxyHandler = new WrapperMBeanServerConnection(wrapped, delegationSubject, connections);
        Object proxy = Proxy.newProxyInstance(MBeanServerConnection.class.getClassLoader(), new Class[]{MBeanServerConnection.class}, proxyHandler);
        return (MBeanServerConnection)proxy;
    }

    public void close() throws IOException
    {
        if (wrappedJMXConnector != null) wrappedJMXConnector.close();
    }

    public void addConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
    {
        if (wrappedJMXConnector != null)
            wrappedJMXConnector.addConnectionNotificationListener(listener, filter, handback);
    }

    public void removeConnectionNotificationListener(NotificationListener listener) throws ListenerNotFoundException
    {
        if (wrappedJMXConnector != null) wrappedJMXConnector.removeConnectionNotificationListener(listener);
    }

    public void removeConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException
    {
        if (wrappedJMXConnector != null)
            wrappedJMXConnector.removeConnectionNotificationListener(listener, filter, handback);
    }

    public String getConnectionId() throws IOException
    {
        if (wrappedJMXConnector == null)
            throw new IOException("Could not retrieve connection id, this connector is not connected");
        return wrappedJMXConnector.getConnectionId();
    }
}
