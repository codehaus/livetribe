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
public class RingIdTest extends TestCase
{
    public void testConstructor() throws Exception
    {
        new RingId(1, new DefaultIdentifier(InetAddress.getByName("127.0.0.1"), 80));

        try
        {
            new RingId(-1, new DefaultIdentifier(InetAddress.getByName("127.0.0.1"), 80));
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException ingore)
        {
        }
        try
        {
            new RingId( 7, null);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException ingore)
        {
        }
    }
}
