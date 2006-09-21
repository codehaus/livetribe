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
package org.livetribe.console.ui;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.livetribe.console.ui.jmx.JMXConnectorInfo;
import org.livetribe.console.ui.jmx.JMXConnectorManager;
import org.livetribe.console.ui.jmx.view.MBeanViewContentManager;
import org.livetribe.console.ui.jmx.view.ObjectNameHandlerManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.StandardContainer;
import org.livetribe.ioc.StandardRegistry;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{
    // The plug-in ID
    public static final String PLUGIN_ID = "org.livetribe.console.ui";

    // The shared instance
    private static Activator plugin;
    
    private StandardRegistry registry;
    private Container container;

    public Activator()
    {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        
        registry = new StandardRegistry();
        container = new StandardContainer(registry);
        registry.putService("container", Container.class, container);
        
        setupJMXConnectorManager();
        setupJMXViewMappingManager();
        setupObjectNameHandlerManager();
    }

    private void setupJMXConnectorManager() throws IOException
    {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        JMXServiceURL jmxServiceURL = new JMXServiceURL("rmi", null, 0);
        JMXConnectorServer connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mbeanServer);
        connectorServer.start();
        jmxServiceURL = connectorServer.getAddress();
        
        JMXConnectorManager jmxConnectorManager = new JMXConnectorManager();
        registry.putService("jmxConnectorManager", JMXConnectorManager.class, jmxConnectorManager);
        JMXConnectorInfo jmxConnectorInfo = new JMXConnectorInfo(jmxServiceURL, null);
        jmxConnectorManager.add(jmxConnectorInfo);
        jmxConnectorManager.connect(jmxConnectorInfo);
/*        
        JMXServiceURL jettyJMXServiceURL = new JMXServiceURL("rmi", null, 0, "/jndi/rmi:///jetty");
        JMXConnectorInfo jettyJMXConnectorInfo = new JMXConnectorInfo(jettyJMXServiceURL, null);
        jmxConnectorManager.add(jettyJMXConnectorInfo);
        jmxConnectorManager.connect(jettyJMXConnectorInfo);
*/        
    }
    
    private void setupJMXViewMappingManager()
    {
        MBeanViewContentManager viewMapper = new MBeanViewContentManager();
        registry.putService("viewMappingManager", MBeanViewContentManager.class, viewMapper);
    }

    private void setupObjectNameHandlerManager()
    {
        ObjectNameHandlerManager handlerManager = new ObjectNameHandlerManager();
        registry.putService("objectNameHandlerManager", ObjectNameHandlerManager.class, handlerManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }
    
    public Container getContainer()
    {
        return container;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
