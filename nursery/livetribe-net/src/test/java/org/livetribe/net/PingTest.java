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
package org.livetribe.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import junit.framework.TestCase;

import org.livetribe.net.icmp.ICMPEchoRequestPacket;
import org.livetribe.net.icmp.ICMPSocket;


/**
 * @version $Revision: 1.3 $ $Date: 2005/10/17 00:54:19 $
 */
public class PingTest extends TestCase
{
    public void testICMP() throws Exception
    {
        ICMPSocket socket = new ICMPSocket();
        socket.bind( InetAddress.getByName( "127.0.0.1" ) );

        byte[] data = new byte[1024];
        data[0] = (byte) 0xCA;
        data[1] = (byte) 0xFE;
        data[2] = (byte) 0xBA;
        data[3] = (byte) 0xBE;
        RawPacket packet = new RawPacket( data, data.length );
        packet.setAddress( InetAddress.getByName( "www.apache.org" ) );

        long start = System.currentTimeMillis();
        InetAddress.getByName( "www.apache.org" );
        long stop = System.currentTimeMillis();
        long diff = stop - start;

        try
        {
            socket.send( packet );
            fail( "Should only accept ICMP packets" );
        }
        catch ( IllegalArgumentException iae )
        {
        }

        ICMPEchoRequestPacket request = new ICMPEchoRequestPacket( InetAddress.getByName( "www.apache.org" ) );
        request.setIdentifier( (short) 1 );
        request.setSequence( (short) 1 );

        socket.send( request );

        socket.close();
    }

    public void testSimple() throws Exception
    {
        RawSocket socket = new RawSocket();
        socket.bind( InetAddress.getByName( "127.0.0.1" ) );

        byte[] data = new byte[1024];
        RawPacket packet = new RawPacket( data, data.length );
        packet.setAddress( InetAddress.getByName( "www.apache.org" ) );

        socket.send( packet );

        socket.close();
    }

    public void testExceptions() throws Exception
    {
        RawSocket socket = new RawSocket( InetAddress.getByName( "www.apache.org" ) );
        try
        {
            socket.bind( InetAddress.getByName( "127.0.0.1" ) );
            fail( "Socket should already be bound" );
        }
        catch ( SocketException e )
        {
        }
        socket.close();
    }

    public void XtestForLeak() throws Exception
    {
        long COUNT = 100000L;
        ICMPEchoRequestPacket request = new ICMPEchoRequestPacket( InetAddress.getByName( "www.apache.org" ) );
        request.setIdentifier( (short) 1 );
        request.setSequence( (short) 1 );


        for ( long i = 0; i < COUNT; i++ )
        {
            ICMPSocket socket = new ICMPSocket();
            socket.bind( InetAddress.getByName( "127.0.0.1" ) );

            try
            {
                socket.send( request );
            }
            catch ( IOException e )
            {
            }

            socket.close();
        }
    }
}
