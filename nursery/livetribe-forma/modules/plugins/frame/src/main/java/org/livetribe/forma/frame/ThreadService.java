/*
 * Copyright 2006 the original author or authors
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
package org.livetribe.forma.frame;

import java.awt.EventQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @version $Rev$ $Date$
 */
public class ThreadService implements ApplicationListener
{
    private Logger logger = Logger.getLogger(getClass().getName());
    private List<Runnable> eventQueue = new ArrayList<Runnable>();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean started;

    public void onApplicationEvent(ApplicationEvent event)
    {
        if (event instanceof ContextRefreshedEvent)
        {
            if (logger.isLoggable(Level.FINE)) logger.fine("Services loading completed, starting ThreadService");
            start();
        }
    }

    private void start()
    {
        if (started) return;
        started = true;
        for (Runnable task : eventQueue) postToEventQueue(task);
        eventQueue.clear();
    }

    public void invokeWithFixedDelay(final Runnable task, long delay, long period, TimeUnit unit)
    {
        // TODO: improve this: consider using javax.swing.Timer
        Runnable wrappedTask = new Runnable()
        {
            public void run()
            {
                postToEventQueue(task);
            }
        };
        scheduler.scheduleWithFixedDelay(wrappedTask, delay, period, unit);
    }

    /**
     * Adds the given runnable as last event of the AWT event queue, returning immediately.
     * <br />
     * Depending on the fact that this service is started or not, the event is respectively
     * added immediately to the AWT event queue via {@link EventQueue#invokeLater(Runnable)},
     * or it is kept in a local queue.
     * <br />
     * When this service is started, events waiting in the local queue are moved to the AWT
     * event queue.
     * @param runnable The runnable to execute as AWT event.
     */
    public void postToEventQueue(Runnable runnable)
    {
        if (started)
        {
            EventQueue.invokeLater(runnable);
        }
        else
        {
            eventQueue.add(runnable);
        }
    }
}
