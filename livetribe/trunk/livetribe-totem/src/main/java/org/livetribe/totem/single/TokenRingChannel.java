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
import java.net.InetAddress;

import edu.emory.mathcs.backport.java.util.concurrent.Executor;
import org.activeio.AsyncChannel;
import org.activeio.AsyncChannelListener;
import org.activeio.Packet;
import org.activeio.SyncChannel;
import org.activeio.adapter.SyncToAsyncChannel;


/**
 * @version $Revision: $ $Date: $
 */
public class TokenRingChannel implements AsyncChannel
{
    private final AsyncChannel channel;
    private InetAddress address;
    private int port;

    public TokenRingChannel(SyncChannel channel)
    {
        this(SyncToAsyncChannel.adapt(channel));
    }

    public TokenRingChannel(SyncChannel channel, Executor executor)
    {
        this(SyncToAsyncChannel.adapt(channel, executor));
    }

    public TokenRingChannel(AsyncChannel channel)
    {
        this.channel = channel;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public void setAddress(InetAddress address)
    {
        this.address = address;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setAsyncChannelListener(AsyncChannelListener channelListener)
    {
        channel.setAsyncChannelListener(channelListener);
    }

    public AsyncChannelListener getAsyncChannelListener()
    {
        return channel.getAsyncChannelListener();
    }

    public void dispose()
    {
        channel.dispose();
    }

    public void start() throws IOException
    {
        channel.start();
    }

    public void stop(long timeout) throws IOException
    {
        channel.stop(timeout);
    }

    public Object getAdapter(Class target)
    {
        if (target.isAssignableFrom(getClass())) return this;
        return channel.getAdapter(target);
    }

    public void write(Packet packet) throws IOException
    {
        channel.write(new UDPFilterPacket(packet, address, new Integer(port)));
    }

    public void flush() throws IOException
    {
        channel.flush();
    }
}
