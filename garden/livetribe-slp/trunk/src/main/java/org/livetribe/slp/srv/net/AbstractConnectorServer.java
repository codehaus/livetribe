/*
 * Copyright 2007-2008 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.slp.srv.net;

import org.livetribe.slp.srv.AbstractServer;
import org.livetribe.util.Listeners;

/**
 * @version $Revision$ $Date$
 */
public abstract class AbstractConnectorServer extends AbstractServer implements ConnectorServer
{
    private final Listeners<MessageListener> listeners = new Listeners<MessageListener>();

    public void addMessageListener(MessageListener messageListener)
    {
        listeners.add(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener)
    {
        listeners.remove(messageListener);
    }

    protected void notifyMessageListeners(MessageEvent event)
    {
        for (MessageListener listener : listeners) listener.handle(event);
    }

    protected void clearMessageListeners()
    {
        listeners.clear();
    }
}
