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


import org.hibernate.Session;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.connection.Connection;
import org.livetribe.arm.connection.messages.ApplicationDefinitionMessage;
import org.livetribe.arm.connection.messages.ApplicationMessage;
import org.livetribe.arm.connection.messages.ApplicationRemoteMessage;
import org.livetribe.arm.connection.messages.BlockMessage;
import org.livetribe.arm.connection.messages.IdentityPropertiesMessage;
import org.livetribe.arm.connection.messages.IdentityPropertiesTransactionMessage;
import org.livetribe.arm.connection.messages.Message;
import org.livetribe.arm.connection.messages.MetricGroupDefinitionMessage;
import org.livetribe.arm.connection.messages.ResetMessage;
import org.livetribe.arm.connection.messages.StartMessage;
import org.livetribe.arm.connection.messages.StopMessage;
import org.livetribe.arm.connection.messages.TransactionDefinitionMessage;
import org.livetribe.arm.connection.messages.TransactionMessage;
import org.livetribe.arm.connection.messages.UnblockedMessage;
import org.livetribe.arm.connection.messages.UpdateMessage;


/**
 * @version $Revision$ $Date$
 */
public class ConnectionHandle implements Connection
{
    private final HibernateManagedConnection owner;
    private final Session session;
    private boolean ready = false;
    private boolean closed = false;


    public ConnectionHandle(HibernateManagedConnection owner, Session session)
    {
        this.owner = owner;
        this.session = session;
    }

    HibernateManagedConnection getOwner()
    {
        return owner;
    }

    public void declareIdentityProperties(String idPropOID, String[] idNames, String[] idValues, String[] ctxNames)
    {
        send(new IdentityPropertiesMessage(idPropOID, idNames, idValues, ctxNames));
    }

    public void declareIdentityPropertiesTransaction(String idPropOID, String[] idNames, String[] idValues, String[] ctxNames, String uriValue)
    {
        send(new IdentityPropertiesTransactionMessage(idPropOID, idNames, idValues, ctxNames, uriValue));
    }

    public void declareApplicationDefinition(String appDefOID, String name, String idPropOID, byte[] id)
    {
        send(new ApplicationDefinitionMessage(appDefOID, name, idPropOID, id));
    }

    public void declareApplication(String appOID, String appDefOID, String group, String instance, String[] contextValues)
    {
        send(new ApplicationMessage(appOID, appDefOID, group, instance, contextValues));
    }

    public void declareApplicationRemote(String appOID, String appDefOID, String group, String instance, String[] contextValues, byte[] systemAddress)
    {
        send(new ApplicationRemoteMessage(appOID, appDefOID, group, instance, contextValues, systemAddress));
    }

    public void declareTransactionDefinition(String transDefOID, String name, String[] idNames, String[] idValues, String[] ctxNames, String uri, byte[] id)
    {
        send(new TransactionDefinitionMessage(transDefOID, name, idNames, idValues, ctxNames, uri, id));
    }

    public void associateTransaction(String transOID, String appOID, String transDefOID)
    {
        send(new TransactionMessage(transOID, appOID, transDefOID));
    }

    public void introduceMetricGroupDefinition(String metricGroupDefOID, String[] appDefOID, String[] name, String[] units, short[] usage, byte[][]id)
    {
        send(new MetricGroupDefinitionMessage(metricGroupDefOID, appDefOID, name, units, usage, id));
    }

    public void start(String transIdOID, byte[] correlator, long start, byte[] parent, ArmUser user, String[] contextValues, String contextURI)
    {
        send(new StartMessage(transIdOID, correlator, start, parent, user, contextValues, contextURI));
    }

    public void update(String transIdOID, byte[] correlator, long ts)
    {
        send(new UpdateMessage(transIdOID, correlator, ts));
    }

    public void block(String transIdOID, byte[] correlator, long handle, long ts)
    {
        send(new BlockMessage(transIdOID, correlator, handle, ts));
    }

    public void unblocked(String transIdOID, byte[] correlator, long handle, long ts)
    {
        send(new UnblockedMessage(transIdOID, correlator, handle, ts));
    }

    public void stop(String transIdOID, byte[] correlator, long end, int status, String message)
    {
        send(new StopMessage(transIdOID, correlator, end, status, message));
    }

    public void reset(String transOID, byte[] correlator)
    {
        send(new ResetMessage(transOID, correlator));
    }

    public void start()
    {
        if (closed) throw new IllegalStateException("The Connection is closed");
        ready = true;
    }

    public void stop()
    {
        ready = false;
    }

    public void close()
    {
        if (closed) return;

        session.close();
        owner.removeConnection(this);
        owner.fireCloseEvent(this);

        ready = false;
        closed = true;
    }

    private void send(Message msg)
    {
        if (closed) throw new IllegalStateException("The Connection is closed");
        if (!ready) throw new IllegalStateException("The Connection has not been started");

        session.save(msg);
        session.flush();
    }
}
