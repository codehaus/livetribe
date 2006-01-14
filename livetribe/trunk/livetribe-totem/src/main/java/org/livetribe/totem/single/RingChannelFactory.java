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

import java.io.IOException;
import java.net.URI;

import org.activeio.AsyncChannel;
import org.activeio.AsyncChannelFactory;
import org.activeio.AsyncChannelServer;
import org.activeio.adapter.SyncToAsyncChannel;
import org.activeio.adapter.SyncToAsyncChannelServer;
import org.activeio.net.DatagramSocketSyncChannelFactory;


/**
 * @version $Revision: $ $Date: $
 */
public class RingChannelFactory implements AsyncChannelFactory
{
    private final DatagramSocketSyncChannelFactory factory = new DatagramSocketSyncChannelFactory();

    public AsyncChannel openAsyncChannel(URI location) throws IOException
    {
        return SyncToAsyncChannel.adapt(factory.openSyncChannel(location));
    }

    public AsyncChannelServer bindAsyncChannel(URI location) throws IOException
    {
        return SyncToAsyncChannelServer.adapt(factory.bindSyncChannel(location));
    }
}
