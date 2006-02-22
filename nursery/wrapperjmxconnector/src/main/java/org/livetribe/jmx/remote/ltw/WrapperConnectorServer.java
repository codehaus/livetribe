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
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

/**
 * $Rev$ $Date$
 */
public class WrapperConnectorServer extends JMXConnectorServer
{
    public static final String CONFIG_RESOURCE_ATTRIBUTE = "jmx.remote.x.livetribe.wrapper.config";

    private JMXServiceURL address;
    private final Map environment;
    private JMXConnectorServer wrappedConnectorServer;

    public WrapperConnectorServer(JMXServiceURL address, Map environment, MBeanServer mbeanServer)
    {
        super(mbeanServer);
        this.address = address;
        this.environment = environment;
    }

    public void start() throws IOException
    {
        List forwarders = getForwarders(address, environment, getClass().getClassLoader());
        Object proxy = Proxy.newProxyInstance(MBeanServer.class.getClassLoader(), new Class[]{MBeanServer.class}, new WrapperMBeanServerForwarder(getMBeanServer(), forwarders));
        MBeanServer mbeanServer = (MBeanServer)proxy;

        JMXServiceURL wrappedServiceURL = WrapperConnectorUtils.unwrap(address);
        wrappedConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(wrappedServiceURL, environment, mbeanServer);
        wrappedConnectorServer.start();

        JMXServiceURL wrappedAddress = wrappedConnectorServer.getAddress();
        address = WrapperConnectorUtils.wrap(address, wrappedAddress);
    }

    public void stop() throws IOException
    {
        wrappedConnectorServer.stop();
    }

    public boolean isActive()
    {
        return wrappedConnectorServer.isActive();
    }

    public JMXServiceURL getAddress()
    {
        return address;
    }

    public Map getAttributes()
    {
        return wrappedConnectorServer.getAttributes();
    }
/*
    private Document createConfigDocument(InputStream configStream) throws IOException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(configStream);
        }
        catch (ParserConfigurationException x)
        {
            throw (IOException)new IOException().initCause(x);
        }
        catch (SAXException x)
        {
            throw (IOException)new IOException().initCause(x);
        }
    }

    private MBeanServerForwarder createWrappers(InputStream configStream)
    {
        Document configDocument = createConfigDocument(configStream);
        return new I18NServerForwarder();
    }
*/

    protected List getForwarders(JMXServiceURL jmxServiceURL, Map environment, ClassLoader loader) throws IOException
    {
        return WrapperConnectorUtils.getForwarders(jmxServiceURL, environment, loader);
    }
}
