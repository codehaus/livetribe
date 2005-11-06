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


/**
 * @version $Revision: 1.1 $ $Date: 2005/05/18 23:23:34 $
 */
public class ICMPEchoResponsePacket extends ICMPPacket
{
    /**
     * Constructs a datagram packet for sending packets of length
     * <code>length</code> with offset <code>ioffset</code>to the
     * specified port number on the specified host. The
     * <code>length</code> argument must be less than or equal to
     * <code>buf.length</code>.
     *
     * @param address the destination address.
     * @see java.net.InetAddress
     */
    public ICMPEchoResponsePacket( InetAddress address )
    {
        super( new byte[2], (byte)0, (byte) 0 );
    }
}
