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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.livetribe.forma.ui.feedback.FeedbackManager;

/**
 * @see FeedbackManager
 * @version $Rev: 118 $ $Date$
 */
public interface ThreadingManager
{
    public void invokeWithFixedDelay(final Runnable task, long delay, long period, TimeUnit unit);

    /**
     * Adds the given runnable as last event of the AWT event queue, returning immediately.
     * @param runnable The runnable to execute as AWT event.
     */
    public void postToEventQueue(Runnable runnable);

    /**
     * Executes the given callable in a separate thread, returning only when the callable
     * is completed, but dequeueing AWT events in the meanwhile.
     * @param callable The object containing the code to run in a separate thread
     */
    public <T> T syncExecute(Callable<T> callable);

    public void flushEvents();
}
