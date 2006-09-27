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
package org.livetribe.console.ui.jetty.jmx.view;

import javax.management.ObjectName;

import org.livetribe.console.ui.jetty.Messages;
import org.livetribe.console.ui.jmx.AbstractObjectNameHandler;
import org.livetribe.console.ui.jmx.ObjectNameDomainInfo;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.console.ui.jmx.ObjectNameKeyInfo;

/**
 * @version $Revision$ $Date$
 */
public class Jetty6ObjectNameHandler extends AbstractObjectNameHandler
{
    private static final String MORTBAY_DOMAIN = "org.mortbay";
    private static final String JETTY_DOMAIN = "org.mortbay.jetty";

    public Boolean accepts(Object element)
    {
        if (!isEnabled()) return null;
        
        if (element instanceof ObjectNameInfo)
        {
            ObjectName objectName = ((ObjectNameInfo)element).getObjectName();
            if (objectName.getDomain().equals(JETTY_DOMAIN))
            {
                if (element instanceof ObjectNameDomainInfo) return true;
                if (element instanceof ObjectNameKeyInfo)
                {
                    ObjectNameKeyInfo keyInfo = (ObjectNameKeyInfo)element;
                    return "type".equals(keyInfo.getKeyName()) && "server".equalsIgnoreCase(keyInfo.getKeyValue());
                }
            }
            else if (objectName.getDomain().startsWith(MORTBAY_DOMAIN))
            {
                return false;
            }
        }
        return null;
    }

    @Override
    public String getLabel(Object element)
    {
        if (element instanceof ObjectNameDomainInfo) return Messages.TREE_JETTY_DOMAIN_TEXT;
        if (element instanceof ObjectNameKeyInfo)
        {
            ObjectNameKeyInfo keyInfo = (ObjectNameKeyInfo)element;
            return keyInfo.getKeyValue() + " #" + keyInfo.getObjectName().getKeyProperty("id");
        }
        return super.getLabel(element);
    }
}
