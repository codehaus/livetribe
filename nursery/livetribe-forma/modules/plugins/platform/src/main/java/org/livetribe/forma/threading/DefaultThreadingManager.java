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
package org.livetribe.forma.threading;

import java.awt.EventQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @version $Rev: 118 $ $Date$
 */
public class DefaultThreadingManager implements ThreadingManager
{
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

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

    public void postToEventQueue(Runnable runnable)
    {
        EventQueue.invokeLater(runnable);
    }
}
