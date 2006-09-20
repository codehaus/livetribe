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
package org.livetribe.arm.connection.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import java.util.List;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.connection.Connection;
import org.livetribe.arm.connection.StaticConnectionMonitor;
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
 * @version $Revision: $ $Date: $
 */
public class ActiveMQConnection implements Connection
{
    private ActiveMQConnectionFactory factory;
    private javax.jms.Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public ActiveMQConnectionFactory getFactory()
    {
        return factory;
    }

    public void setFactory(ActiveMQConnectionFactory factory)
    {
        this.factory = factory;
    }

    public Destination getDestination()
    {
        return destination;
    }

    public void setDestination(Destination destination)
    {
        this.destination = destination;
    }

    public void init() throws Exception
    {
        connection = factory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(destination);

        connection.start();
    }

    public void destroy() throws Exception
    {
        session.close();
        connection.close();
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

    public void declareTransactionDefinition(String transDefOID, String name, String idPropTranOID, byte[] id)
    {
        send(new TransactionDefinitionMessage(transDefOID, name, idPropTranOID, id));
    }

    public void associateTransaction(String transOID, String appOID, String transDefOID)
    {
        send(new TransactionMessage(transOID, appOID, transDefOID));
    }

    public void declareMetricGroupDefinition(String metricGroupDefOID, String[] appDefOID, String[] name, String[] units, short[] usage, byte[][]id)
    {
        send(new MetricGroupDefinitionMessage(metricGroupDefOID, appDefOID, name, units, usage, id));
    }

    public void declareTransactionWithMetricsDefinition(String transDefOID, String appDefOID, String name, String idPropOID, String metricGroupDefOID, byte[] id)
    {
        //todo: consider this autogenerated code
    }

    public void declareMetricGroup(String metricGroupOID, String metricGroupDefOID)
    {
        //todo: consider this autogenerated code
    }

    public void declareTranReportWithMetrics(String tranReportMetricsOID, String tranReportMetricsDefOID, String metricGroupOID)
    {
        //todo: consider this autogenerated code
    }

    public void declareTranWithMetrics(String tranReportMetricsOID, String tranReportMetricsDefOID, String metricGroupOID)
    {
        //todo: consider this autogenerated code
    }

    public void start(String transOID, byte[] correlator, long start, byte[] parent, ArmUser user, String[] contextValues, String contextURI)
    {
        send(new StartMessage(transOID, correlator, start, parent, user, contextValues, contextURI));
    }

    public void update(String transOID, byte[] correlator, long ts)
    {
        send(new UpdateMessage(transOID, correlator, ts));
    }

    public void block(String transOID, byte[] correlator, long handle, long ts)
    {
        send(new BlockMessage(transOID, correlator, handle, ts));
    }

    public void unblocked(String transOID, byte[] correlator, long handle, long ts)
    {
        send(new UnblockedMessage(transOID, correlator, handle, ts));
    }

    public void stop(String transOID, byte[] correlator, long end, int status, String message)
    {
        //todo: consider this autogenerated code
    }

    public void stop(String transOID, byte[] correlator, long end, int status, String message, List[] metrics)
    {
        send(new StopMessage(transOID, correlator, end, status, message));
    }

    public void reset(String transOID, byte[] correlator)
    {
        send(new ResetMessage(transOID, correlator));
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, String diagnosticDetail)
    {
        //todo: consider this autogenerated code
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, long stopTime, String diagnosticDetail)
    {
        //todo: consider this autogenerated code
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, String diagnosticDetail, List[] metrics)
    {
        //todo: consider this autogenerated code
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, long stopTime, String diagnosticDetail, List[] metrics)
    {
        //todo: consider this autogenerated code
    }

    public void start()
    {
        //todo: consider this autogenerated code
    }

    public void stop()
    {
        //todo: consider this autogenerated code
    }

    public void close()
    {
        //todo: consider this autogenerated code
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
