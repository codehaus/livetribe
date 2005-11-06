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
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;


/**
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
final class APRRawSocketImpl extends RawSocketImpl
{
    /* timeout value for receive() */
    private int timeout = 0;

    protected void create( int protocol ) throws SocketException
    {
        this.fileDescriptor = new FileDescriptor();
        this.protocol = protocol;
        rawSocketCreate( protocol );
    }

    protected native void bind( byte[] laddr ) throws SocketException;

    protected native void send( RawPacket packet ) throws IOException;

    protected native byte[] peek() throws IOException;

    protected native void receive( RawPacket p ) throws IOException;

    protected void close()
    {
        if ( fileDescriptor != null )
        {
            try
            {
                rawSocketClose();
            }
            finally
            {
                fileDescriptor = null;
            }
        }
    }

    public Object getOption( int optID ) throws SocketException
    {
        if (optID == SO_TIMEOUT) {
            return new Integer(timeout);
        }

        byte[] ret = socketGetOption(optID);

        if (optID == SO_RCVBUF || optID == SO_SNDBUF) {
            int result;

            result  = ret[3] & 0xFF;
            result |= ((ret[2] << 8) & 0xFF00);
            result |= ((ret[1] << 16) & 0xFF0000);
            result |= ((ret[0] << 24) & 0xFF000000);
            return (new Integer(result));
        }

        if (optID == SO_BINDADDR) {
            try
            {
                return InetAddress.getByAddress( ret );
            }
            catch ( UnknownHostException e )
            {
                throw new SocketException(e.toString());
            }
        } else {
            return null;
        }
    }

    public void setOption( int optID, Object value ) throws SocketException
    {
        switch ( optID )
        {
            /* check type safety b4 going native.  These should never
             * fail, since only java.Socket* has access to
             * PlainSocketImpl.setOption().
             */
            case SO_TIMEOUT:
                if ( value == null || !( value instanceof Integer ) )
                {
                    throw new SocketException( "bad argument for SO_TIMEOUT" );
                }
                int tmp = ( (Integer) value ).intValue();
                if ( tmp < 0 )
                {
                    throw new IllegalArgumentException( "timeout < 0" );
                }
                timeout = tmp;
                return;
            case SO_BINDADDR:
                throw new SocketException( "Cannot re-bind Socket" );
            case SO_RCVBUF:
            case SO_SNDBUF:
                if ( value == null || !( value instanceof Integer ) || ( (Integer) value ).intValue() < 0 )
                {
                    throw new SocketException( "bad argument for SO_SNDBUF or SO_RCVBUF" );
                }
                break;
            default:
                throw new SocketException( "invalid option: " + optID );
        }
        socketSetOption( optID, value );
    }

    static
    {
        AccessController.doPrivileged( new PrivilegedAction()
        {
            public Object run()
            {
                System.loadLibrary( "livetribe_net_APR" );
                return null;
            }
        } );
        init();
    }

    private native void rawSocketCreate( int protocol ) throws SocketException;

    private native void rawSocketClose();

    private native void socketSetOption( int optID, Object value ) throws SocketException;

    private native byte[] socketGetOption( int optID ) throws SocketException;

    private native static void init();

    protected synchronized void finalize()
    {
        close();
    }

}
