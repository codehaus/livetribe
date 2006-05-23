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
package org.livetribe.agent;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.api.sa.ServiceAgent;
import org.livetribe.slp.api.sa.ServiceInfo;

/**
 * @version $Rev$ $Date$
 */
public class Agent
{
    private ServiceAgent serviceAgent;
    private JMXConnectorServer jmxConnectorServer;
    private volatile boolean running;

    public void setServiceAgent(ServiceAgent serviceAgent)
    {
        this.serviceAgent = serviceAgent;
    }

    public void setJMXConnectorServer(JMXConnectorServer jmxConnectorServer)
    {
        this.jmxConnectorServer = jmxConnectorServer;
    }

    public void start() throws Exception
    {
        if (jmxConnectorServer == null) jmxConnectorServer = createJMXConnectorServer();
        jmxConnectorServer.start();

        JMXServiceURL jmxServiceURL = jmxConnectorServer.getAddress();
        ObjectName delegateObjectName = ObjectName.getInstance("JMImplementation:type=MBeanServerDelegate");
        String mbeanServerId = (String)jmxConnectorServer.getMBeanServer().getAttribute(delegateObjectName, "MBeanServerId");
        InetAddress localhost = InetAddress.getLocalHost();
        Attributes attributes = new Attributes();
        attributes.put("mbeanServerId", mbeanServerId);
        attributes.put("hostAddress", localhost.getHostAddress());
        attributes.put("agentName", toString());
        String[] scopes = new String[]{"agent"};

        ServiceURL serviceURL = new ServiceURL(jmxServiceURL.toString(), ServiceURL.LIFETIME_DEFAULT);
        ServiceInfo serviceInfo = new ServiceInfo(serviceURL, scopes, attributes, Locale.getDefault().getLanguage());
        serviceAgent.register(serviceInfo);
        serviceAgent.start();

        running = true;
    }

    public void stop() throws Exception
    {
        running = false;

        serviceAgent.stop();

        jmxConnectorServer.stop();
    }

    public boolean isRunning()
    {
        return running;
    }

    protected JMXConnectorServer createJMXConnectorServer() throws IOException
    {
        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();
        JMXServiceURL jmxServiceURL = new JMXServiceURL("rmi", null, 0);
        return JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mbeanServer);
    }
}
