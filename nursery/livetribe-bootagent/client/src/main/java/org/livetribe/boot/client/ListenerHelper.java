/**
 *
 * Copyright 2007-2009 (C) The original author or authors
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

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;


/**
 * @version $Revision$ $Date$
 */
@ThreadSafe
public class ListenerHelper implements Listener
{
    private final List<Listener> listeners = new ArrayList<Listener>();

    protected List<Listener> getListeners()
    {
        return listeners;
    }

    @GuardedBy("this")
    public synchronized void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    @GuardedBy("this")
    public synchronized void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    @GuardedBy("this")
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

    @SuppressWarnings({"EmptyCatchBlock"})
    @GuardedBy("this")
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

    @SuppressWarnings({"EmptyCatchBlock"})
    @GuardedBy("this")
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
