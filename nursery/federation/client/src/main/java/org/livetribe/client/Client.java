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
package org.livetribe.client;

import java.util.List;
import java.util.ArrayList;

import org.livetribe.slp.api.ua.UserAgent;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.ServiceType;

/**
 * @version $Rev$ $Date$
 */
public class Client
{
    private UserAgent userAgent;

    public void setUserAgent(UserAgent userAgent)
    {
        this.userAgent = userAgent;
    }

    public void start() throws Exception
    {
        userAgent.start();
    }

    public void stop() throws Exception
    {
        userAgent.stop();
    }

    /**
     * Returns the JMXServiceURLs that connect to servers, found after discovering
     * the servers available for this client.
     */
    public List<ConsoleClient> discoverServers() throws Exception
    {
        ServiceType serviceType = new ServiceType("service:jmx");
        String[] consoleScope = new String[]{"console"};
        List<ServiceURL> serviceURLs = (List<ServiceURL>)userAgent.findServices(serviceType, consoleScope, null);
        List<ConsoleClient> result = new ArrayList<ConsoleClient>();
        for (ServiceURL serviceURL : serviceURLs)
        {
//            ConsoleClient client = ConsoleClient.Factory.newInstance(serviceURL);
//            result.add(client);
        }
        return result;
    }
}
