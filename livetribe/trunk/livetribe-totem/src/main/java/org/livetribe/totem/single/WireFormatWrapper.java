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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import org.activeio.Packet;
import org.activeio.command.WireFormat;


/**
 * @version $Revision: $ $Date: $
 */
class WireFormatWrapper implements WireFormat
{
    private final WireFormat wireFormat;
    private final InetAddress address;
    private final int port;

    WireFormatWrapper(WireFormat wireFormat, InetAddress address, int port)
    {
        this.wireFormat = wireFormat;
        this.address = address;
        this.port = port;
    }

    public Packet marshal(Object command) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(baos);
        marshal(command, ds);
        ds.close();
        return new UDPFilterPacket(wireFormat.marshal(command), address, new Integer(port));
    }

    public Object unmarshal(Packet packet) throws IOException
    {
        return wireFormat.unmarshal(packet);
    }

    public void marshal(Object command, DataOutputStream out) throws IOException
    {
        wireFormat.marshal(command, out);
    }

    public Object unmarshal(DataInputStream in) throws IOException
    {
        return wireFormat.unmarshal(in);
    }

    public void setVersion(int version)
    {
        wireFormat.setVersion(version);
    }

    public int getVersion()
    {
        return wireFormat.getVersion();
    }

}
