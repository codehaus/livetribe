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
package org.livetribe.forma.console.browser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.ServiceType;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.api.Configuration;
import org.livetribe.slp.api.sa.ServiceInfo;
import org.livetribe.slp.api.ua.StandardUserAgent;
import org.livetribe.slp.api.ua.UserAgent;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkModel
{
    private UserAgent userAgent;

    @PostConstruct
    private void initController() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setPort(1427);
        userAgent = new StandardUserAgent();
        userAgent.setConfiguration(configuration);
        userAgent.start();
    }

    public List<ServiceInfo> findServices(String serviceType) throws IOException, ServiceLocationException
    {
        List<ServiceInfo> result = new ArrayList<ServiceInfo>();

        List<ServiceURL> serviceURLs = userAgent.findServices(new ServiceType(serviceType), null, null, null);
        for (ServiceURL serviceURL : serviceURLs)
        {
            ServiceInfo service = new ServiceInfo(serviceURL, null, null, null);
            result.add(service);
        }

        return result;
    }
}
