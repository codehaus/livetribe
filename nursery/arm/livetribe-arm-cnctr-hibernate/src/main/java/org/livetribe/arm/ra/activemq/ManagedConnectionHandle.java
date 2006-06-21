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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.connection.Connection;
import org.livetribe.arm.connection.StaticConnectionMonitor;
import org.livetribe.arm.connection.activemq.messages.ApplicationDefinitionMessage;
import org.livetribe.arm.connection.activemq.messages.ApplicationMessage;
import org.livetribe.arm.connection.activemq.messages.ApplicationRemoteMessage;
import org.livetribe.arm.connection.activemq.messages.BlockMessage;
import org.livetribe.arm.connection.activemq.messages.Message;
import org.livetribe.arm.connection.activemq.messages.MetricGroupDefinitionMessage;
import org.livetribe.arm.connection.activemq.messages.ResetMessage;
import org.livetribe.arm.connection.activemq.messages.StartMessage;
import org.livetribe.arm.connection.activemq.messages.StopMessage;
import org.livetribe.arm.connection.activemq.messages.TransactionDefinitionMessage;
import org.livetribe.arm.connection.activemq.messages.TransactionMessage;
import org.livetribe.arm.connection.activemq.messages.UnblockedMessage;
import org.livetribe.arm.connection.activemq.messages.UpdateMessage;


/**
 * @version $Revision$ $Date$
 */
public class ManagedConnectionHandle implements Connection
{
    private final ActiveMQManagedConnection owner;
    private final javax.jms.Connection amqConnection;
    private final Destination destination;
    private Session session;
    private MessageProducer producer;

    public ManagedConnectionHandle(ActiveMQManagedConnection owner, javax.jms.Connection amqConnection, Destination destination)
    {
        this.owner = owner;
        this.amqConnection = amqConnection;
        this.destination = destination;
    }

    ActiveMQManagedConnection getOwner()
    {
        return owner;
    }

    public void introduceApplicationDefinition(byte[] appDefId, String name, String[] idNames, String[] idValues, String[] ctxNames, byte[] id)
    {
        send(new ApplicationDefinitionMessage(appDefId, name, idNames, idValues, ctxNames, id));
    }

    public void introduceApplication(byte[] appId, byte[] appDefId, String group, String instance, String[] contextValues)
    {
        send(new ApplicationMessage(appId, appDefId, group, instance, contextValues));
    }

    public void introduceApplicationRemote(byte[] appId, byte[] appDefId, String group, String instance, String[] contextValues, byte[] systemAddress)
    {
        send(new ApplicationRemoteMessage(appId, appDefId, group, instance, contextValues, systemAddress));
    }

    public void introduceTransactionDefinition(byte[] transDefId, String name, String[] idNames, String[] idValues, String[] ctxNames, String uri, byte[] id)
    {
        send(new TransactionDefinitionMessage(transDefId, name, idNames, idValues, ctxNames, uri, id));
    }

    public void associateTransaction(byte[] transId, byte [] appId, byte [] transDefId)
    {
        send(new TransactionMessage(transId, appId, transDefId));
    }

    public void introduceMetricGroupDefinition(byte[] metricGroupDef, byte[][] appDef, String[] name, String[] units, short[] usage, byte[][]id)
    {
        send(new MetricGroupDefinitionMessage(metricGroupDef, appDef, name, units, usage, id));
    }

    public void start(byte[] transId, byte[] correlator, long start, byte[] parent, ArmUser user, String[] contextValues, String contextURI)
    {
        send(new StartMessage(transId, correlator, start, parent, user, contextValues, contextURI));
    }

    public void update(byte[] transId, byte[] correlator)
    {
        send(new UpdateMessage(transId, correlator));
    }

    public void block(byte[] transId, byte[] correlator, long handle)
    {
        send(new BlockMessage(transId, correlator, handle));
    }

    public void unblocked(byte[] transId, byte[] correlator, long handle)
    {
        send(new UnblockedMessage(transId, correlator, handle));
    }

    public void stop(byte[] transId, byte[] correlator, long end, int status, String message)
    {
        send(new StopMessage(transId, correlator, end, status, message));
    }

    public void reset(byte[] transId, byte[] correlator)
    {
        send(new ResetMessage(transId, correlator));
    }

    void cleanup()
    {
        try
        {
            session.close();
        }
        catch (JMSException doNothing)
        {
        }
    }

    private void send(Message msg)
    {
        try
        {
            producer.send(session.createObjectMessage(msg));
        }
        catch (JMSException e)
        {
            StaticConnectionMonitor.error(this, e);
        }
    }
}
