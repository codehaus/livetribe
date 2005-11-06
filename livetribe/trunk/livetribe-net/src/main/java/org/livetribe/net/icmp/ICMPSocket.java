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

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.livetribe.net.RawPacket;
import org.livetribe.net.RawSocket;


/**
 * @version $Revision: 1.1 $ $Date: 2005/05/18 23:23:34 $
 */
public final class ICMPSocket extends RawSocket
{
    public ICMPSocket() throws SocketException
    {
        super( 1, null );
    }

    public ICMPSocket( InetAddress bindaddr ) throws SocketException
    {
        super( 1, bindaddr );
    }

    public void send( RawPacket p ) throws IOException
    {
        if ( !( p instanceof ICMPPacket ) ) throw new IllegalArgumentException( "Packet must be ICMPPacket" );

        super.send( p );
    }
}
