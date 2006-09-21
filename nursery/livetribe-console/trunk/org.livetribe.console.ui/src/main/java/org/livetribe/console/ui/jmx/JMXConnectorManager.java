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

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.livetribe.console.ui.jmx.view.JMXDiscoveryView;

/**
 * @version $Rev$ $Date$
 */
public class JMXConnectorManager
{
    public interface Listener extends EventListener
    {
        public void jmxConnectorAdded(Event event);
        
        public void jmxConnectorChanged(Event event);
        
        public void jmxConnectorRemoved(Event event);
    }
    
    public static class Event extends EventObject
    {
        private final JMXConnectorInfo jmxConnectorInfo;

        public Event(Object source, JMXConnectorInfo jmxConnectorInfo)
        {
            super(source);
            this.jmxConnectorInfo = jmxConnectorInfo;
        }

        public JMXConnectorInfo getJMXConnectorInfo()
        {
            return jmxConnectorInfo;
        }
    }
    
    private final Map<JMXServiceURL, JMXConnector> connectors = new HashMap<JMXServiceURL, JMXConnector>();
    private final List<JMXConnectorInfo> connectorInfos = new ArrayList<JMXConnectorInfo>();
    private final List<Listener> listeners = new ArrayList<Listener>();
    
    public void connect(JMXConnectorInfo jmxConnectorInfo) throws IOException
    {
        JMXServiceURL jmxServiceURL = jmxConnectorInfo.getJMXServiceURL();
        Map<String, ?> environment = jmxConnectorInfo.getEnvironment();
        JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL, environment);
        jmxConnectorInfo.setConnected(true);
        // TODO: synchronize here
        connectors.put(jmxServiceURL, connector);
        
        // Retrieve the ObjectNames
        @SuppressWarnings("unchecked")
        Set<ObjectName> objectNames = connector.getMBeanServerConnection().queryNames(null, null);
        for (ObjectName objectName : objectNames)
        {
            ObjectNameDomainInfo domainInfo = jmxConnectorInfo.find(objectName.getDomain());
            if (domainInfo == null)
            {
                domainInfo = new ObjectNameDomainInfo(objectName, objectName.getDomain());
                jmxConnectorInfo.add(domainInfo);
            }
            JMXNode parent = domainInfo;

            @SuppressWarnings("unchecked")
            Map<String, String> properties = new TreeMap<String, String>(objectName.getKeyPropertyList());
            String key = "type";
            String value = properties.remove(key);
            if (value != null) parent = linkKeys(parent, objectName, key, value);
            key = "name";
            value = properties.remove(key);
            if (value != null) parent = linkKeys(parent, objectName, key, value);
            for (Map.Entry<String, String> entry : properties.entrySet())
            {
                parent = linkKeys(parent, objectName, entry.getKey(), entry.getValue());
            }
        }
        
        // TODO: handle reconnections
    }

    public void close(JMXConnectorInfo jmxConnectorInfo)
    {
        JMXServiceURL jmxServiceURL = jmxConnectorInfo.getJMXServiceURL();
        JMXConnector jmxConnector = connectors.remove(jmxServiceURL);
        if (jmxConnector != null)
        {
            try
            {
                jmxConnector.close();
            }
            catch (IOException x)
            {
                // TODO: log this exception and ignore
            }
            finally
            {
                jmxConnectorInfo.setConnected(false);
            }
        }
        notifyChanged(jmxConnectorInfo);
    }
    
    public void add(JMXConnectorInfo jmxConnectorInfo)
    {
        synchronized (connectorInfos)
        {
            connectorInfos.add(jmxConnectorInfo);
        }
        notifyAdd(jmxConnectorInfo);
    }
    
    private JMXNode linkKeys(JMXNode parent, ObjectName objectName, String key, String value)
    {
        String property = key + "=" + value;
        JMXNode child = parent.find(property);
        if (child == null)
        {
            child = new ObjectNameKeyInfo(objectName, value, property);
            parent.add(child);
        }
        return child;
    }

    public List<JMXConnectorInfo> getJMXConnectorInfos()
    {
        synchronized (connectorInfos)
        {
            return new ArrayList<JMXConnectorInfo>(connectorInfos);
        }        
    }

    public MBeanInfo getMBeanInfo(JMXConnectorInfo connectorInfo, ObjectNameInfo objectNameInfo) throws InstanceNotFoundException, IOException
    {
        JMXServiceURL jmxServiceURL = connectorInfo.getJMXServiceURL();
        JMXConnector connector = connectors.get(jmxServiceURL);
        return getMBeanInfo(connector, objectNameInfo.getObjectName());
    }
    
    private MBeanInfo getMBeanInfo(JMXConnector connector, ObjectName objectName) throws InstanceNotFoundException, IOException
    {
        try
        {
            return connector.getMBeanServerConnection().getMBeanInfo(objectName);
        }
        catch (IntrospectionException x)
        {
            throw new JMXRuntimeException(x);
        }
        catch (ReflectionException x)
        {
            throw new JMXRuntimeException(x);
        }
    }

    public Map<String, Object> getAttributes(JMXConnectorInfo connectorInfo, ObjectNameInfo objectNameInfo, MBeanAttributeInfo[] attributeInfos) throws InstanceNotFoundException, IOException
    {
        JMXServiceURL jmxServiceURL = connectorInfo.getJMXServiceURL();
        JMXConnector connector = connectors.get(jmxServiceURL);
        List<String> attributeNames = new ArrayList<String>();
        for (int i = 0; i < attributeInfos.length; ++i)
        {
            MBeanAttributeInfo attributeInfo = attributeInfos[i];
            if (attributeInfo.isReadable()) attributeNames.add(attributeInfo.getName());
        }
        AttributeList attributes = getAttributes(connector, objectNameInfo.getObjectName(), attributeNames.toArray(new String[0]));
        Map<String, Object> result = new HashMap<String, Object>();
        for (int i = 0; i < attributes.size(); ++i)
        {
            Attribute attribute = (Attribute)attributes.get(i);
            result.put(attribute.getName(), attribute.getValue());
        }
        return result;
    }
    
    private AttributeList getAttributes(JMXConnector connector, ObjectName objectName, String[] attributeNames) throws InstanceNotFoundException, IOException
    {
        try
        {
            return connector.getMBeanServerConnection().getAttributes(objectName, attributeNames);
        }
        catch (ReflectionException x)
        {
            throw new JMXRuntimeException(x);
        }
    }

    public void addListener(Listener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }        
    }
    
    private void notifyAdd(JMXConnectorInfo jmxConnectorInfo)
    {
        Event event = null;
        List<Listener> observers = getListeners();
        for (Listener observer : observers)
        {
            if (event == null) event = new Event(this, jmxConnectorInfo);
            try
            {
                observer.jmxConnectorAdded(event);
            }
            catch (RuntimeException x)
            {
                // TODO: log and ignore
            }            
        }
    }
    
    private void notifyChanged(JMXConnectorInfo jmxConnectorInfo)
    {
        Event event = null;
        List<Listener> observers = getListeners();
        for (Listener observer : observers)
        {
            if (event == null) event = new Event(this, jmxConnectorInfo);
            try
            {
                observer.jmxConnectorChanged(event);
            }
            catch (RuntimeException x)
            {
                // TODO: log and ignore
            }            
        }
    }
    
    public void removeListener(Listener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }        
    }
    
    private List<Listener> getListeners()
    {
        synchronized (listeners)
        {
            return new ArrayList<Listener>(listeners);
        }        
    }
}
