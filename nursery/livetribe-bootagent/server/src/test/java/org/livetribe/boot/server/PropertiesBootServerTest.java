/**
 *
 * Copyright 2007 (C) The original author or authors
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
package org.livetribe.boot.server;

import java.io.File;
import java.io.InputStream;

import junit.framework.TestCase;

import org.livetribe.boot.protocol.BootServer;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision$ $Date$
 */
public class PropertiesBootServerTest extends TestCase
{
    public void test() throws Exception
    {
        File bootDirectory = new File("target/test-classes/boot");

        assertTrue(bootDirectory.isDirectory());

        BootServer bootServer = new PropertiesBootServer(bootDirectory);

        YouShould directive = bootServer.hello("", 6);

        assertNotNull(directive);
        assertEquals(10, directive.getVersion());
        assertEquals("com.acme.mock.c.MockLifecycle", directive.getBootClass());
        assertFalse(directive instanceof YouMust);
        assertEquals(3, directive.getEntries().size());

        for (ProvisionEntry entry : directive.getEntries())
        {
            long count = 0;
            int len = 0;
            byte[] buffer = new byte[1024];
            InputStream inputStream = bootServer.pleaseProvide(entry.getName(), entry.getVersion());
            assertNotNull(inputStream);
            while ((len = inputStream.read(buffer)) != -1) count += len;
            assertTrue(count > 0);
        }

        directive = bootServer.hello("special", 2);

        assertNotNull(directive);
        assertEquals(5, directive.getVersion());
        assertEquals("com.acme.mock.c.MockLifecycle", directive.getBootClass());
        assertTrue(directive instanceof YouMust);
        assertEquals(3, directive.getEntries().size());

        for (ProvisionEntry entry : directive.getEntries())
        {
            long count = 0;
            int len = 0;
            byte[] buffer = new byte[1024];
            InputStream inputStream = bootServer.pleaseProvide(entry.getName(), entry.getVersion());
            assertNotNull(inputStream);
            while ((len = inputStream.read(buffer)) != -1) count += len;
            assertTrue(count > 0);
        }

    }
}
