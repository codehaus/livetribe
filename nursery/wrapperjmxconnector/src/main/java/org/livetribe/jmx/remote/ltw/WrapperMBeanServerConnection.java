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
import java.util.Iterator;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.security.auth.Subject;

import org.livetribe.jmx.remote.MBeanServerConnectionForwarder;

/**
 * $Rev$ $Date$
 */
public class WrapperMBeanServerConnection implements InvocationHandler
{
    private final MBeanServerConnection mbeanServerConnection;

    public WrapperMBeanServerConnection(MBeanServerConnection mbeanServerConnection, Subject subject, List connections)
    {
        MBeanServerConnectionForwarder head = null;
        MBeanServerConnectionForwarder tail = null;
        for (Iterator cntions = connections.iterator(); cntions.hasNext();)
        {
            MBeanServerConnectionForwarder forwarder = (MBeanServerConnectionForwarder)cntions.next();
            if (tail == null) head = forwarder;
            if (tail != null) tail.setMBeanServerConnection(forwarder);
            if (forwarder instanceof MBeanServerConnectionForwarder.SubjectAware)
                ((MBeanServerConnectionForwarder.SubjectAware)forwarder).setSubject(subject);
            tail = forwarder;
        }
        if (tail != null) tail.setMBeanServerConnection(mbeanServerConnection);

        if (head != null)
            this.mbeanServerConnection = head;
        else
            this.mbeanServerConnection = mbeanServerConnection;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        return method.invoke(mbeanServerConnection, args);
    }
}
