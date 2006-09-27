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

/**
 * @version $Revision$ $Date$
 */
public class ContextInfo
{
    private final ServerInfo serverInfo;
    private final ObjectName objectName;
    private String contextPath;
    private String displayName;
    
    public ContextInfo(final ServerInfo serverInfo, final ObjectName objectName)
    {
        this.serverInfo = serverInfo;
        this.objectName = objectName;
    }
    
    public ServerInfo getServerInfo()
    {
        return serverInfo;
    }
    
    public ObjectName getObjectName()
    {
        return objectName;
    }

    public String getContextPath()
    {
        return contextPath;
    }
    
    public void setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
    }
    
    public String getDisplayName()
    {
        return displayName;
    }
    
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
}
