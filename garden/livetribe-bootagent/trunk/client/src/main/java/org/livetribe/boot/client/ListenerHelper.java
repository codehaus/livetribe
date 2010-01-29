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

import java.util.ArrayList;
import java.util.List;


/**
 * A helper class to manage listeners and event delivery.
 *
 * @version $Revision$ $Date$
 */
public class ListenerHelper implements Listener
{
    private final List<Listener> listeners = new ArrayList<Listener>();

    /**
     * Add a listener to the collection of mangaged listeners
     *
     * @param listener the listener to be added
     */
    public synchronized void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    /**
     * Remove a listener from the collection of managed listeners
     *
     * @param listener the listener to be removed
     */
    public synchronized void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void warning(String message)
    {
        for (Listener listener : listeners)
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
    public synchronized void warning(String message, Throwable throwable)
    {
        for (Listener listener : listeners)
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
    public synchronized void error(String message)
    {
        for (Listener listener : listeners)
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
    public synchronized void error(String message, Throwable throwable)
    {
        for (Listener listener : listeners)
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
