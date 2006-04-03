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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

/**
 * @version $Rev$ $Date$
 */
public class Gateway extends StandardMBean implements GatewayMBean, MBeanRegistration
{
    private final Map gates = new HashMap();
    private final Lock gatesLock = new ReentrantLock();
    private MBeanServer mbeanServer;

    public Gateway() throws NotCompliantMBeanException
    {
        super(GatewayMBean.class);
    }

    public ObjectName preRegister(MBeanServer mbeanServer, ObjectName objectName) throws Exception
    {
        this.mbeanServer = mbeanServer;
        return objectName;
    }

    public void postRegister(Boolean registered)
    {
        if (!registered.booleanValue()) mbeanServer = null;
    }

    public void preDeregister() throws Exception
    {
        mbeanServer = null;
    }

    public void postDeregister()
    {
    }

    public ObjectName mount(MountPoint mountPoint)
    {
        try
        {
            Gate gate = new Gate(mountPoint, null);
            String gateName = gate.getName();
            ObjectName gateObjectName = ObjectName.getInstance(":type=Gate,gateName=" + gateName);
            gatesLock.lock();
            try
            {
                // TODO: handle duplicate names
                gates.put(gateName, gate);
                mbeanServer.registerMBean(gate, gateObjectName);
            }
            finally
            {
                gatesLock.unlock();
            }

            try
            {
                gate.connect();
            }
            catch (IOException x)
            {
                // TODO: explain why it is not enabled (useful to the user)
            }

            return gateObjectName;
        }
        catch (NotCompliantMBeanException x)
        {
            throw new AssertionError(x);
        }
        catch (MalformedObjectNameException x)
        {
            throw new AssertionError(x);
        }
        catch (MBeanRegistrationException x)
        {
            throw new AssertionError(x);
        }
        catch (InstanceAlreadyExistsException x)
        {
            throw new AssertionError(x);
        }
    }

    public Gate getGate(String name)
    {
        gatesLock.lock();
        try
        {
            return (Gate)gates.get(name);
        }
        finally
        {
            gatesLock.unlock();
        }
    }

    public Set getGateObjectNames()
    {
        try
        {
            return mbeanServer.queryNames(ObjectName.getInstance(":type=Gate,*"), null);
        }
        catch (MalformedObjectNameException x)
        {
            throw new AssertionError(x);
        }
    }

    public ObjectName getGateObjectName(String name)
    {
        if (name == null) return null;
        try
        {
            return ObjectName.getInstance(":type=Gate,gateName=" + name);
        }
        catch (MalformedObjectNameException x)
        {
            throw new AssertionError(x);
        }
    }

    public List getMounted()
    {
        List result = new ArrayList();
        gatesLock.lock();
        for (Iterator gatesIter = gates.values().iterator(); gatesIter.hasNext();)
        {
            Gate gate = (Gate)gatesIter.next();
            result.add(gate.getMountPoint());
        }
        return result;
    }
}
