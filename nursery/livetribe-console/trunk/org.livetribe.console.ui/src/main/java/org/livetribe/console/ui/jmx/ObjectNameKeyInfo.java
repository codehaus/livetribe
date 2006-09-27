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
public class ObjectNameKeyInfo extends ObjectNameInfo
{
    private final String keyName;
    private final String keyValue;
    private final String property;
    
    public ObjectNameKeyInfo(final ObjectName objectName,final String key, final String value)
    {
        super(objectName);
        this.keyName = key;
        this.keyValue = value;
        this.property = key + "=" + value;
    }

    @Override
    public int hashCode()
    {
        int hash = getParent().hashCode();
        hash = 31 * hash + property.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final ObjectNameKeyInfo that = (ObjectNameKeyInfo)obj;
        return getParent().equals(that.getParent()) && property.equals(that.property);
    }

    @Override
    public String getName()
    {
        return getKeyValue();
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
                if (keyInfo.property.equals(property)) return keyInfo;
            }
        }
        return null;
    }

    public String getKeyValue()
    {
        return keyValue;
    }
    
    public String getKeyName()
    {
        return keyName;
    }
}
