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
package org.livetribe.console.ui.jetty.jmx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.livetribe.console.ui.jmx.JMXConnectorInfo;
import org.livetribe.console.ui.jmx.JMXConnectorManager;
import org.livetribe.console.ui.jmx.JMXRuntimeException;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision$ $Date$
 */
public class Jetty6Manager
{
    @Inject
    private JMXConnectorManager jmxConnectorManager;
    
    public ServerInfo getServerInfo(ObjectNameInfo objectNameInfo) throws IOException
    {
        JMXConnectorInfo jmxConnectorInfo = (JMXConnectorInfo)objectNameInfo.getRoot();
        ServerInfo serverInfo = new ServerInfo(jmxConnectorInfo, objectNameInfo.getObjectName());
        return retrieveServerInfo(serverInfo);
    }
    
    private ServerInfo retrieveServerInfo(ServerInfo serverInfo) throws IOException
    {
        try
        {
            String[] attributeNames = new String[] {"version", "startupTime"};
            Map<String, Object> attributes = jmxConnectorManager.getAttributes(serverInfo.getJMXConnectorInfo(), serverInfo.getObjectName(), attributeNames);
            for (Map.Entry<String, Object> entry : attributes.entrySet())
            {
                if ("version".equals(entry.getKey())) serverInfo.setVersion((String)entry.getValue());
                else if ("startupTime".equals(entry.getKey())) serverInfo.setStartupTime((Long)entry.getValue());
            }
            return serverInfo;
        }
        catch (InstanceNotFoundException x)
        {
            // Concurrently unregistered
            return null;
        }
    }

    public List<ContextInfo> getContextInfos(ServerInfo serverInfo) throws IOException
    {
        List<ContextInfo> result = new ArrayList<ContextInfo>();
        try
        {
            JMXConnectorInfo jmxConnectorInfo = serverInfo.getJMXConnectorInfo();
            ObjectName serverObjectName = serverInfo.getObjectName();
            ObjectName[] contextObjectNames = (ObjectName[])jmxConnectorManager.getAttribute(jmxConnectorInfo, serverObjectName, "contexts");
            for (ObjectName contextObjectName : contextObjectNames)
            {
                ContextInfo contextInfo = retrieveContextInfo(serverInfo, contextObjectName);
                if (contextInfo != null) result.add(contextInfo);
            }
        }
        catch (AttributeNotFoundException x)
        {
            throw new JMXRuntimeException(x);
        }
        catch (InstanceNotFoundException x)
        {
            // Concurrently unregistered
            // TODO: log this
        }
        return result;
    }

    private ContextInfo retrieveContextInfo(ServerInfo serverInfo, ObjectName contextObjectName) throws IOException
    {
        try
        {
            String[] attributeNames = new String[] {"contextPath", "displayName"};
            Map<String, Object> attributes = jmxConnectorManager.getAttributes(serverInfo.getJMXConnectorInfo(), contextObjectName, attributeNames);
            ContextInfo result = new ContextInfo(serverInfo, contextObjectName);
            for (Map.Entry<String, Object> entry : attributes.entrySet())
            {
                if ("contextPath".equals(entry.getKey())) result.setContextPath((String)entry.getValue());
                else if ("displayName".equals(entry.getKey())) result.setDisplayName((String)entry.getValue());
            }
            return result;
        }
        catch (InstanceNotFoundException x)
        {
            // Concurrently unregistered
            // TODO: log this
            return null;
        }
    }
}
