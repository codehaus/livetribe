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
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketOptions;
import java.security.AccessController;

import sun.security.action.GetPropertyAction;


/**
 * @version $Revision: 1.2 $ $Date: 2005/10/17 00:54:19 $
 */
public class RawSocket
{
    /**
     * Various states of this socket.
     */
    private boolean bound = false;
    private boolean closed = false;
    private Object closeLock = new Object();

    /*
     * The implementation of this DatagramSocket.
     */
    final RawSocketImpl impl;

    /**
     * Constructs a raw socket and binds it to any available interface
     * on the local host machine.  The socket will be bound to the wildcard
     * address, an IP address chosen by the kernel.
     * <p/>
     * <p>If there is a security manager,
     * its <code>checkListen</code> method is first called
     * with 0 as its argument to ensure the operation is allowed.
     * This could result in a SecurityException.
     *
     * @throws SocketException   if the socket could not be opened,
     *                           or the socket could not bind to the specified local address.
     * @throws SecurityException if a security manager exists and its
     *                           <code>checkListen</code> method doesn't allow the operation.
     * @see SecurityManager#checkListen
     */
    public RawSocket() throws SocketException
    {
        this( 0, null );
    }

    /**
     * Creates a raw socket, bound to the specified local
     * socket address.
     * <p/>
     * If, if the address is <code>null</code>, creates an unbound socket.
     * <p/>
     * <p>If there is a security manager,
     * its <code>checkListen</code> method is first called
     * with the port from the socket address
     * as its argument to ensure the operation is allowed.
     * This could result in a SecurityException.
     *
     * @param bindaddr local socket address to bind, or <code>null</code>
     *                 for an unbound socket.
     * @throws SocketException   if the socket could not be opened,
     *                           or the socket could not bind to the specified local port.
     * @throws SecurityException if a security manager exists and its
     *                           <code>checkListen</code> method doesn't allow the operation.
     * @see SecurityManager#checkListen
     * @since 1.4
     */
    public RawSocket( InetAddress bindaddr ) throws SocketException
    {
        this( 0, bindaddr );
    }

    public RawSocket( int protocol, InetAddress bindaddr ) throws SocketException
    {
        try
        {
            impl = (RawSocketImpl) implClass.newInstance();
            impl.create( protocol );

            if ( bindaddr != null )
            {
                bind( bindaddr );
            }
        }
        catch ( Exception e )
        {
            throw new SocketException( "can't instantiate RawSocketImpl" );
        }
    }


