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

import javax.management.ObjectName;

import org.livetribe.console.ui.jmx.JMXConnectorInfo;

/**
 * @version $Revision$ $Date$
 */
public class ServerInfo
{
    private final JMXConnectorInfo jmxConnectorInfo;
    private final ObjectName objectName;
    private String version;
    private long startupTime;
    
    public ServerInfo(final JMXConnectorInfo jmxConnectorInfo, final ObjectName objectName)
    {
        this.jmxConnectorInfo = jmxConnectorInfo;
        this.objectName = objectName;
    }

    public JMXConnectorInfo getJMXConnectorInfo()
    {
        return jmxConnectorInfo;
    }

    public ObjectName getObjectName()
    {
        return objectName;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    protected void setVersion(String version)
    {
        this.version = version;
    }

    public long getStartupTime()
    {
        return startupTime;
    }

    protected void setStartupTime(long startupTime)
    {
        this.startupTime = startupTime;
    }
}
