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

import org.livetribe.console.ui.jmx.IObjectNameHandler;
import org.livetribe.console.ui.jmx.JMXNode;
import org.livetribe.console.ui.jmx.ObjectNameDomainInfo;
import org.livetribe.console.ui.jmx.ObjectNameInfo;

/**
 * @version $Revision$ $Date$
 */
public class Jetty6ObjectNameHandler implements IObjectNameHandler
{
    private static final String JETTY6_DOMAIN = "org.mortbay.jetty";
    private boolean enabled; 

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public Boolean accepts(Object element)
    {
        if (!enabled) return null;
        
        if (element instanceof ObjectNameInfo)
        {
            ObjectName objectName = ((ObjectNameInfo)element).getObjectName();
            if (objectName.getDomain().equals(JETTY6_DOMAIN))
            {
                return element instanceof ObjectNameDomainInfo;
            }
            else if (objectName.getDomain().startsWith(JETTY6_DOMAIN))
            {
                return false;
            }
        }
        return null;
    }

    public String getLabel(Object element)
    {
        return ((JMXNode)element).getName();
    }
}
