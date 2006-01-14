/**
 *
 * Copyright 2005 (C) The original author or authors.
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
package org.livetribe.totem.single;

import java.net.InetAddress;

import junit.framework.TestCase;


/**
 * @version $Revision: $ $Date: $
 */
public class DefaultIdentifierTest extends TestCase
{
    public void testConstructor() throws Exception
    {
        new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 8080);

        try
        {
            new DefaultIdentifier(null, 80);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException ingore)
        {
        }

        new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 0);
        new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 0xFFFF);

        try
        {
            new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 0xFFFF + 1);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException ingore)
        {
        }

        try
        {
            new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), -1);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException ingore)
        {
        }
    }

    public void testEquals() throws Exception
    {
        DefaultIdentifier one = new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 1);
        DefaultIdentifier two = new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 1);
        DefaultIdentifier three = new DefaultIdentifier(InetAddress.getByName("205.1.1.2"), 1);
        DefaultIdentifier four = new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), 4);

        assertFalse(one.equals(new Object()));
        assertTrue(one.equals(one));
        assertTrue(one.hashCode() == one.hashCode());
        assertTrue(one.equals(two));
        assertTrue(one.hashCode() == two.hashCode());
        assertFalse(one.equals(three));
        assertFalse(one.equals(four));
    }

    public void testCompare() throws Exception
    {
        DefaultIdentifier one = new DefaultIdentifier(InetAddress.getByName("205.1.1.1"), (short) 1);
        DefaultIdentifier two = new DefaultIdentifier(InetAddress.getByName("205.1.1.2"), (short) 1);
        DefaultIdentifier three = new DefaultIdentifier(InetAddress.getByName("205.1.1.2"), (short) 2);

        assertTrue(one.compareTo(new Object()) > 0);
        assertTrue(one.compareTo(one) == 0);
        assertTrue(one.compareTo(two) < 0);
        assertTrue(two.compareTo(one) > 0);
        assertTrue(two.compareTo(three) < 0);
        assertTrue(three.compareTo(two) > 0);
    }
}
