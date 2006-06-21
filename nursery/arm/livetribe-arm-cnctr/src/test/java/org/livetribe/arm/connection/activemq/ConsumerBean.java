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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Blatently stolen from ActiveMQ
 *
 * @version $Revision: $ $Date: $
 */
public class ConsumerBean implements MessageListener
{
    private final List messages = new ArrayList();
    private final Object semaphore;
    private Destination destination;
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public ConsumerBean()
    {
        this(new Object());
    }

    public ConsumerBean(Object semaphore)
    {
        this.semaphore = semaphore;
    }

    public Destination getDestination()
    {
        return destination;
    }

    public void setDestination(Destination destination)
    {
        this.destination = destination;
    }

    public ConnectionFactory getFactory()
    {
        return factory;
    }

    public void setFactory(ConnectionFactory factory)
    {
        this.factory = factory;
    }

    public void start() throws JMSException
    {
        connection = factory.createConnection();

        connection.start();

        session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);

    }


    public void stop() throws JMSException
    {
        if (consumer != null)
            consumer.close();
        if (session != null)
            session.close();
        if (connection != null)
            connection.close();
    }

    public synchronized List flushMessages()
    {
        List answer = new ArrayList(messages);
        messages.clear();
        return answer;
    }

    public synchronized void onMessage(Message message)
    {
        try
        {
            messages.add(((ObjectMessage) message).getObject());
        }
        catch (JMSException e)
        {
        }
        synchronized (semaphore)
        {
            semaphore.notifyAll();
        }
        try
        {
            message.acknowledge();
        }
        catch (JMSException e)
        {
        }
    }

    public void waitForMessageToArrive()
    {
        try
        {
            if (hasReceivedMessage())
            {
                synchronized (semaphore)
                {
                    semaphore.wait(4000);
                }
            }
        }
        catch (InterruptedException doNothing)
        {
        }
    }

    public void waitForMessagesToArrive(int messageCount)
    {
        for (int i = 0; i < 10; i++)
        {
            waitForMessageToArrive();
        }
    }

    protected boolean hasReceivedMessage()
    {
        return messages.isEmpty();
    }

    protected synchronized boolean hasReceivedMessages(int messageCount)
    {
        return messages.size() >= messageCount;
    }
}
