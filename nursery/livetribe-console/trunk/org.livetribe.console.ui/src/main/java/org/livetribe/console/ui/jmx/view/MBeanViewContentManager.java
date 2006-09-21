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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.jmx.IMBeanViewContent;

/**
 * @version $Rev$ $Date$
 */
public class MBeanViewContentManager
{
	private static final String EXTENSION_ID = "org.livetribe.console.ui.jmx.mbeanViewContents";
	private static final String ELEMENT_MAPPING = "mapping";
	private static final String ELEMENT_PATTERN = "pattern";
	private static final String ATTRIBUTE_ID = "id";
	private static final String ATTRIBUTE_CLASS_NAME = "className";
	
    private List<MBeanViewContentInfo> mbeanViewContents;
    
    public IMBeanViewContent getMBeanViewContent(ObjectName objectName)
    {
    	if (mbeanViewContents == null)
    	{
    		mbeanViewContents = new ArrayList<MBeanViewContentInfo>();
        	IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_ID).getExtensions();
        	for (IExtension extension : extensions)
        	{
        		IConfigurationElement[] elements = extension.getConfigurationElements();
        		for (IConfigurationElement element : elements)
        		{
                    MBeanViewContentInfo content = parseExtension(element);
        			if (content != null) mbeanViewContents.add(content);
        		}
        	}
        	sort(mbeanViewContents);
    	}
    	
    	for (MBeanViewContentInfo mapping : mbeanViewContents)
    	{
    		if (mapping.matches(objectName)) 
            {
                try
                {
                    IMBeanViewContent result = (IMBeanViewContent)mapping.getConfigurationElement().createExecutableExtension(ATTRIBUTE_CLASS_NAME);
                    Activator.getDefault().getContainer().resolve(result);
                    return result;
                }
                catch (CoreException x)
                {
                    // TODO: log this exception
                    x.printStackTrace();
                }
            }
    	}
        
        // Return the default for now; to be removed when partial order sort is implemented
        IMBeanViewContent defaultResult = new MBeanViewContent();
        Activator.getDefault().getContainer().resolve(defaultResult);
    	return defaultResult;
    }

	private void sort(List<MBeanViewContentInfo> mappings) 
	{
		// TODO: implement partial order sort
	}

	private MBeanViewContentInfo parseExtension(IConfigurationElement element) 
	{
		// TODO: read "before" and "after" attributes to give partial ordering
		try 
		{
			if (ELEMENT_MAPPING.equals(element.getName())) 
			{
				String id = element.getAttribute(ATTRIBUTE_ID);
				if (id != null) 
				{
					String className = element.getAttribute(ATTRIBUTE_CLASS_NAME);
					if (className != null) 
					{
                        MBeanViewContentInfo contentInfo = new MBeanViewContentInfo(id, className, element);
						IConfigurationElement[] patterns = element.getChildren(ELEMENT_PATTERN);
						for (IConfigurationElement pattern : patterns) 
						{
							String value = pattern.getValue();
							contentInfo.addObjectNamePattern(ObjectName.getInstance(value));
						}
						return contentInfo;
					}
				}
			}
		}
		catch (MalformedObjectNameException x) 
		{
			// TODO: log this exception
		}		
		return null;
	}
	
	private static class MBeanViewContentInfo
	{
		private final String id;
		private final String className;
        private final IConfigurationElement configurationElement;
		private final List<ObjectName> patterns = new ArrayList<ObjectName>();
		
		public MBeanViewContentInfo(final String id, final String className, final IConfigurationElement configurationElement) 
		{
			this.id = id;
			this.className = className;
            this.configurationElement = configurationElement;
		}

		public String getClassName() 
		{
			return className;
		}

		public IConfigurationElement getConfigurationElement()
        {
            return configurationElement;
        }

        public void addObjectNamePattern(ObjectName pattern) 
		{
			patterns.add(pattern);
		}

		public boolean matches(ObjectName objectName) 
		{
			for (ObjectName pattern : patterns)
			{
				if (pattern.apply(objectName)) return true;
			}
			return false;
		}
	}
}
