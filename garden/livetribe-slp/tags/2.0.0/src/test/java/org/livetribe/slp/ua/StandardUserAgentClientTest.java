/*
 * Copyright 2008-2008 the original author or authors
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
package org.livetribe.slp.ua;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.SLP;
import org.livetribe.slp.Scopes;
import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.da.StandardDirectoryAgentServer;
import org.livetribe.slp.sa.ServiceAgentClient;
import org.livetribe.slp.settings.Factories;
import static org.livetribe.slp.settings.Keys.*;
import org.livetribe.slp.settings.MapSettings;
import org.livetribe.slp.settings.Settings;
import org.livetribe.slp.spi.net.MessageEvent;
import org.livetribe.slp.spi.net.MessageListener;
import org.livetribe.slp.spi.net.UDPConnectorServer;
import org.testng.annotations.Test;

/**
 * @version $Revision$ $Date$
 */
public class StandardUserAgentClientTest
{
    private Settings newSettings()
    {
        Settings settings = new MapSettings();
        settings.put(PORT_KEY, 4427);
        return settings;
    }

    @Test
    public void testFindServicesWithKnownDirectoryAgent()
    {
        StandardDirectoryAgentServer da = StandardDirectoryAgentServer.newInstance(newSettings());
        da.start();

        try
        {
            ServiceURL serviceURL = new ServiceURL("service:jmx:rmi:///jndi/rmi:///test");
            Attributes attributes = Attributes.from("(attr=test)");
            ServiceInfo service = new ServiceInfo(serviceURL, Locale.ENGLISH.getLanguage(), Scopes.DEFAULT, attributes);
            ServiceAgentClient sa = SLP.newServiceAgentClient(newSettings());
            sa.register(service);

            final AtomicInteger messages = new AtomicInteger();
            Settings udpSettings = newSettings();
            UDPConnectorServer.Factory udpServerFactory = Factories.newInstance(udpSettings, UDP_CONNECTOR_SERVER_FACTORY_KEY);
            UDPConnectorServer udpConnectorServer = udpServerFactory.newUDPConnectorServer(udpSettings);
            udpConnectorServer.addMessageListener(new MessageListener()
            {
                public void handle(MessageEvent event)
                {
                    messages.incrementAndGet();
                }
            });
            udpConnectorServer.start();

            try
            {
                Settings uaSettings = newSettings();
                uaSettings.put(DA_ADDRESSES_KEY, new String[]{"127.0.0.1"});
                StandardUserAgentClient ua = StandardUserAgentClient.newInstance(uaSettings);
                ua.init();

                List<ServiceInfo> services = ua.findServices(serviceURL.getServiceType(), null, null, null);
                assert services.size() == 1;

                assert messages.get() == 0;
            }
            finally
            {
                udpConnectorServer.stop();
            }
        }
        finally
        {
            da.stop();
        }
    }
}
