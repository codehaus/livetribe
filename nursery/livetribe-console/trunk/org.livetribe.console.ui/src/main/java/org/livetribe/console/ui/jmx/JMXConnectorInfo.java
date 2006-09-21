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
package org.livetribe.console.ui.jmx;

import java.util.List;
import java.util.Map;

import javax.management.remote.JMXServiceURL;

/**
 * @version $Rev$ $Date$
 */
public class JMXConnectorInfo extends JMXNode
{
    private final JMXServiceURL jmxServiceURL;
    private final Map<String, ?> environment;
    private boolean connected;
    private String name;
    
    public JMXConnectorInfo(final JMXServiceURL jmxServiceURL, final Map<String, ?> environment)
    {
        this.jmxServiceURL = jmxServiceURL;
        this.environment = environment;
        setName(jmxServiceURL.getHost());
    }

    @Override
    public int hashCode()
    {
        return getJMXServiceURL().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final JMXConnectorInfo that = (JMXConnectorInfo)obj;
        return getJMXServiceURL().equals(that.getJMXServiceURL());
    }

    public JMXServiceURL getJMXServiceURL()
    {
        return jmxServiceURL;
    }

    public Map<String, ?> getEnvironment()
    {
        return environment;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ObjectNameDomainInfo find(String domain)
    {
        List<JMXNode> children = getChildren();
        for (JMXNode child : children)
        {
            if (child instanceof ObjectNameDomainInfo)
            {
                ObjectNameDomainInfo domainInfo = (ObjectNameDomainInfo)child;
                if (domainInfo.getDomain().equals(domain)) return domainInfo;
            }
        }
        return null;
    }
}
