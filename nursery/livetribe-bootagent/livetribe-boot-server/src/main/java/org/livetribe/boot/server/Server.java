/**
 *
 * Copyright 2007 (C) The original author or authors
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
package org.livetribe.boot.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A boot agent server object listens to requests from boot agent clients
 * and responds to their provisioning requests.  It processes requests from
 * boot agent clients by delegating descisions to and obtaining streams from
 * requested artifacts from the <code>ProvisionManager</code>.   It allows one
 * to specify what port it listens to as well as, optionally, which NIC.
 * <p/>
 * This boot agent server is purposfully simple.  It is expected that higher
 * level servers that are booted on top of this service will fulfill more
 * sophisticated paradigms of provisioning.
 * <p/>
 * Concurrency is controlled via an internal lock object and not
 *
 * @version $Revision$ $Date$
 */
@ThreadSafe
public class Server
{
    private final String className = Server.class.getName();
    private final Logger logger = Logger.getLogger(className);
    private final List<ServerListener> listeners = new ArrayList<ServerListener>();
    protected final Object LOCK = new Object();
    private final InetAddress nic;
    private final int port;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ProvisionManager provisionManager;
    private volatile boolean started;

    /**
     * @param port
     * @param scheduledExecutorService
     * @param provisionManager
     */
    public Server(int port, ScheduledExecutorService scheduledExecutorService, ProvisionManager provisionManager)
    {
        this(null, port, scheduledExecutorService, provisionManager);
    }

    /**
     * @param nic
     * @param port
     * @param scheduledExecutorService
     * @param provisionManager
     */
    public Server(InetAddress nic, int port, ScheduledExecutorService scheduledExecutorService, ProvisionManager provisionManager)
    {
        if (scheduledExecutorService == null) throw new IllegalArgumentException("Sceduled executor service must not be null");
        if (provisionManager == null) throw new IllegalArgumentException("Provisioning DAO must not be null");

        if (logger.isLoggable(Level.CONFIG))
        {
            logger.config("NIC: " + (nic == null ? "DEFAULT" : nic));
            logger.config("port: " + port);
            logger.config("ScheduledExecutorService: " + scheduledExecutorService);
            logger.config("ProvisionManager: " + provisionManager);
        }

        this.nic = nic;
        this.port = port;
        this.scheduledExecutorService = scheduledExecutorService;
        this.provisionManager = provisionManager;
    }

    /**
     * Start the boot agent server.  This method can be called multiple times
     * with no ill effects.
     */
    @GuardedBy("LOCK")
    @SuppressWarnings({ "EmptyCatchBlock" })
    public void start()
    {
        logger.entering(className, "start");
        synchronized (LOCK)
        {
            if (started) return;

            synchronized (listeners) { for (ServerListener listener : listeners) try { listener.started(); } catch (Throwable ignore) {} }
            started = true;
        }
        logger.exiting(className, "start");
    }

    /**
     * Stop the boot agent server.  This method can be called multiple times
     * with no ill effects.
     */
    @GuardedBy("LOCK")
    @SuppressWarnings({ "EmptyCatchBlock" })
    public void stop()
    {
        logger.entering(className, "stop");
        synchronized (LOCK)
        {
            if (!started) return;

            synchronized (listeners) { for (ServerListener listener : listeners) try { listener.stopped(); } catch (Throwable ignore) {} }
            started = false;
        }
        logger.exiting(className, "stop");
    }

    /**
     * Add a server listener
     *
     * @param listener the listener to add
     */
    public void addListener(ServerListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    /**
     * Remove a server listener
     *
     * @param listener the listener to remove.  This method uses
     *                 <code>equal()</code> to identify the listener to remove.
     */
    public void removeListener(ServerListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }
}
