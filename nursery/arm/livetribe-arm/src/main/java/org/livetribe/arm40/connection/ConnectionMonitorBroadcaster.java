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
package org.livetribe.arm40.connection;

import java.util.Iterator;

import edu.emory.mathcs.backport.java.util.concurrent.CopyOnWriteArrayList;


/**
 * @version $Revision: $ $Date: $
 */
public class ConnectionMonitorBroadcaster implements ConnectionMonitor
{
    private final CopyOnWriteArrayList connectionMonitors = new CopyOnWriteArrayList();

    public void addConnectionMonitor(ConnectionMonitor monitor)
    {
        connectionMonitors.addIfAbsent(monitor);
    }

    public void removeConnectionMonitor(ConnectionMonitor monitor)
    {
        connectionMonitors.remove(monitor);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Connection connection, Throwable t)
    {
        for (Iterator iterator = connectionMonitors.iterator(); iterator.hasNext();)
        {
            ConnectionMonitor monitor = (ConnectionMonitor) iterator.next();
            try
            {
                monitor.error(connection, t);
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }
}
