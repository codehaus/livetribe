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

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketOptions;


/**
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
public abstract class RawSocketImpl implements SocketOptions
{
    /**
     * IP protocol number
     */
    protected int protocol;

    /**
     * The file descriptor object.
     */
    protected FileDescriptor fileDescriptor;

    /**
     * Creates a raw socket.
     *
     * @param protocol IP protocol number
     * @throws java.net.SocketException if there is an error in the
     *                                  underlying protocol, such as a TCP error.
     */
    abstract void create( int protocol ) throws SocketException;

    /**
     * Binds a raw socket to a local address.
     *
     * @param laddr the local address
     * @throws SocketException if there is an error in the
     *                         underlying protocol, such as a TCP error.
     */
    abstract void bind( byte[] laddr ) throws SocketException;

    /**
     * Sends a raw packet. The packet contains the data and the
     * destination address to send the packet to.
     *
     * @param packet the packet to be sent.
     * @throws java.io.IOException if an I/O exception occurs while sending the
     *                             raw packet.
     */
    abstract void send( RawPacket packet ) throws IOException;

    /**
     * Peek at the packet to see who it is from.
     *
     * @return the address which the packet came from.
     * @throws IOException if an I/O exception occurs
     */
    abstract byte[] peek() throws IOException;

    /**
     * Receive the raw packet.
     *
     * @param p the Packet Received.
     * @throws IOException if an I/O exception occurs
     *                     while receiving the raw packet.
     */
    abstract void receive( RawPacket p ) throws IOException;

    /**
     * Close the socket.
     */
    abstract void close();

    int getProtocol()
    {
        return protocol;
    }

    /**
     * Gets the raw socket file descriptor.
     *
     * @return a <tt>FileDescriptor</tt> object representing the raw socket
     *         file descriptor
     */
    FileDescriptor getFileDescriptor()
    {
        return fileDescriptor;
    }
}