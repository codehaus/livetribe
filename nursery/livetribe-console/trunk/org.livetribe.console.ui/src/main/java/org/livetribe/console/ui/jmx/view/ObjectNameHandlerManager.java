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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.jmx.IObjectNameHandler;

/**
 * @version $Revision$ $Date$
 */
public class ObjectNameHandlerManager
{
    private static final String EXTENSION_ID = "org.livetribe.console.ui.jmx.objectNameHandlers";
    private static final String ELEMENT_HANDLER = "handler";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private static final String ATTRIBUTE_CLASS_NAME = "className";
    private static final String ATTRIBUTE_ENABLED = "enabled";
    
    private List<ObjectNameHandlerInfo> infos; 
    private List<IObjectNameHandler> handlers; 
    
    public List<ObjectNameHandlerInfo> getObjectNameHandlerInfos()
    {
        if (infos == null)
        {
            infos = new ArrayList<ObjectNameHandlerInfo>();
            IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_ID).getExtensions();
            for (IExtension extension : extensions)
            {
                IConfigurationElement[] elements = extension.getConfigurationElements();
                for (IConfigurationElement element : elements)
                {
                    ObjectNameHandlerInfo info = parseExtension(element);
                    if (info != null) infos.add(info);
                }
            }
        }
        return infos;
    }

    private ObjectNameHandlerInfo parseExtension(IConfigurationElement element)
    {
        if (ELEMENT_HANDLER.equals(element.getName()))
        {
            String id = element.getAttribute(ATTRIBUTE_ID);
            String name = element.getAttribute(ATTRIBUTE_NAME);
            String description = element.getAttribute(ATTRIBUTE_DESCRIPTION);
            String className = element.getAttribute(ATTRIBUTE_CLASS_NAME);
            if (id != null && name != null && className != null)
            {
                ObjectNameHandlerInfo result = new ObjectNameHandlerInfo(id, name, description, element);
                boolean enabled = true;
                String enabledAttr = element.getAttribute(ATTRIBUTE_ENABLED);
                if (enabledAttr != null) enabled = Boolean.valueOf(enabledAttr).booleanValue();
                result.setEnabled(enabled);
                return result;
            }
        }
        return null;
    }

    public List<IObjectNameHandler> getObjectNameHandlers()
    {
        if (handlers == null)
        {
            handlers = new ArrayList<IObjectNameHandler>();
            for (ObjectNameHandlerInfo info : getObjectNameHandlerInfos())
            {
                try
                {
                    IObjectNameHandler handler = (IObjectNameHandler)info.getConfigurationElement().createExecutableExtension(ATTRIBUTE_CLASS_NAME);
                    handler.setEnabled(info.isEnabled());
                    handlers.add(handler);
                }
                catch (CoreException x)
                {
                    // TODO: log this exception
                    x.printStackTrace();
                }
            }
        }
        return handlers;
    }
}
