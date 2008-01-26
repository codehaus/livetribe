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
package org.livetribe.boot.beep;

import java.io.File;
import java.io.InputStream;
import java.net.SocketAddress;

import junit.framework.TestCase;
import net.sf.beep4j.transport.mina.MinaListener;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.vmpipe.VmPipeAcceptor;
import org.apache.mina.transport.vmpipe.VmPipeAddress;

import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;
import org.livetribe.boot.server.PropertiesBootServer;


/**
 * @version $Revision$ $Date$
 */
public class ServerSessionTest extends TestCase
{
    public void test() throws Exception
    {
        File bootDirectory = new File("target/test-classes/boot");

        assertTrue(bootDirectory.isDirectory());

        PropertiesBootServer bootServer = new PropertiesBootServer(bootDirectory);

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

        ServerSessionHandlerFactory handlerFactory = new ServerSessionHandlerFactory(bootServer);

        SocketAddress address = new VmPipeAddress(12345);
        IoAcceptor acceptor = new VmPipeAcceptor();
        MinaListener listener = new MinaListener(acceptor);

        listener.bind(address, handlerFactory);

        Thread.sleep(1000);

        listener.unbind(address);
    }
}