    /**
     * Binds this DatagramSocket to a specific address.
     * <p/>
     * If the address is <code>null</code>, then the system will pick up
     * an ephemeral port and a valid local address to bind the socket.
     * <p/>
     *
     * @throws IllegalArgumentException if addr is a SocketAddress subclass
     *                                  not supported by this socket.
     * @param	addr The address to bind to.
     * @throws	SocketException if any error happens during the bind, or if the
     * socket is already bound.
     * @throws	SecurityException if a security manager exists and its
     * <code>checkListen</code> method doesn't allow the operation.
     * @since 1.4
     */
    public synchronized void bind( InetAddress addr ) throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        if ( isBound() )
        {
            throw new SocketException( "already bound" );
        }
        InetSocketAddress epoint = new InetSocketAddress( addr, 0 );
        if ( epoint.isUnresolved() )
        {
            throw new SocketException( "Unresolved address" );
        }
        impl.bind( epoint.getAddress().getAddress() );
        bound = true;
    }

    /**
     * Returns the binding state of the socket.
     *
     * @return true if the socket succesfuly bound to an address
     * @since 1.4
     */
    public boolean isBound()
    {
        return bound;
    }


    /**
     * Sends a raw packet from this socket. The
     * <code>RawPacket</code> includes information indicating the
     * data to be sent, its length, the IP address of the remote host,
     * and the port number on the remote host.
     * <p/>
     * <p>If there is a security manager, and the socket is not currently
     * connected to a remote address, this method first performs some
     * security checks. First, if <code>p.getAddress().isMulticastAddress()</code>
     * is true, this method calls the
     * security manager's <code>checkMulticast</code> method
     * with <code>p.getAddress()</code> as its argument.
     * If the evaluation of that expression is false,
     * this method instead calls the security manager's
     * <code>checkConnect</code> method with arguments
     * <code>p.getAddress().getHostAddress()</code> and
     * <code>p.getPort()</code>. Each call to a security manager method
     * could result in a SecurityException if the operation is not allowed.
     *
     * @param p the <code>RawPacket</code> to be sent.
     * @throws IOException       if an I/O error occurs.
     * @throws SecurityException if a security manager exists and its
     *                           <code>checkMulticast</code> or <code>checkConnect</code>
     *                           method doesn't allow the send.
     * @see RawPacket
     * @see SecurityManager#checkMulticast(InetAddress)
     * @see SecurityManager#checkConnect
     */
    public void send( RawPacket p ) throws IOException
    {
        if ( p == null ) throw new NullPointerException( "Null pointer for RawPacket" );
        if ( p.getAddress() == null ) throw new NullPointerException( "RawPacket has no address" );

        synchronized ( p )
        {
            if ( isClosed() ) throw new SocketException( "Socket is closed" );

            SecurityManager security = System.getSecurityManager();

            // The reason you want to synchronize on raw packet
            // is because you dont want an applet to change the address
            // while you are trying to send the packet for example
            // after the security check but before the send.
            if ( security != null )
            {
                if ( p.getAddress().isMulticastAddress() )
                {
                    security.checkMulticast( p.getAddress() );
                }
                else
                {
                    security.checkConnect( p.getAddress().getHostAddress(), -1 );
                }
            }

            if ( !isBound() )
            {
                bind( null );
            }

            p.generateChecksum();

            impl.send( p );
        }
    }

    /**
     * Receives a raw packet from this socket. When this method
     * returns, the <code>RawPacket</code>'s buffer is filled with
     * the data received. The raw packet also contains the sender's
     * IP address, and the port number on the sender's machine.
     * <p/>
     * This method blocks until a raw is received. The
     * <code>length</code> field of the raw packet object contains
     * the length of the received message. If the message is longer than
     * the packet's length, the message is truncated.
     * <p/>
     * If there is a security manager, a packet cannot be received if the
     * security manager's <code>checkAccept</code> method
     * does not allow it.
     *
     * @param p the <code>RawPacket</code> into which to place
     *          the incoming data.
     * @throws IOException if an I/O error occurs.
     * @throws java.net.SocketTimeoutException
     *                     if setSoTimeout was previously called
     *                     and the timeout has expired.
     * @see RawPacket
     */
    public synchronized void receive( RawPacket p ) throws IOException
    {
        if ( p == null ) throw 	new NullPointerException( "Null pointer for RawPacket" );

        synchronized ( p )
        {
            if ( !isBound() )
            {
                bind( null );
            }
            // check the address is ok with the security manager before every recv.
            SecurityManager security = System.getSecurityManager();
            if ( security != null )
            {
                while ( true )
                {
                    byte[] addr = impl.peek();

                    try
                    {
                        security.checkConnect( InetAddress.getByAddress( addr ).getHostAddress(), -1 );
                        // security check succeeded - so now break
                        // and recv the packet.
                        break;
                    }
                    catch ( SecurityException se )
                    {
                        // Throw away the offending packet by consuming
                        // it in a tmp buffer.
                        RawPacket tmp = new RawPacket( new byte[1], 1 );
                        impl.receive( tmp );

                        // silently discard the offending packet
                        // and continue: unknown/malicious
                        // entities on nets should not make
                        // runtime throw security exception and
                        // disrupt the applet by sending random
                        // raw packets.
                    }
                }
            }

            // If the security check succeeds, or the raw is
            // connected then receive the packet
            impl.receive( p );
        }
    }

    /**
     * Gets the local address to which the socket is bound.
     * <p/>
     * <p>If there is a security manager, its
     * <code>checkConnect</code> method is first called
     * with the host address and <code>-1</code>
     * as its arguments to see if the operation is allowed.
     *
     * @return the local address to which the socket is bound, or
     *         an <code>InetAddress</code> representing any local
     *         address if either the socket is not bound, or
     *         the security manager <code>checkConnect</code>
     *         method does not allow the operation
     * @see SecurityManager#checkConnect
     */
    public InetAddress getLocalAddress()
    {
        if ( isClosed() )
        {
            return null;
        }
        InetAddress in = null;
        try
        {
            in = (InetAddress) impl.getOption( SocketOptions.SO_BINDADDR );
            if ( in.isAnyLocalAddress() )
            {
                in = new InetSocketAddress( 0 ).getAddress();
            }
            SecurityManager s = System.getSecurityManager();
            if ( s != null )
            {
                s.checkConnect( in.getHostAddress(), -1 );
            }
        }
        catch ( Exception e )
        {
            in = new InetSocketAddress( 0 ).getAddress(); // "0.0.0.0"
        }
        return in;
    }

    /**
     * Enable/disable SO_TIMEOUT with the specified timeout, in
     * milliseconds. With this option set to a non-zero timeout,
     * a call to receive() for this DatagramSocket
     * will block for only this amount of time.  If the timeout expires,
     * a <B>java.net.SocketTimeoutException</B> is raised, though the
     * DatagramSocket is still valid.  The option <B>must</B> be enabled
     * prior to entering the blocking operation to have effect.  The
     * timeout must be > 0.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param timeout the specified timeout in milliseconds.
     * @throws SocketException if there is an error in the underlying protocol, such as an UDP error.
     * @see #getSoTimeout()
     */
    public synchronized void setSoTimeout( int timeout ) throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        impl.setOption( SocketOptions.SO_TIMEOUT, new Integer( timeout ) );
    }

    /**
     * Retrive setting for SO_TIMEOUT.  0 returns implies that the
     * option is disabled (i.e., timeout of infinity).
     *
     * @return the setting for SO_TIMEOUT
     * @throws SocketException if there is an error in the underlying protocol, such as an UDP error.
     * @see #setSoTimeout(int)
     */
    public synchronized int getSoTimeout() throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        if ( impl == null )
        {
            return 0;
        }
        Object o = impl.getOption( SocketOptions.SO_TIMEOUT );
        /* extra type safety */
        if ( o instanceof Integer )
        {
            return ( (Integer) o ).intValue();
        }
        else
        {
            return 0;
        }
    }

    /**
     * Sets the SO_SNDBUF option to the specified value for this
     * <tt>DatagramSocket</tt>. The SO_SNDBUF option is used by the
     * network implementation as a hint to size the underlying
     * network I/O buffers. The SO_SNDBUF setting may also be used
     * by the network implementation to determine the maximum size
     * of the packet that can be sent on this socket.
     * <p/>
     * As SO_SNDBUF is a hint, applications that want to verify
     * what size the buffer is should call {@link #getSendBufferSize()}.
     * <p/>
     * Increasing the buffer size may allow multiple outgoing packets
     * to be queued by the network implementation when the send rate
     * is high.
     * <p/>
     * Note: If {@link #send(RawPacket)} is used to send a
     * <code>RawPacket</code> that is larger than the setting
     * of SO_SNDBUF then it is implementation specific if the
     * packet is sent or discarded.
     *
     * @param size the size to which to set the send buffer
     *             size. This value must be greater than 0.
     * @throws SocketException          if there is an error
     *                                  in the underlying protocol, such as an UDP error.
     * @throws IllegalArgumentException if the value is 0 or is
     *                                  negative.
     * @see #getSendBufferSize()
     */
    public synchronized void setSendBufferSize( int size ) throws SocketException
    {
        if ( !( size > 0 ) )
        {
            throw new IllegalArgumentException( "negative send size" );
        }
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        impl.setOption( SocketOptions.SO_SNDBUF, new Integer( size ) );
    }

    /**
     * Get value of the SO_SNDBUF option for this <tt>DatagramSocket</tt>, that is the
     * buffer size used by the platform for output on this <tt>DatagramSocket</tt>.
     *
     * @return the value of the SO_SNDBUF option for this <tt>DatagramSocket</tt>
     * @throws SocketException if there is an error in
     *                         the underlying protocol, such as an UDP error.
     * @see #setSendBufferSize
     */
    public synchronized int getSendBufferSize() throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        int result = 0;
        Object o = impl.getOption( SocketOptions.SO_SNDBUF );
        if ( o instanceof Integer )
        {
            result = ( (Integer) o ).intValue();
        }
        return result;
    }

    /**
     * Sets the SO_RCVBUF option to the specified value for this
     * <tt>DatagramSocket</tt>. The SO_RCVBUF option is used by the
     * the network implementation as a hint to size the underlying
     * network I/O buffers. The SO_RCVBUF setting may also be used
     * by the network implementation to determine the maximum size
     * of the packet that can be received on this socket.
     * <p/>
     * Because SO_RCVBUF is a hint, applications that want to
     * verify what size the buffers were set to should call
     * {@link #getReceiveBufferSize()}.
     * <p/>
     * Increasing SO_RCVBUF may allow the network implementation
     * to buffer multiple packets when packets arrive faster than
     * are being received using {@link #receive(RawPacket)}.
     * <p/>
     * Note: It is implementation specific if a packet larger
     * than SO_RCVBUF can be received.
     *
     * @param size the size to which to set the receive buffer
     *             size. This value must be greater than 0.
     * @throws SocketException          if there is an error in
     *                                  the underlying protocol, such as an UDP error.
     * @throws IllegalArgumentException if the value is 0 or is
     *                                  negative.
     * @see #getReceiveBufferSize()
     */
    public synchronized void setReceiveBufferSize( int size ) throws SocketException
    {
        if ( size <= 0 )
        {
            throw new IllegalArgumentException( "invalid receive size" );
        }
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        impl.setOption( SocketOptions.SO_RCVBUF, new Integer( size ) );
    }

    /**
     * Get value of the SO_RCVBUF option for this <tt>DatagramSocket</tt>, that is the
     * buffer size used by the platform for input on this <tt>DatagramSocket</tt>.
     *
     * @return the value of the SO_RCVBUF option for this <tt>DatagramSocket</tt>
     * @throws SocketException if there is an error in the underlying protocol, such as an UDP error.
     * @see #setReceiveBufferSize(int)
     */
    public synchronized int getReceiveBufferSize() throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        int result = 0;
        Object o = impl.getOption( SocketOptions.SO_RCVBUF );
        if ( o instanceof Integer )
        {
            result = ( (Integer) o ).intValue();
        }
        return result;
    }

    /**
     * Enable/disable the SO_REUSEADDR socket option.
     * <p/>
     * For UDP sockets it may be necessary to bind more than one
     * socket to the same socket address. This is typically for the
     * purpose of receiving multicast packets
     * (See {@link java.net.MulticastSocket}). The
     * <tt>SO_REUSEADDR</tt> socket option allows multiple
     * sockets to be bound to the same socket address if the
     * <tt>SO_REUSEADDR</tt> socket option is enabled prior
     * to binding the socket using {@link #bind(InetAddress)}.
     * <p/>
     * When a <tt>DatagramSocket</tt> is created the initial setting
     * of <tt>SO_REUSEADDR</tt> is disabled.
     * <p/>
     * The behaviour when <tt>SO_REUSEADDR</tt> is enabled or
     * disabled after a socket is bound (See {@link #isBound()})
     * is not defined.
     *
     * @param on whether to enable or disable the
     * @throws SocketException if an error occurs enabling or
     *                         disabling the <tt>SO_RESUEADDR</tt> socket option,
     *                         or the socket is closed.
     * @see #getReuseAddress()
     * @see #bind(InetAddress)
     * @see #isBound()
     * @see #isClosed()
     */
    public synchronized void setReuseAddress( boolean on ) throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        // Integer instead of Boolean for compatibility with older DatagramSocketImpl
        impl.setOption( SocketOptions.SO_REUSEADDR, new Boolean( on ) );

    }

    /**
     * Tests if SO_REUSEADDR is enabled.
     *
     * @return a <code>boolean</code> indicating whether or not SO_REUSEADDR is enabled.
     * @throws SocketException if there is an error
     *                         in the underlying protocol, such as an UDP error.
     * @see #setReuseAddress(boolean)
     * @since 1.4
     */
    public synchronized boolean getReuseAddress() throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        Object o = impl.getOption( SocketOptions.SO_REUSEADDR );
        return ( (Boolean) o ).booleanValue();
    }

    /**
     * Sets traffic class or type-of-service octet in the IP
     * raw header for datagrams sent from this DatagramSocket.
     * As the underlying network implementation may ignore this
     * value applications should consider it a hint.
     * <p/>
     * <P> The tc <B>must</B> be in the range <code> 0 <= tc <=
     * 255</code> or an IllegalArgumentException will be thrown.
     * <p>Notes:
     * <p> for Internet Protocol v4 the value consists of an octet
     * with precedence and TOS fields as detailed in RFC 1349. The
     * TOS field is bitset created by bitwise-or'ing values such
     * the following :-
     * <p/>
     * <UL>
     * <LI><CODE>IPTOS_LOWCOST (0x02)</CODE></LI>
     * <LI><CODE>IPTOS_RELIABILITY (0x04)</CODE></LI>
     * <LI><CODE>IPTOS_THROUGHPUT (0x08)</CODE></LI>
     * <LI><CODE>IPTOS_LOWDELAY (0x10)</CODE></LI>
     * </UL>
     * The last low order bit is always ignored as this
     * corresponds to the MBZ (must be zero) bit.
     * <p/>
     * Setting bits in the precedence field may result in a
     * SocketException indicating that the operation is not
     * permitted.
     * <p/>
     * for Internet Protocol v6 <code>tc</code> is the value that
     * would be placed into the sin6_flowinfo field of the IP header.
     *
     * @param tc an <code>int</code> value for the bitset.
     * @throws SocketException if there is an error setting the
     *                         traffic class or type-of-service
     * @see #getTrafficClass
     */
    public synchronized void setTrafficClass( int tc ) throws SocketException
    {
        if ( tc < 0 || tc > 255 )
        {
            throw new IllegalArgumentException( "tc is not in range 0 -- 255" );
        }

        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        impl.setOption( SocketOptions.IP_TOS, new Integer( tc ) );
    }

    /**
     * Gets traffic class or type-of-service in the IP raw
     * header for packets sent from this DatagramSocket.
     * <p/>
     * As the underlying network implementation may ignore the
     * traffic class or type-of-service set using {@link #setTrafficClass(int)}
     * this method may return a different value than was previously
     * set using the {@link #setTrafficClass(int)} method on this
     * DatagramSocket.
     *
     * @return the traffic class or type-of-service already set
     * @throws SocketException if there is an error obtaining the
     *                         traffic class or type-of-service value.
     * @see #setTrafficClass
     * @since 1.4
     */
    public synchronized int getTrafficClass() throws SocketException
    {
        if ( isClosed() )
        {
            throw new SocketException( "Socket is closed" );
        }
        return ( (Integer) ( impl.getOption( SocketOptions.IP_TOS ) ) ).intValue();
    }

    /**
     * Closes this raw socket.
     * <p/>
     * Any thread currently blocked in {#link receive} upon this socket
     * will throw a {@link SocketException}.
     * <p/>
     * <p> If this socket has an associated channel then the channel is closed
     * as well.
     */
    public void close()
    {
        synchronized ( closeLock )
        {
            if ( isClosed() )
            {
                return;
            }
            try
            {
                impl.close();
            }
            finally
            {
                closed = true;
            }
        }
    }

    /**
     * Returns wether the socket is closed or not.
     *
     * @return true if the socket has been closed
     */
    public boolean isClosed()
    {
        synchronized ( closeLock )
        {
            return closed;
        }
    }

    static Class implClass = null;

    static
    {
        String prefix = "";
        try
        {
            prefix = (String) AccessController.doPrivileged( new GetPropertyAction( "impl.prefix", "ACE" ) );
            implClass = Class.forName( "org.livetribe.net." + prefix + "RawSocketImpl" );
        }
        catch ( Exception e )
        {
            System.err.println( "Can't find class: org.livetribe.net." + prefix + "RawSocketImpl: check impl.prefix property" );
        }

        if ( implClass == null )
        {
            try
            {
                implClass = ACERawSocketImpl.class;
            }
            catch ( Exception e )
            {
                throw new Error( "System property impl.prefix incorrect" );
            }
        }
    }
}
