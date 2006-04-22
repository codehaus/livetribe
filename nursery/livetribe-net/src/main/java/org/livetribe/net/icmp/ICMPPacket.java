/**
 *
 * Copyright 2005 (C) The original author or authors
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
package org.livetribe.net.icmp;

import java.net.InetAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.livetribe.net.RawPacket;
import org.livetribe.net.util.Util;


/**
 * @version $Revision: 1.2 $ $Date: 2005/10/17 00:54:19 $
 */
public abstract class ICMPPacket extends RawPacket
{
    private final byte type;
    private final byte code;
    private short identifier;
    private short sequence;

    protected ICMPPacket( byte[] buffer, byte type, byte code )
    {
        super( buffer, 0, buffer.length );

        this.type = type;
        this.code = code;

        buf[0] = type;
        buf[1] = code;
    }

    protected ICMPPacket( byte[] buffer, byte type, byte code, InetAddress address )
    {
        super( buffer, 0, buffer.length, address );

        this.type = type;
        this.code = code;

        buf[0] = type;
        buf[1] = code;
    }

    public byte getType()
    {
        return type;
    }

    public byte getCode()
    {
        return code;
    }

    public short getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier( short identifier )
    {
        this.identifier = identifier;

        buf[6] = (byte) ( identifier >> 8 );
        buf[7] = (byte) ( identifier & 0xFF );
    }

    public short getSequence()
    {
        return sequence;
    }

    public void setSequence( short sequence )
    {
        this.sequence = sequence;

        buf[8] = (byte) ( sequence >> 8 );
        buf[9] = (byte) ( sequence & 0xFF );
    }

    /**
     * Sets the checksum of the packet
     */
    public synchronized void generateChecksum()
    {
        Util.uncheckedGenerateChecksum( buf, offset, length, offset + 2 );
    }

    static
    {
        AccessController.doPrivileged( new PrivilegedAction()
        {
            public Object run()
            {
                System.loadLibrary( "livetribe_net" );
                return null;
            }
        } );
        init();
    }

    private native static void init();
}

