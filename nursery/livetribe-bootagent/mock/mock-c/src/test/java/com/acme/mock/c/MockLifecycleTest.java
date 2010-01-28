/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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
package com.acme.mock.c;

import com.acme.mock.Service;
import junit.framework.TestCase;


/**
 * @version $Revision$ $Date$
 */
public class MockLifecycleTest extends TestCase
{
    public void test() throws Exception
    {
        MockLifecycle mock = new MockLifecycle();

        mock.start();

        Service service = mock.getService();

        assertNotNull(service);

        assertEquals("test mocked by B", service.test("test"));

        mock.stop();

        assertNull(mock.getService());
    }
}
