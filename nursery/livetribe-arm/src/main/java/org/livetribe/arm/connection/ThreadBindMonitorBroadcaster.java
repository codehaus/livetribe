/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm.connection;

import java.util.Iterator;

import edu.emory.mathcs.backport.java.util.concurrent.CopyOnWriteArrayList;


/**
 * @version $Revision: $ $Date: $
 */
public class ThreadBindMonitorBroadcaster implements ThreadBindMonitor
{
    private final CopyOnWriteArrayList threadiMonitors = new CopyOnWriteArrayList();

    public void addThreadMonitor(ThreadBindMonitor monitor)
    {
        threadiMonitors.addIfAbsent(monitor);
    }

    public void removeThredaMonitor(ThreadBindMonitor monitor)
    {
        threadiMonitors.remove(monitor);
    }

    /**
     * {@inheritDoc}
     */
    public void bind()
    {
        for (Iterator iterator = threadiMonitors.iterator(); iterator.hasNext();)
        {
            ThreadBindMonitor monitor = (ThreadBindMonitor) iterator.next();
            try
            {
                monitor.bind();
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void unbind()
    {
        for (Iterator iterator = threadiMonitors.iterator(); iterator.hasNext();)
        {
            ThreadBindMonitor monitor = (ThreadBindMonitor) iterator.next();
            try
            {
                monitor.unbind();
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }
}
