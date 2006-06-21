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
package org.livetribe.arm.ra.activemq;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;
import java.util.logging.Logger;


/**
 * @version $Revision$ $Date$
 */
public class DefaultConnectionManager implements ConnectionManager, ConnectionEventListener
{
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Object allocateConnection(ManagedConnectionFactory managedConnectionFactory, ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        Subject subject = null;

        ManagedConnection connection = managedConnectionFactory.createManagedConnection(subject, connectionRequestInfo);
        connection.addConnectionEventListener(this);

        return connection.getConnection(subject, connectionRequestInfo);
    }

    public void connectionClosed(ConnectionEvent connectionEvent)
    {
        try
        {
            ((ManagedConnection) connectionEvent.getSource()).cleanup();
        }
        catch (ResourceException re)
        {
            logger.warning("Error occured during the cleanup of a managed connection: " + re);
        }
        try
        {
            ((ManagedConnection) connectionEvent.getSource()).destroy();
        }
        catch (ResourceException re)
        {
            logger.warning("Error occured during the destruction of a managed connection: " + re);
        }
    }

    public void localTransactionStarted(ConnectionEvent connectionEvent)
    {
    }

    public void localTransactionCommitted(ConnectionEvent connectionEvent)
    {
    }

    public void localTransactionRolledback(ConnectionEvent connectionEvent)
    {
    }

    public void connectionErrorOccurred(ConnectionEvent connectionEvent)
    {
    }
}
