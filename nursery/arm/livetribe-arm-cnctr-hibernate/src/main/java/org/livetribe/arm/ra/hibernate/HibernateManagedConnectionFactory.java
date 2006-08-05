/**
 *
 * Copyright 2006 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.arm.ra.hibernate;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;


/**
 * @version $Revision$ $Date$
 */
public class HibernateManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation, Serializable
{
    private HibernateResourceAdapter resourceAdapter;
    private HibernateConnectionRequestInfo info = new HibernateConnectionRequestInfo();
    private PrintWriter logWriter;


    public void setLogWriter(PrintWriter logWriter) throws ResourceException
    {
        this.logWriter = logWriter;
    }

    public PrintWriter getLogWriter() throws ResourceException
    {
        return logWriter;
    }

    public ResourceAdapter getResourceAdapter()
    {
        return resourceAdapter;
    }

    public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException
    {
        this.resourceAdapter = (HibernateResourceAdapter) resourceAdapter;

        try
        {
            info = (HibernateConnectionRequestInfo) this.resourceAdapter.getInfo().clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new ResourceException("Counld not clone connection request info");
        }
    }

    public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException
    {
        return new HibernateConnectionFactory(info);
    }

    public Object createConnectionFactory() throws ResourceException
    {
        return new HibernateConnectionFactory(info);
    }

    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        if (info == null) connectionRequestInfo = info;

        return new HibernateManagedConnection(subject, (HibernateConnectionRequestInfo) connectionRequestInfo);
    }

    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
        {
            HibernateManagedConnection c = (HibernateManagedConnection) iterator.next();
            if (c.matches(subject, info))
            {
                return c;
            }
        }
        return null;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HibernateManagedConnectionFactory that = (HibernateManagedConnectionFactory) o;

        return info.equals(that.info);
    }

    public int hashCode()
    {
        return info.hashCode();
    }
}
