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
package org.livetribe.slp.demo.server;

import java.net.InetAddress;
import java.util.Locale;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.Scopes;
import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.api.sa.StandardServiceAgent;
import org.mortbay.component.Container;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.management.MBeanContainer;

/**
 * @version $Revision$ $Date$
 */
public class Service
{
    public static void main(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            printUsage();
            return;
        }

        Service service = new Service();
        service.setName(args[0]);
        service.setPort(Integer.parseInt(args[1]));
        service.start();
    }

    private static void printUsage()
    {
        System.out.println("Usage:");
        System.out.println();
        System.out.println("java " + Service.class.getName() + " <name> <port>");
    }

    private String name;
    private int port;

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void start() throws Exception
    {
        MBeanServer mbeanServer = MBeanServerFactory.newMBeanServer();

        MBeanContainer mbeanContainer = new MBeanContainer(mbeanServer);
        mbeanContainer.start();
        Server jetty = new Server();
        Container container = jetty.getContainer();
        container.addEventListener(mbeanContainer);
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        jetty.addConnector(connector);
        jetty.start();

        JMXServiceURL jmxServiceURL = new JMXServiceURL("rmi", null, 0);

        JMXConnectorServer connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mbeanServer);
        connectorServer.start();

        jmxServiceURL = connectorServer.getAddress();

        ServiceURL serviceURL = new ServiceURL(jmxServiceURL.toString(), 5);
        Scopes scopes = Scopes.DEFAULT;
        Attributes attributes = new Attributes();
        attributes.put("name", name);
        attributes.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
        ServiceInfo serviceInfo = new ServiceInfo(serviceURL, scopes, attributes, Locale.ENGLISH.getLanguage());

        StandardServiceAgent serviceAgent = new StandardServiceAgent();
        serviceAgent.setPort(1427);
        serviceAgent.register(serviceInfo);
        serviceAgent.start();

        System.out.println("Demo Service '" + name + "' up and running");
    }
}
