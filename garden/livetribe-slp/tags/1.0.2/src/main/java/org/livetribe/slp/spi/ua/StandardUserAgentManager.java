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
package org.livetribe.slp.spi.ua;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.livetribe.slp.Scopes;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.ServiceType;
import org.livetribe.slp.spi.StandardAgentManager;
import org.livetribe.slp.spi.msg.AttributeListExtension;
import org.livetribe.slp.spi.msg.DAAdvert;
import org.livetribe.slp.spi.msg.Message;
import org.livetribe.slp.spi.msg.SAAdvert;
import org.livetribe.slp.spi.msg.SrvRply;
import org.livetribe.slp.spi.msg.SrvRqst;
import org.livetribe.slp.spi.net.MessageListener;
import org.livetribe.slp.spi.net.TCPConnector;
import org.livetribe.slp.spi.net.UDPConnector;

/**
 * TODO: rework javadocs: they refer to StandardUserAgent, more than this class.
 * An SLP User Agent (UA) can discover SLP ServiceAgents (SAs) and SLP DirectoryAgents (DAs).
 * <br />
 * It caches the DAs discovered, and listens for multicast DAAdverts coming from new DAs
 * or from known DAs that changed (for example, rebooted).
 * UAs also listens for SAAdverts emitted by SAs.
 * <br />
 * UAs have scopes assigned so that they can discover services belonging to those scopes.
 *
 * @version $Rev$ $Date$
 */
public class StandardUserAgentManager extends StandardAgentManager implements UserAgentManager
{
    private UDPConnector notificationConnector;

    public UDPConnector getNotificationConnector()
    {
        return notificationConnector;
    }

    public void setNotificationConnector(UDPConnector notificationConnector)
    {
        this.notificationConnector = notificationConnector;
    }

    protected void doStart() throws IOException
    {
        super.doStart();
        if (getNotificationConnector() == null) setNotificationConnector(createNotificationConnector());
        configureNotificationConnector(getNotificationConnector());
        getNotificationConnector().start();
    }

    protected void doStop() throws IOException
    {
        UDPConnector connector = getNotificationConnector();
        if (connector != null) connector.stop();
        super.doStop();
    }

    protected UDPConnector createNotificationConnector() throws IOException
    {
        return createUDPConnector();
    }

    protected void configureNotificationConnector(UDPConnector connector)
    {
        configureUDPConnector(connector);
        connector.setPort(getNotificationPort());
    }

    public void addNotificationListener(MessageListener listener)
    {
        notificationConnector.addMessageListener(listener);
    }

    public void removeNotificationListener(MessageListener listener)
    {
        notificationConnector.removeMessageListener(listener);
    }

    public DAAdvert[] multicastDASrvRqst(Scopes scopes, String filter, String language, long timeframe) throws IOException
    {
        SrvRqst request = createSrvRqst(new ServiceType("service:directory-agent"), scopes, filter, language);
        request.setMulticast(true);
        return convergentDASrvRqst(request, timeframe);
    }

    public SAAdvert[] multicastSASrvRqst(Scopes scopes, String filter, String language, int timeframe) throws IOException
    {
        SrvRqst request = createSrvRqst(new ServiceType("service:service-agent"), scopes, filter, language);
        request.setMulticast(true);
        return convergentSASrvRqst(request, timeframe);
    }

    public SrvRply[] multicastSrvRqst(ServiceType serviceType, Scopes scopes, String filter, String language, int timeframe) throws IOException
    {
        SrvRqst request = createSrvRqst(serviceType, scopes, filter, language);
        request.setMulticast(true);
        return convergentSrvRqst(request, timeframe);
    }

    public SrvRply tcpSrvRqst(InetAddress address, ServiceType serviceType, Scopes scopes, String filter, String language) throws IOException
    {
        SrvRqst request = createSrvRqst(serviceType, scopes, filter, language);
        // Ask to send the Attributes as well (RFC 3059)
        request.addExtension(new AttributeListExtension());
        byte[] requestBytes = serializeMessage(request);

        TCPConnector connector = getTCPConnector();
        Socket socket = connector.send(requestBytes, address, false);
        byte[] replyBytes = connector.receive(socket);
        try
        {
            Message message = Message.deserialize(replyBytes);

            if (message.getMessageType() != Message.SRV_RPLY_TYPE) throw new AssertionError("BUG: expected SrvRply upon SrvRqst, received instead " + message);

            return (SrvRply)message;
        }
        catch (ServiceLocationException e)
        {
            throw new AssertionError("BUG: could not deserialize message " + replyBytes);
        }
        finally
        {
            closeNoExceptions(socket);
        }
    }

    private SrvRqst createSrvRqst(ServiceType serviceType, Scopes scopes, String filter, String language)
    {
        SrvRqst request = new SrvRqst();
        request.setLanguage(language);
        request.setXID(generateXID());
        request.setServiceType(serviceType);
        request.setScopes(scopes);
        request.setFilter(filter);
        return request;
    }
}
