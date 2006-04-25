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

/**
 * @version $Revision: $ $Date: $
 */
public final class StaticConnectionMonitor
{
    private static final ConnectionMonitorBroadcaster connectionMonitor = new ConnectionMonitorBroadcaster();

    private StaticConnectionMonitor()
    {
    }

    public static void addConnectionMonitor(ConnectionMonitor monitor)
    {
        if (monitor == null) throw new NullPointerException("monitor is null");
        StaticConnectionMonitor.connectionMonitor.addConnectionMonitor(monitor);
    }

    public static void removeConnectionMonitor(ConnectionMonitor monitor)
    {
        if (monitor == null) throw new NullPointerException("monitor is null");
        StaticConnectionMonitor.connectionMonitor.removeConnectionMonitor(monitor);
    }

    public static void error(Connection connection, Throwable t)
    {
        connectionMonitor.error(connection, t);
    }
}
