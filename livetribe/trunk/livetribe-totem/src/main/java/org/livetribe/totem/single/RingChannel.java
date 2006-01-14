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

import org.activeio.AsyncChannel;
import org.activeio.adapter.SyncToAsyncChannel;
import org.activeio.net.DatagramSocketSyncChannel;
import org.activeio.command.AsyncChannelToAsyncCommandChannel;
import org.activeio.command.WireFormat;


/**
 * @version $Revision: $ $Date: $
 */
public class RingChannel extends AsyncChannelToAsyncCommandChannel
{
    private final DatagramSocketSyncChannel channel;

    public RingChannel(DatagramSocketSyncChannel channel, WireFormat wireFormat)
    {
        super(SyncToAsyncChannel.adapt(channel), wireFormat);
        this.channel = channel;
    }
}
