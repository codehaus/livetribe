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
package org.livetribe.jmx.remote.ltw;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.remote.MBeanServerForwarder;

/**
 * @version $Rev$ $Date$
 */
public class WrapperMBeanServerForwarder implements InvocationHandler
{
    private final MBeanServer mbeanServer;

    public WrapperMBeanServerForwarder(MBeanServer mbeanServer, List forwarders)
    {
        MBeanServerForwarder head = null;
        MBeanServerForwarder tail = null;
        for (java.util.Iterator frwrders = forwarders.iterator(); frwrders.hasNext();)
        {
            MBeanServerForwarder forwarder = (MBeanServerForwarder)frwrders.next();
            if (tail == null) head = forwarder;
            if (tail != null) tail.setMBeanServer(forwarder);
            tail = forwarder;
        }
        if (tail != null) tail.setMBeanServer(mbeanServer);

        if (head != null)
            this.mbeanServer = head;
        else
            this.mbeanServer = mbeanServer;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        return method.invoke(mbeanServer, args);
    }
}
