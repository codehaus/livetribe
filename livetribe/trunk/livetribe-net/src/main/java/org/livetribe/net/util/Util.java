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
package org.livetribe.net.util;


import java.security.AccessController;
import java.security.PrivilegedAction;


/**
 * @version $Revision: 1.2 $ $Date: 2005/10/17 00:54:19 $
 */
public final class Util
{
    public static void generateChecksum( byte[] data, int start, int length, int destination )
    {
        if ( data == null ) throw new IllegalArgumentException( "data cannot be null" );
        if ( start < 0 || start >= data.length ) throw new IllegalArgumentException( "Invalid value for start" );
        if ( length < 0 || ( start + length ) > data.length ) throw new IllegalArgumentException( "Invalid value for length" );
        if ( start > destination || ( start + length ) <= destination + 1 ) throw new IllegalArgumentException( "Invalid destination" );

        uncheckedGenerateChecksum( data, start, length, destination );
    }

    public static void uncheckedGenerateChecksum( byte[] data, int start, int length, int destination )
    {
        short checksum = checksum( data, start, length );

        data[destination] = (byte) ( checksum >> 8 );
        data[destination + 1] = (byte) ( checksum & 0xFF );
    }

    /**
     * @param data
     * @return checksum of byte data
     */
    public static short checksum( byte[] data )
    {
        return checksum( data, 0, data.length );
    }

    public static short checksum( byte[] data, int start, int length )
    {
        if ( data == null ) throw new IllegalArgumentException( "data cannot be null" );
        if ( start < 0 || start >= data.length ) throw new IllegalArgumentException( "Invalid value for start" );
        if ( length < 0 || ( start + length ) > data.length ) throw new IllegalArgumentException( "Invalid value for length" );

        return uncheckedChecksum( data, start, length );
    }

    public static short uncheckedChecksum( byte[] data )
    {
        return uncheckedChecksum( data, 0, data.length );
    }

    public native static short uncheckedChecksum( byte[] data, int start, int length );

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
