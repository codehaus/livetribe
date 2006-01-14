/**
 *
 * Copyright 2005 (C) The original author or authors.
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
package org.livetribe.totem.single;

import java.net.InetAddress;

import org.activeio.Packet;
import org.activeio.net.DatagramContext;
import org.activeio.packet.FilterPacket;


/**
 * @version $Revision: $ $Date: $
 */
final class UDPFilterPacket extends FilterPacket
{
    private final InetAddress address;
    private final Integer port;

    public UDPFilterPacket(Packet next, InetAddress address, Integer port)
    {
        super(next);
        this.address = address;
        this.port = port;
    }

    public Object getAdapter(Class target)
    {
        if (target == DatagramContext.class)
        {
            return new DatagramContext(address, port);
        }
        return super.getAdapter(target);
    }

    public Packet filter(Packet packet)
    {
        return new UDPFilterPacket(packet, address, port);
    }
}
