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

import net.sf.beep4j.StartChannelRequest;
import net.sf.beep4j.StartSessionRequest;
import net.sf.beep4j.ext.SessionHandlerAdapter;

import org.livetribe.boot.protocol.BootServer;


/**
 * @version $Revision$ $Date$
 */
public class ServerSessionHandler extends SessionHandlerAdapter
{
    public final static String CMD_PROFILE = "urn:livetribe.org:names:boot:1:0";
    private final BootServer bootServer;

    public ServerSessionHandler(BootServer bootServer)
    {
        this.bootServer = bootServer;
    }

    @Override
    public void connectionEstablished(StartSessionRequest startSessionRequest)
    {
        startSessionRequest.registerProfile(CMD_PROFILE);
    }

    @Override
    public void channelStartRequested(StartChannelRequest startChannelRequest)
    {
        if (startChannelRequest.hasProfile(CMD_PROFILE))
        {
            startChannelRequest.selectProfile(startChannelRequest.getProfile(CMD_PROFILE), new ServeChannelHandler(bootServer));
        }
    }
}
