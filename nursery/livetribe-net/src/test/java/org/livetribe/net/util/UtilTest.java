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

import junit.framework.TestCase;


/**
 * @version $Revision: 1.2 $ $Date: 2005/05/18 23:18:36 $
 */
public class UtilTest extends TestCase
{
    public void testChecksum() throws Exception
    {
        try
        {
            byte[] data = new byte[1];

            try
            {
                Util.checksum( null, 0, 1 );
                fail( "Should have thrown IllegalArgumentException" );
            }
            catch ( IllegalArgumentException iae )
            {
            }

            try
            {
                Util.checksum( data, -1, 1 );
                fail( "Should have thrown IllegalArgumentException" );
            }
            catch ( IllegalArgumentException iae )
            {
            }

            try
            {
                Util.checksum( data, 1, 1 );
                fail( "Should have thrown IllegalArgumentException" );
            }
            catch ( IllegalArgumentException iae )
            {
            }

            try
            {
                Util.checksum( data, 0, 2 );
                fail( "Should have thrown IllegalArgumentException" );
            }
            catch ( IllegalArgumentException iae )
            {
            }

            try
            {
                Util.checksum( data, 0, -1 );
                fail( "Should have thrown IllegalArgumentException" );
            }
            catch ( IllegalArgumentException iae )
            {
            }

            int result = ( Util.checksum( data ) & 0xFFFF );
            assertEquals( 0xFFFF, result );
            assertEquals( Util.checksum( data ), Util.uncheckedChecksum( data ) );

            data[0] = 1;
            result = ( Util.checksum( data ) & 0xFFFF );
            assertEquals( 0xFFFE, result );
            assertEquals( Util.checksum( data ), Util.uncheckedChecksum( data ) );

            data = new byte[]{(byte) 0xFE, (byte) 0x05};
            result = ( Util.checksum( data ) & 0xFFFF );
            assertEquals( 0x01FA, result );
            assertEquals( Util.checksum( data ), Util.uncheckedChecksum( data ) );

            data = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0xF2, (byte) 0x03, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6, (byte) 0xF7, (byte) 0x00, (byte) 0x00};
            result = ( Util.checksum( data ) & 0xFFFF );
            assertEquals( 0x210E, result );
            assertEquals( Util.checksum( data ), Util.uncheckedChecksum( data ) );

            data = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0xF2, (byte) 0x03, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6, (byte) 0xF7, (byte) 0x21, (byte) 0x0E};
            result = ( Util.checksum( data ) & 0xFFFF );
            assertEquals( 0, result );
            assertEquals( Util.checksum( data ), Util.uncheckedChecksum( data ) );
        }
        catch ( UnsatisfiedLinkError ule )
        {
        }
    }
}
