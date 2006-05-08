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
import java.util.List;

import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.api.ua.UserAgent;
import org.livetribe.slp.api.ua.StandardUserAgent;
import org.livetribe.slp.api.Configuration;
import org.livetribe.slp.ServiceType;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.ServiceURL;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkController
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

    public List<ServiceURL> findServices(String serviceType) throws IOException, ServiceLocationException
    {
        return userAgent.findServices(new ServiceType(serviceType), null, null, null);
    }
}
