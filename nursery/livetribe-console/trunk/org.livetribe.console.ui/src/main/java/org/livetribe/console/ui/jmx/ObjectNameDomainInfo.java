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

import javax.management.ObjectName;

/**
 * @version $Rev$ $Date$
 */
public class ObjectNameDomainInfo extends ObjectNameInfo
{
    private final String domain;

    public ObjectNameDomainInfo(final ObjectName objectName, final String domain)
    {
        super(objectName);
        this.domain = domain;
    }

    @Override
    public int hashCode()
    {
        int hash = getParent().hashCode();
        hash = 31 * hash + domain.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final ObjectNameDomainInfo that = (ObjectNameDomainInfo)obj;
        return getParent().equals(that.getParent()) && domain.equals(that.domain);
    }

    @Override
    public String getName()
    {
        return getDomain();
    }
    
    public String getDomain()
    {
        return domain;
    }

    @Override
    public ObjectNameKeyInfo find(String property)
    {
        List<JMXNode> children = getChildren();
        for (JMXNode child : children)
        {
            if (child instanceof ObjectNameKeyInfo)
            {
                ObjectNameKeyInfo keyInfo = (ObjectNameKeyInfo)child;
                if (keyInfo.getKeyName().equals(property)) return keyInfo;
            }
        }
        return null;
    }
}
