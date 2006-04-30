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
package org.livetribe.forma.platform.threading;

import java.util.concurrent.TimeUnit;

/**
 * @version $Rev$ $Date$
 */
public interface IThreadingService
{
    public void invokeWithFixedDelay(final Runnable task, long delay, long period, TimeUnit unit);

    /**
     * Adds the given runnable as last event of the AWT event queue, returning immediately.
     * <br />
     * Depending on the fact that this service is started or not, the event is respectively
     * added immediately to the AWT event queue via {@link java.awt.EventQueue#invokeLater(Runnable)},
     * or it is kept in a local queue.
     * <br />
     * When this service is started, events waiting in the local queue are moved to the AWT
     * event queue.
     * @param runnable The runnable to execute as AWT event.
     */
    public void postToEventQueue(Runnable runnable);
}
