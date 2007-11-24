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

import java.net.SocketAddress;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import junit.framework.TestCase;

import org.livetribe.boot.client.Client;
import org.livetribe.boot.mock.client.MockProvisionStore;

import org.apache.mina.common.IoConnector;
import org.apache.mina.transport.vmpipe.VmPipeConnector;
import org.apache.mina.transport.vmpipe.VmPipeAddress;
import net.sf.beep4j.Initiator;
import net.sf.beep4j.transport.mina.MinaInitiator;


/**
 * @version $Revision$ $Date$
 */
public class ClientSessionTest extends TestCase
{
    public void test() throws Exception
    {
        ScheduledThreadPoolExecutor clientPool = new ScheduledThreadPoolExecutor(5);
        SocketAddress address = new VmPipeAddress(12345);
        IoConnector connector = new VmPipeConnector();
        Initiator initiator = new MinaInitiator(connector);
        ClientSessionHandler bootServer = new ClientSessionHandler();
        initiator.connect(address, bootServer);

        Client client = new Client(bootServer, clientPool, new MockProvisionStore());

//        client.start();
//
//        client.stop();
    }
}
