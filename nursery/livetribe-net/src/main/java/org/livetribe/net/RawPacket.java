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

import java.net.InetAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.io.Serializable;


/**
 * This class represents a raw packet.
 * <p/>
 * Raw packets are used to implement a connectionless packet
 * delivery service. Each message is routed from one machine to
 * another based solely on information contained within that packet.
 * Multiple packets sent from one machine to another might be routed
 * differently, and might arrive in any order. Packet delivery is
 * not guaranteed.
 *
 * @version $Revision: 1.3 $ $Date: 2005/10/17 00:54:19 $
 */
public class RawPacket implements Serializable
{
    /*
     * The fields of this class are package-private since RawSocketImpl
     * classes needs to access them.
     */
    protected byte[] buf;
    protected int offset;
    protected int length;
    InetAddress address;

    /**
     * Constructs a <code>RawPacket</code> for receiving packets of
     * length <code>length</code>, specifying an offset into the buffer.
     * <p/>
     * The <code>length</code> argument must be less than or equal to
     * <code>buf.length</code>.
     *
     * @param buf    buffer for holding the incoming raw packet.
     * @param offset the offset for the buffer
     * @param length the number of bytes to read.
     */
    public RawPacket( byte buf[], int offset, int length )
    {
        setData( buf, offset, length );
        this.address = null;
    }

    /**
     * Constructs a <code>RawPacket</code> for receiving packets of
     * length <code>length</code>.
     * <p/>
     * The <code>length</code> argument must be less than or equal to
     * <code>buf.length</code>.
     *
     * @param buf    buffer for holding the incoming raw packet.
     * @param length the number of bytes to read.
     */
    public RawPacket( byte buf[], int length )
    {
        this( buf, 0, length );
    }

    /**
     * Constructs a raw packet for sending packets of length
     * <code>length</code> with offset <code>ioffset</code>to the
     * specified port number on the specified host. The
     * <code>length</code> argument must be less than or equal to
     * <code>buf.length</code>.
     *
     * @param buf     the packet data.
     * @param offset  the packet data offset.
     * @param length  the packet data length.
     * @param address the destination address.
     * @see java.net.InetAddress
     */
    public RawPacket( byte buf[], int offset, int length, InetAddress address )
    {
        setData( buf, offset, length );
        setAddress( address );
    }

    /**
     * Returns the IP address of the machine to which this raw packet is being
     * sent or from which the raw packet was received.
     *
     * @return the IP address of the machine to which this raw packet is being
     *         sent or from which the raw packet was received.
     * @see java.net.InetAddress
     * @see #setAddress(java.net.InetAddress)
     */
    public synchronized InetAddress getAddress()
    {
        return address;
    }

    /**
     * Returns the data buffer. The data received or the data to be sent
     * starts from the <code>offset</code> in the buffer,
     * and runs for <code>length</code> long.
     *
     * @return the buffer used to receive or  send data
     * @see #setData(byte[], int, int)
     */
    public synchronized byte[] getData()
    {
        return buf;
    }

    /**
     * Returns the offset of the data to be sent or the offset of the
     * data received.
     *
     * @return the offset of the data to be sent or the offset of the
     *         data received.
     */
    public synchronized int getOffset()
    {
        return offset;
    }

    /**
     * Returns the length of the data to be sent or the length of the
     * data received.
     *
     * @return the length of the data to be sent or the length of the
     *         data received.
     * @see #setLength(int)
     */
    public synchronized int getLength()
    {
        return length;
    }

    /**
     * Set the data buffer for this packet. This sets the
     * data, length and offset of the packet.
     *
     * @param buf    the buffer to set for this packet
     * @param offset the offset into the data
     * @param length the length of the data
     *               and/or the length of the buffer used to receive data
     * @throws NullPointerException if the argument is null
     * @see #getData
     * @see #getOffset
     * @see #getLength
     */
    public synchronized void setData( byte[] buf, int offset, int length )
    {
        if ( buf == null ) throw new NullPointerException( "Null pointer for buf" );

        /* this will check to see if buf is null */
        if ( length < 0 || offset < 0 ||
             ( ( length + offset ) > buf.length ) )
        {
            throw new IllegalArgumentException( "illegal length or offset" );
        }
        this.buf = buf;
        this.length = length;
        this.offset = offset;
    }

    /**
     * Sets the IP address of the machine to which this raw packet
     * is being sent.
     *
     * @param iaddr the <code>InetAddress</code>
     * @see #getAddress()
     */
    public synchronized void setAddress( InetAddress iaddr )
    {
        address = iaddr;
    }

    /**
     * Set the data buffer for this packet. With the offset of
     * this RawPacket set to 0, and the length set to
     * the length of <code>buf</code>.
     *
     * @param buf the buffer to set for this packet.
     * @throws NullPointerException if the argument is null.
     * @see #getLength
     * @see #getData
     */
    public synchronized void setData( byte[] buf )
    {
        if ( buf == null )
        {
            throw new NullPointerException( "null packet buffer" );
        }
        this.buf = buf;
        this.offset = 0;
        this.length = buf.length;
    }

    /**
     * Set the length for this packet. The length of the packet is
     * the number of bytes from the packet's data buffer that will be
     * sent, or the number of bytes of the packet's data buffer that
     * will be used for receiving data. The length must be lesser or
     * equal to the offset plus the length of the packet's buffer.
     *
     * @param length the length to set for this packet.
     * @throws IllegalArgumentException if the length is negative
     *                                  of if the length is greater than the packet's data buffer
     *                                  length.
     * @see #getLength
     * @see #setData
     */
    public synchronized void setLength( int length )
    {
        if ( ( length + offset ) > buf.length || length < 0 )
        {
            throw new IllegalArgumentException( "illegal length" );
        }
        this.length = length;
    }

    /**
     * Sets the checksum of the packet
     */
    public synchronized void generateChecksum() {
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

    /**
     * Perform class load-time initializations.
     */
    private native static void init();
}
