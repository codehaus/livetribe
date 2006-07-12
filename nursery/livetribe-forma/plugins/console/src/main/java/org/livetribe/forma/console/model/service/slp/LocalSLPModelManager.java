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
package org.livetribe.forma.console.model.service.slp;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.remote.JMXServiceURL;

import org.livetribe.forma.console.model.service.JMXServiceNode;
import org.livetribe.forma.ui.configuration.ConfigurationManager;
import org.livetribe.ioc.Inject;
import org.livetribe.slp.ServiceType;
import org.livetribe.slp.api.Configuration;
import org.livetribe.slp.api.sa.ServiceInfo;
import org.livetribe.slp.api.ua.StandardUserAgent;
import org.livetribe.slp.api.ua.UserAgent;

/**
 * @version $Rev$ $Date$
 */
public class LocalSLPModelManager implements SLPModelManager
{
    private static final ServiceType JMX_SERVICE_TYPE = new ServiceType("service:jmx");

    @Inject
    private ConfigurationManager configurationManager;
    private UserAgent userAgent;


    public List<JMXServiceNode> findJMXServices()
    {
        try
        {
            List<ServiceInfo> serviceInfos = userAgent.findServices(JMX_SERVICE_TYPE, null, null, null);
            return serviceInfosToJMXServiceNodes(serviceInfos);
        }
        catch (Exception x)
        {
            throw new SLPException(x);
        }
    }

    public void start() throws Exception
    {
        // TODO: implement the configuration manager
//        SLPConfiguration config = configurationManager.getConfiguration(SLPModelManager.ID);

        Configuration configuration = new Configuration();
        configuration.setPort(1427);
//        configuration.setPort(config.getPort());

        userAgent = new StandardUserAgent();
        userAgent.setConfiguration(configuration);
        userAgent.start();
    }

    private List<JMXServiceNode> serviceInfosToJMXServiceNodes(List<ServiceInfo> serviceInfos)
    {
        List<JMXServiceNode> result = new ArrayList<JMXServiceNode>();
        for (ServiceInfo serviceInfo : serviceInfos) result.add(serviceInfoToServiceNode(serviceInfo));
        return result;
    }

    private JMXServiceNode serviceInfoToServiceNode(ServiceInfo serviceInfo)
    {
        JMXServiceNode jmxServiceNode = new JMXServiceNode();
        try
        {
            JMXServiceURL jmxServiceURL = new JMXServiceURL(serviceInfo.getServiceURL().getURL());
            jmxServiceNode.setJMXServiceURL(jmxServiceURL);
            // TODO: convert other serviceInfo properties
        }
        catch (MalformedURLException x)
        {
            // TODO: mark the node as "unavailable" or "unconnectable" so the GUI can show it differently
        }
        return jmxServiceNode;
    }
}
