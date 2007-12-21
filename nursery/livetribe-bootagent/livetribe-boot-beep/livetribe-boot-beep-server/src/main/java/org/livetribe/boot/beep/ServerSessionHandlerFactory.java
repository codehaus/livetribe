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

import net.sf.beep4j.SessionHandler;
import net.sf.beep4j.SessionHandlerFactory;

import org.livetribe.boot.protocol.BootServer;


/**
 * @version $Revision$ $Date$
 */
public class ServerSessionHandlerFactory implements SessionHandlerFactory
{
    private final BootServer bootServer;

    public ServerSessionHandlerFactory(BootServer bootServer)
    {
        this.bootServer = bootServer;
    }

    public SessionHandler createSessionHandler()
    {
        return new ServerSessionHandler(bootServer);
    }
}
