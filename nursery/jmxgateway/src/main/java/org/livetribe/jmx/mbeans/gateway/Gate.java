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

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.StandardMBean;
import javax.management.QueryExp;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;

/**
 * @version $Rev$ $Date$
 */
public class Gate extends StandardMBean implements GateMBean
{
    private final MountPoint mountPoint;
    private final Map environment;
    private boolean enabled;
    private JMXConnector jmxConnector;

    public Gate(MountPoint mountPoint, Map environment) throws NotCompliantMBeanException
    {
        super(GateMBean.class);
        this.mountPoint = mountPoint;
        this.environment = environment;
    }

    public void connect() throws IOException
    {
        jmxConnector = JMXConnectorFactory.connect(mountPoint.getJMXServiceURL(), environment);
        setEnabled(true);
    }

    public void close() throws IOException
    {
        if (jmxConnector != null) jmxConnector.close();
    }

    public Object getAttribute(ObjectName objectName, String attributeName)
            throws IOException, ReflectionException, InstanceNotFoundException, MBeanException, AttributeNotFoundException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.getAttribute(objectName, attributeName);
    }

    public Object invoke(ObjectName objectName, String operationName, Object[] arguments, String[] signature)
            throws IOException, ReflectionException, InstanceNotFoundException, MBeanException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.invoke(objectName, operationName, arguments, signature);
    }

    public boolean isRegistered(ObjectName objectName)
            throws IOException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.isRegistered(objectName);
    }

    public Set queryNames(ObjectName pattern, QueryExp expression)
            throws IOException
    {
        MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
        return jmxConnection.queryNames(pattern, expression);
    }

    public String getName()
    {
        return mountPoint.getName();
    }

    public void setEnabled(boolean value)
    {
        this.enabled = value;
    }

    public MountPoint getMountPoint()
    {
        return mountPoint;
    }
}
