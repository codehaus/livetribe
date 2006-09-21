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
package org.livetribe.console.ui.jmx.view;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @version $Revision$ $Date$
 */
public class ObjectNameHandlerInfo
{
    private final String id;
    private final String name;
    private final String description;
    private final IConfigurationElement configurationElement;
    private boolean enabled;

    public ObjectNameHandlerInfo(String id, String name, String description, IConfigurationElement configurationElement)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.configurationElement = configurationElement;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    
    public String getDescription()
    {
        return description;
    }

    public IConfigurationElement getConfigurationElement()
    {
        return configurationElement;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
