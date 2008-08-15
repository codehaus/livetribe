/*
 * Copyright 2005-2008 the original author or authors
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
package org.livetribe.slp.sa;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.SLPError;
import org.livetribe.slp.Scopes;
import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.settings.Defaults;
import org.livetribe.slp.settings.Factories;
import static org.livetribe.slp.settings.Keys.*;
import org.livetribe.slp.settings.Settings;
import org.livetribe.slp.spi.msg.SrvAck;
import org.livetribe.slp.spi.net.NetUtils;
import org.livetribe.slp.spi.net.TCPConnector;
import org.livetribe.slp.spi.net.UDPConnector;
import org.livetribe.slp.spi.sa.TCPSrvDeRegPerformer;
import org.livetribe.slp.spi.sa.TCPSrvRegPerformer;
import org.livetribe.slp.spi.sa.UDPSrvDeRegPerformer;
import org.livetribe.slp.spi.sa.UDPSrvRegPerformer;

/**
 * @version $Revision$ $Date$
 */
public class StandardServiceAgentClient implements ServiceAgentClient
{
    public static StandardServiceAgentClient newInstance(Settings settings)
    {
        UDPConnector.Factory udpFactory = Factories.newInstance(settings, UDP_CONNECTOR_FACTORY_KEY);
        TCPConnector.Factory tcpFactory = Factories.newInstance(settings, TCP_CONNECTOR_FACTORY_KEY);
        return new StandardServiceAgentClient(udpFactory.newUDPConnector(settings), tcpFactory.newTCPConnector(settings), settings);
    }

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final UDPSrvRegPerformer udpSrvReg;
    private final TCPSrvRegPerformer tcpSrvReg;
    private final UDPSrvDeRegPerformer udpSrvDeReg;
    private final TCPSrvDeRegPerformer tcpSrvDeReg;
    private int port = Defaults.get(PORT_KEY);
    private String connectAddress = Defaults.get(SA_CLIENT_CONNECT_ADDRESS);
    private boolean useTCP = Defaults.get(SA_CLIENT_USE_TCP);

    public StandardServiceAgentClient(UDPConnector udpConnector, TCPConnector tcpConnector)
    {
        this(udpConnector, tcpConnector, null);
    }

    public StandardServiceAgentClient(UDPConnector udpConnector, TCPConnector tcpConnector, Settings settings)
    {
        this.udpSrvReg = new UDPSrvRegPerformer(udpConnector, settings);
        this.tcpSrvReg = new TCPSrvRegPerformer(tcpConnector, settings);
        this.udpSrvDeReg = new UDPSrvDeRegPerformer(udpConnector, settings);
        this.tcpSrvDeReg = new TCPSrvDeRegPerformer(tcpConnector, settings);
        if (settings != null) setSettings(settings);
    }

    private void setSettings(Settings settings)
    {
        if (settings.containsKey(PORT_KEY)) this.port = settings.get(PORT_KEY);
        if (settings.containsKey(SA_CLIENT_CONNECT_ADDRESS))
            this.connectAddress = settings.get(SA_CLIENT_CONNECT_ADDRESS);
        if (settings.containsKey(SA_CLIENT_USE_TCP)) this.useTCP = settings.get(SA_CLIENT_USE_TCP);
    }

    public int getPort()
    {
        return port;
    }

    /**
     * Sets the SLP port
     *
     * @param port the new SLP port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    public String getConnectAddress()
    {
        return connectAddress;
    }

    /**
     * Sets the IP address to which this client connects to
     *
     * @param connectAddress the new connect address
     */
    public void setConnectAddress(String connectAddress)
    {
        this.connectAddress = connectAddress;
    }

    public boolean isUseTCP()
    {
        return useTCP;
    }

    public void setUseTCP(boolean useTCP)
    {
        this.useTCP = useTCP;
    }

    public void register(ServiceInfo service) throws ServiceLocationException
    {
        register(service, false);
    }

    public void addAttributes(ServiceURL serviceURL, String language, Attributes attributes) throws ServiceLocationException
    {
        ServiceInfo service = new ServiceInfo(serviceURL, language, Scopes.NONE, attributes);
        register(service, true);
    }

    protected void register(ServiceInfo service, boolean update)
    {
        InetSocketAddress remoteAddress = new InetSocketAddress(NetUtils.getByName(connectAddress), port);

        SrvAck ack = useTCP ? tcpSrvReg.perform(remoteAddress, service, update) : udpSrvReg.perform(remoteAddress, service, update);

        int errorCode = ack.getErrorCode();
        if (errorCode != 0)
            throw new ServiceLocationException("Could not register service " + service + " to ServiceAgent server", SLPError.from(errorCode));

        if (logger.isLoggable(Level.FINE))
            logger.fine("Registered service " + service + " to ServiceAgent server");
    }

    public void removeAttributes(ServiceURL serviceURL, String language, Attributes attributes) throws ServiceLocationException
    {
        ServiceInfo service = new ServiceInfo(serviceURL, language, Scopes.NONE, attributes);
        deregister(service, true);
    }

    public void deregister(ServiceURL serviceURL, String language) throws ServiceLocationException
    {
        ServiceInfo service = new ServiceInfo(serviceURL, language, Scopes.NONE, Attributes.NONE);
        deregister(service, false);
    }

    protected void deregister(ServiceInfo service, boolean update) throws ServiceLocationException
    {
        InetSocketAddress remoteAddress = new InetSocketAddress(NetUtils.getByName(connectAddress), port);

        SrvAck ack = useTCP ? tcpSrvDeReg.perform(remoteAddress, service, update) : udpSrvDeReg.perform(remoteAddress, service, update);

        int errorCode = ack.getErrorCode();
        if (errorCode != 0)
            throw new ServiceLocationException("Could not deregister service " + service + " from ServiceAgent server", SLPError.from(errorCode));

        if (logger.isLoggable(Level.FINE))
            logger.fine("Deregistered service " + service + " from ServiceAgent server");
    }

    public static class Factory implements ServiceAgentClient.Factory
    {
        public ServiceAgentClient newServiceAgentClient(Settings settings)
        {
            return newInstance(settings);
        }
    }
}
