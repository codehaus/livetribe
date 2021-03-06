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
package org.livetribe.boot.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.livetribe.boot.protocol.ProvisionDirective;


/**
 * A helper class to manage listeners and event delivery.
 *
 * @version $Revision$ $Date$
 */
public class ClientListenerHelper implements ClientListener
{
    private final List<ClientListener> listeners = new CopyOnWriteArrayList<ClientListener>();

    /**
     * Add a listener to the collection of mangaged listeners
     *
     * @param listener the listener to be added
     */
    public void addListener(ClientListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Remove a listener from the collection of managed listeners
     *
     * @param listener the listener to be removed
     */
    public void removeListener(ClientListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void stateChange(State oldState, State newState)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.stateChange(oldState, newState);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provisionCheck(String uuid, long version)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.provisionCheck(uuid, version);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provisionDirective(ProvisionDirective directive)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.provisionDirective(directive);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void warning(String message)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.warning(message);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void warning(String message, Throwable throwable)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.warning(message, throwable);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void error(String message)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.error(message);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void error(String message, Throwable throwable)
    {
        for (ClientListener listener : listeners)
        {
            try
            {
                listener.error(message, throwable);
            }
            catch (Throwable ignore)
            {
            }
        }
    }
}
