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
package org.livetribe.slp.spi;

import java.net.InetSocketAddress;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.SLPError;
import org.livetribe.slp.settings.Defaults;
import static org.livetribe.slp.settings.Keys.MAX_TRANSMISSION_UNIT_KEY;
import org.livetribe.slp.settings.Settings;
import org.livetribe.slp.spi.msg.AttrRply;
import org.livetribe.slp.spi.msg.Message;
import org.livetribe.slp.spi.net.UDPConnector;

/**
 * @version $Revision$ $Date$
 */
public class UDPAttrRplyPerformer extends AttrRplyPerformer
{
    private final UDPConnector udpConnector;
    private int maxTransmissionUnit = Defaults.get(MAX_TRANSMISSION_UNIT_KEY);

    public UDPAttrRplyPerformer(UDPConnector udpConnector, Settings settings)
    {
        this.udpConnector = udpConnector;
        if (settings != null) setSettings(settings);
    }

    private void setSettings(Settings settings)
    {
        if (settings.containsKey(MAX_TRANSMISSION_UNIT_KEY))
            this.maxTransmissionUnit = settings.get(MAX_TRANSMISSION_UNIT_KEY);
    }

    public void perform(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Message message, Attributes attributes)
    {
        AttrRply attrRply = newAttrRply(message, attributes, maxTransmissionUnit);
        byte[] attrRplyBytes = attrRply.serialize();
        send(localAddress, remoteAddress, attrRplyBytes);
    }

    public void perform(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Message message, SLPError error)
    {
        AttrRply attrRply = newAttrRply(message, error);
        byte[] attrRplyBytes = attrRply.serialize();
        send(localAddress, remoteAddress, attrRplyBytes);
    }

    protected void send(InetSocketAddress localAddress, InetSocketAddress remoteAddress, byte[] attrRplyBytes)
    {
        udpConnector.send(localAddress.getAddress().getHostAddress(), remoteAddress, attrRplyBytes);
    }
}
