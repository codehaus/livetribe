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

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class GatewayTest extends TestCase
{
    public void testClientToServerToRemote() throws Exception
    {
        MBeanServer remoteMBeanServer = MBeanServerFactory.newMBeanServer();
        JMXServiceURL remoteJMXServiceURL = new JMXServiceURL("rmi", null, 0);
        JMXConnectorServer remoteConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(remoteJMXServiceURL, null, remoteMBeanServer);
        remoteConnectorServer.start();
        remoteJMXServiceURL = remoteConnectorServer.getAddress();
        ObjectName delegateObjectName = ObjectName.getInstance("JMImplementation:type=MBeanServerDelegate");
        String attributeName = "MBeanServerId";
        String mbeanServerId = (String)remoteMBeanServer.getAttribute(delegateObjectName, attributeName);

        try
        {
            MBeanServer gatewayMBeanServer = MBeanServerFactory.newMBeanServer();
            Gateway gateway = new Gateway();
            ObjectName gatewayObjectName = ObjectName.getInstance(":type=Gateway");
            gatewayMBeanServer.registerMBean(gateway, gatewayObjectName);
            MountPoint mountPoint = new MountPoint(remoteJMXServiceURL.toString());
            mountPoint.setName("remote1");
            ObjectName gateObjectName = gateway.mount(mountPoint);

            try
            {
                JMXServiceURL gatewayJMXServiceURL = new JMXServiceURL("rmi", null, 0);
                JMXConnectorServer gatewayConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(gatewayJMXServiceURL, null, gatewayMBeanServer);
                gatewayConnectorServer.setMBeanServerForwarder(new GatewayForwarder(gateway));
                gatewayConnectorServer.start();
                gatewayJMXServiceURL = gatewayConnectorServer.getAddress();

                try
                {
                    JMXConnector jmxConnector = JMXConnectorFactory.connect(gatewayJMXServiceURL);

                    try
                    {
                        // Perform invocation directly using the gate
                        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
                        GateMBean gateProxy = (GateMBean)MBeanServerInvocationHandler.newProxyInstance(jmxConnection, gateObjectName, GateMBean.class, false);
                        String remoteMBeanServerId1 = (String)gateProxy.getAttribute(delegateObjectName, attributeName);
                        assertEquals(mbeanServerId, remoteMBeanServerId1);

                        // Perform the invocation transparently
                        ObjectName remoteDelegateObjectName = ObjectName.getInstance(mountPoint.getName() + "/" + delegateObjectName);
                        String remoteMBeanServerId2 = (String)jmxConnection.getAttribute(remoteDelegateObjectName, attributeName);
                        assertEquals(mbeanServerId, remoteMBeanServerId2);
                    }
                    finally
                    {
                        jmxConnector.close();
                    }
                }
                finally
                {
                    gatewayConnectorServer.stop();
                }
            }
            finally
            {
                gatewayMBeanServer.invoke(gateObjectName, "close", null, null);
            }
        }
        finally
        {
            remoteConnectorServer.stop();
        }
    }

    public void testClientToServer1ToServer2ToRemote() throws Exception
    {
        MBeanServer remoteMBeanServer = MBeanServerFactory.newMBeanServer();
        JMXServiceURL remoteJMXServiceURL = new JMXServiceURL("rmi", null, 0);
        JMXConnectorServer remoteConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(remoteJMXServiceURL, null, remoteMBeanServer);
        remoteConnectorServer.start();
        remoteJMXServiceURL = remoteConnectorServer.getAddress();
        ObjectName delegateObjectName = ObjectName.getInstance("JMImplementation:type=MBeanServerDelegate");
        String attributeName = "MBeanServerId";
        String mbeanServerId = (String)remoteMBeanServer.getAttribute(delegateObjectName, attributeName);

        try
        {
            MBeanServer gatewayMBeanServer1 = MBeanServerFactory.newMBeanServer();
            Gateway gateway1 = new Gateway();
            ObjectName gatewayObjectName1 = ObjectName.getInstance(":type=Gateway");
            gatewayMBeanServer1.registerMBean(gateway1, gatewayObjectName1);
            MountPoint mountPoint1 = new MountPoint(remoteJMXServiceURL.toString());
            mountPoint1.setName("remote1");
            ObjectName gateObjectName1 = gateway1.mount(mountPoint1);

            try
            {
                JMXServiceURL gatewayJMXServiceURL1 = new JMXServiceURL("rmi", null, 0);
                JMXConnectorServer gatewayConnectorServer1 = JMXConnectorServerFactory.newJMXConnectorServer(gatewayJMXServiceURL1, null, gatewayMBeanServer1);
                gatewayConnectorServer1.setMBeanServerForwarder(new GatewayForwarder(gateway1));
                gatewayConnectorServer1.start();
                gatewayJMXServiceURL1 = gatewayConnectorServer1.getAddress();

                try
                {
                    MBeanServer gatewayMBeanServer2 = MBeanServerFactory.newMBeanServer();
                    Gateway gateway2 = new Gateway();
                    ObjectName gatewayObjectName2 = ObjectName.getInstance(":type=Gateway");
                    gatewayMBeanServer2.registerMBean(gateway2, gatewayObjectName2);
                    MountPoint mountPoint2 = new MountPoint(gatewayJMXServiceURL1.toString());
                    mountPoint2.setName("remote2");
                    ObjectName gateObjectName2 = gateway2.mount(mountPoint2);

                    try
                    {
                        JMXServiceURL gatewayJMXServiceURL2 = new JMXServiceURL("rmi", null, 0);
                        JMXConnectorServer gatewayConnectorServer2 = JMXConnectorServerFactory.newJMXConnectorServer(gatewayJMXServiceURL2, null, gatewayMBeanServer2);
                        gatewayConnectorServer2.setMBeanServerForwarder(new GatewayForwarder(gateway2));
                        gatewayConnectorServer2.start();
                        gatewayJMXServiceURL2 = gatewayConnectorServer2.getAddress();

                        try
                        {
                            JMXConnector jmxConnector = JMXConnectorFactory.connect(gatewayJMXServiceURL2);

                            try
                            {
                                // Perform invocation directly using the gates
                                MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();

                                // First style of invocation: nested invoke() calls
                                String remoteMBeanServerId0 = (String)jmxConnection.invoke(gateObjectName2,
                                        "invoke",
                                        new Object[]{gateObjectName1, "getAttribute", new Object[]{delegateObjectName, attributeName}, new String[]{ObjectName.class.getName(), String.class.getName()}},
                                        new String[]{ObjectName.class.getName(), String.class.getName(), Object[].class.getName(), String[].class.getName()});
                                assertEquals(mbeanServerId, remoteMBeanServerId0);

                                // Alternative to the nested invoke() calls, using the Gateway MBean
                                ObjectName gatewayObjectName = ObjectName.getInstance(":type=Gateway");
                                GatewayMBean gatewayProxy = (GatewayMBean)MBeanServerInvocationHandler.newProxyInstance(jmxConnection, gatewayObjectName, GatewayMBean.class, false);
                                ObjectName gateNameOnHost2 = gatewayProxy.getGateObjectName(mountPoint2.getName());
                                GateMBean gateProxyOnHost2 = (GateMBean)MBeanServerInvocationHandler.newProxyInstance(jmxConnection, gateNameOnHost2, GateMBean.class, false);
                                ObjectName gateNameOnHost1 = (ObjectName)gateProxyOnHost2.invoke(gatewayObjectName, "getGateObjectName", new Object[]{mountPoint1.getName()}, new String[]{String.class.getName()});
                                String remoteMBeanServerId1 = (String)gateProxyOnHost2.invoke(gateNameOnHost1, "getAttribute", new Object[]{delegateObjectName, attributeName}, new String[]{ObjectName.class.getName(), String.class.getName()});
                                assertEquals(mbeanServerId, remoteMBeanServerId1);

                                // Second style of invocation: use routed ObjectNames
                                ObjectName remoteDelegateObjectName = ObjectName.getInstance(mountPoint2.getName() + "/" + mountPoint1.getName() + "/" + delegateObjectName);
                                String remoteMBeanServerId2 = (String)jmxConnection.getAttribute(remoteDelegateObjectName, attributeName);
                                assertEquals(mbeanServerId, remoteMBeanServerId2);
                            }
                            finally
                            {
                                jmxConnector.close();
                            }
                        }
                        finally
                        {
                            gatewayConnectorServer2.stop();
                        }
                    }
                    finally
                    {
                        gatewayMBeanServer2.invoke(gateObjectName2, "close", null, null);
                    }
                }
                finally
                {
                    gatewayConnectorServer1.stop();
                }
            }
            finally
            {
                gatewayMBeanServer1.invoke(gateObjectName1, "close", null, null);
            }
        }
        finally
        {
            remoteConnectorServer.stop();
        }
    }
/*
    // TODO
    public void testNotificationsRemoteToServerToClient() throws Exception
    {
        MBeanServer remoteMBeanServer = MBeanServerFactory.newMBeanServer();
        JMXServiceURL remoteJMXServiceURL = new JMXServiceURL("rmi", null, 0);
        JMXConnectorServer remoteConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(remoteJMXServiceURL, null, remoteMBeanServer);
        remoteConnectorServer.start();
        remoteJMXServiceURL = remoteConnectorServer.getAddress();
        ObjectName delegateObjectName = ObjectName.getInstance("JMImplementation:type=MBeanServerDelegate");
        String attributeName = "MBeanServerId";
        String mbeanServerId = (String)remoteMBeanServer.getAttribute(delegateObjectName, attributeName);

        try
        {
            MBeanServer gatewayMBeanServer = MBeanServerFactory.newMBeanServer();
            Gateway gateway = new Gateway();
            ObjectName gatewayObjectName = ObjectName.getInstance(":type=Gateway");
            gatewayMBeanServer.registerMBean(gateway, gatewayObjectName);
            MountPoint mountPoint = new MountPoint(remoteJMXServiceURL.toString());
            mountPoint.setName("remote1");
            ObjectName gateObjectName = gateway.mount(mountPoint);

            try
            {
                JMXServiceURL gatewayJMXServiceURL = new JMXServiceURL("rmi", null, 0);
                JMXConnectorServer gatewayConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(gatewayJMXServiceURL, null, gatewayMBeanServer);
                gatewayConnectorServer.setMBeanServerForwarder(new GatewayForwarder(gateway));
                gatewayConnectorServer.start();
                gatewayJMXServiceURL = gatewayConnectorServer.getAddress();

                try
                {
                    JMXConnector jmxConnector = JMXConnectorFactory.connect(gatewayJMXServiceURL);

                    try
                    {
                        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
                        ObjectName remoteDelegateObjectName = ObjectName.getInstance(mountPoint.getName() + "/" + delegateObjectName);

                        final AtomicReference notificationRef = new AtomicReference(null);
                        NotificationListener listener = new NotificationListener()
                        {
                            public void handleNotification(Notification notification, Object obj)
                            {
                                notificationRef.set(notification);
                            }
                        };
                        jmxConnection.addNotificationListener(remoteDelegateObjectName, listener, null, null);

                        // Register a new MBean, so that the delegate emits a notification
                        ObjectName objectName = ObjectName.getInstance("domain:type=MLet");
                        remoteMBeanServer.registerMBean(new MLet(), objectName);

                        Thread.sleep(500);

                        assertNotNull(notificationRef.get());
                        assertTrue(notificationRef.get() instanceof MBeanServerNotification);
                        MBeanServerNotification received = (MBeanServerNotification)notificationRef.get();
                        assertEquals(remoteDelegateObjectName, received.getSource());
                        assertEquals(mountPoint.getName() + "/" + objectName, received.getMBeanName().toString());
                    }
                    finally
                    {
                        jmxConnector.close();
                    }
                }
                finally
                {
                    gatewayConnectorServer.stop();
                }
            }
            finally
            {
                gatewayMBeanServer.invoke(gateObjectName, "close", null, null);
            }
        }
        finally
        {
            remoteConnectorServer.stop();
        }
    }
*/
}
