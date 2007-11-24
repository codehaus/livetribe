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
