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
package org.livetribe.jmx.mbeans.gateway;

import java.net.MalformedURLException;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;

/**
 * @version $Rev$ $Date$
 */
public class MountPoint
{
    private JMXServiceURL jmxServiceURL;
    private ObjectName sourcePattern;
    private String name;

    public MountPoint(String jmxServiceURL)
    {
        try
        {
            this.jmxServiceURL = new JMXServiceURL(jmxServiceURL);
        }
        catch (MalformedURLException ignored)
        {
            this.jmxServiceURL = null;
        }
    }

    public JMXServiceURL getJMXServiceURL()
    {
        return jmxServiceURL;
    }

    public ObjectName getSourcePattern()
    {
        return sourcePattern;
    }

    public void setSourcePattern(ObjectName sourcePattern)
    {
        this.sourcePattern = sourcePattern;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String mountPoint)
    {
        this.name = mountPoint;
    }
}
