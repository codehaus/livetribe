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
package org.livetribe.server;

import java.util.List;
import java.util.Locale;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.livetribe.jmx.mbeans.gateway.Gateway;
import org.livetribe.jmx.mbeans.gateway.GatewayForwarder;
import org.livetribe.jmx.mbeans.gateway.MountPoint;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.api.da.DirectoryAgent;
import org.livetribe.slp.api.da.ServiceRegistrationEvent;
import org.livetribe.slp.api.da.ServiceRegistrationListener;

/**
 * @version $Rev$ $Date$
 */
public class Server
{
    private volatile boolean running;
    private DirectoryAgent directoryAgent;
    private JMXConnectorServer jmxConnectorServer;
    private ServiceRegistrationListener serviceListener;
    private MBeanServer mbeanServer;
    private Gateway gateway;
    private ObjectName gatewayObjectName;

    public void setDirectoryAgent(DirectoryAgent directoryAgent)
    {
        this.directoryAgent = directoryAgent;
    }

    public void start() throws Exception
    {
        mbeanServer = MBeanServerFactory.createMBeanServer();
        gateway = new Gateway();
        gatewayObjectName = ObjectName.getInstance("org.livetribe.jmx.gateway:type=Gateway");
        mbeanServer.registerMBean(gateway, gatewayObjectName);

        JMXServiceURL jmxServiceURL = new JMXServiceURL("rmi", null, 0);
        jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mbeanServer);
        jmxConnectorServer.setMBeanServerForwarder(new GatewayForwarder(gateway));
        jmxConnectorServer.start();

        serviceListener = new ServiceListener();
        directoryAgent.addServiceRegistrationListener(serviceListener);
        directoryAgent.start();
        ServiceURL serviceURL = new ServiceURL(jmxConnectorServer.getAddress().toString(), ServiceURL.LIFETIME_DEFAULT);
        directoryAgent.registerService(null, serviceURL, new String[]{"console"}, null, Locale.getDefault().getLanguage(), false);

        running = true;
    }

    public void stop() throws Exception
    {
        running = false;

        directoryAgent.stop();
        directoryAgent.removeServiceRegistrationListener(serviceListener);

        jmxConnectorServer.stop();

        mbeanServer.unregisterMBean(gatewayObjectName);
        MBeanServerFactory.releaseMBeanServer(mbeanServer);
    }

    public boolean isRunning()
    {
        return running;
    }

    protected void mount(MountPoint mountPoint)
    {
        gateway.mount(mountPoint);
    }

    protected void unmount(MountPoint mountPoint)
    {
//        gateway.unmount(mountPoint);
    }

    public List getMounted()
    {
        return gateway.getMounted();
    }

    private class ServiceListener implements ServiceRegistrationListener
    {
        public void serviceRegistered(ServiceRegistrationEvent event)
        {
            try
            {
                ServiceURL serviceURL = event.getServiceURL();
                String jmxServiceURL = serviceURL.getURL();
                ObjectName pattern = ObjectName.getInstance("*:*");
                String agentName = (String)event.getAttributes().getValue("agentName");

                MountPoint mountPoint = new MountPoint(jmxServiceURL);
                mountPoint.setSourcePattern(pattern);
                mountPoint.setName(agentName);

                mount(mountPoint);
            }
            catch (MalformedObjectNameException x)
            {
                throw new AssertionError(x);
            }
        }

        public void serviceDeregistered(ServiceRegistrationEvent event)
        {
            ServiceURL serviceURL = event.getServiceURL();
            String jmxServiceURL = serviceURL.getURL();
            String agentName = (String)event.getAttributes().getValue("agentName");

            MountPoint mountPoint = new MountPoint(jmxServiceURL);
            mountPoint.setName(agentName);

            unmount(mountPoint);
        }
    }
}
