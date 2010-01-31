/*
 * Copyright 2008-2008 the original author or authors
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
package org.livetribe.slp.spi.sa;

import java.net.InetSocketAddress;

import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.settings.Defaults;
import static org.livetribe.slp.settings.Keys.MAX_TRANSMISSION_UNIT_KEY;
import org.livetribe.slp.settings.Settings;
import org.livetribe.slp.spi.msg.Message;
import org.livetribe.slp.spi.msg.SrvAck;
import org.livetribe.slp.spi.msg.SrvDeReg;
import org.livetribe.slp.spi.net.TCPConnector;
import org.livetribe.slp.spi.net.UDPConnector;

/**
 * @version $Revision$ $Date$
 */
public class UnicastSrvDeRegPerformer extends SrvDeRegPerformer
{
    private final UDPConnector udpConnector;
    private final TCPConnector tcpConnector;
    private int maxTransmissionUnit = Defaults.get(MAX_TRANSMISSION_UNIT_KEY);

    public UnicastSrvDeRegPerformer(UDPConnector udpConnector, TCPConnector tcpConnector, Settings settings)
    {
        this.udpConnector = udpConnector;
        this.tcpConnector = tcpConnector;
        if (settings != null) setSettings(settings);
    }

    private void setSettings(Settings settings)
    {
        if (settings.containsKey(MAX_TRANSMISSION_UNIT_KEY))
            this.maxTransmissionUnit = settings.get(MAX_TRANSMISSION_UNIT_KEY);
    }

    public SrvAck perform(InetSocketAddress remoteAddress, boolean preferTCP, ServiceInfo service, boolean update)
    {
        SrvDeReg srvDeReg = newSrvDeReg(service, update);
        byte[] srvDeRegBytes = srvDeReg.serialize();
        byte[] replyBytes = null;
        if (preferTCP || srvDeRegBytes.length > maxTransmissionUnit)
            replyBytes = tcpConnector.writeAndRead(remoteAddress, srvDeRegBytes);
        else
            replyBytes = udpConnector.sendAndReceive(remoteAddress, srvDeRegBytes);
        return (SrvAck)Message.deserialize(replyBytes);
    }
}
