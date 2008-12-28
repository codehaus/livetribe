/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.util;

import java.util.Arrays;

import junit.framework.TestCase;


/**
 * @version $Revision$ $Date$
 */
public class HexSupportTest extends TestCase
{
    public void testEmpty()
    {
        assertTrue(Arrays.equals(new byte[]{}, HexSupport.toBytesFromHex("")));
    }

    public void testOdd()
    {
        assertTrue(Arrays.equals(new byte[]{0x01, (byte) 0xba}, HexSupport.toBytesFromHex("1ba")));
        assertTrue(Arrays.equals(new byte[]{0x0a}, HexSupport.toBytesFromHex("a")));
    }

    public void testEven()
    {
        assertTrue(Arrays.equals(new byte[]{(byte) 0xb1, (byte) 0xba}, HexSupport.toBytesFromHex("b1ba")));
        assertTrue(Arrays.equals(new byte[]{0x1a}, HexSupport.toBytesFromHex("1a")));
    }

    public void testFromBytes()
    {
        assertEquals("b1ba", HexSupport.toHexFromBytes(new byte[]{(byte) 0xb1, (byte) 0xba}));
        assertEquals("01ba", HexSupport.toHexFromBytes(new byte[]{(byte) 0x1, (byte) 0xba}));
    }
}
