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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import net.jcip.annotations.ThreadSafe;

import org.livetribe.boot.LifeCycle;
import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.ProvisionProvider;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * A Boot Agent Provisioning Client, (BAPC).
 * <p/>
 * The BPAC is meant to load and start the lowest layer of a server.  It is
 * expected that this bottom service layer will then, after being started by
 * the BAPC, go on to provision higher, more complex, levels of a server.
 * Long running servers must be prepared to have any of its layers updated.
 * To facilitate operator-less updates a simple boot agent, this BAPC, queries
 * for updates, loads, then initiates them.  Since the BPAC is the start of
 * this chain of updates it must be incredibly simple to mitigate any need to
 * update the BAPC itself.  Therefore it is severely restricted in
 * functionality.
 * <p/>
 * The BAPC is supplied with a provision provider, from which it it makes
 * provisioning queries, a content provider, from which it obtains provision
 * enties, a scheduled thread pool, to schedule and execute its provisioning
 * queries, and the provisioning store, to store its current provisioning
 * directive.
 *
 * @version $Revision$ $Date$
 * @see ProvisionProvider
 * @see ContentProvider
 * @see ProvisionStore
 */
@ThreadSafe
public class Client
{
    public final static long DEFAULT_PERIOD = 900;
    private final Object LOCK = new Object();
    private final Semaphore semaphore = new Semaphore(1);
    private final ClientListenerHelper listeners = new ClientListenerHelper();
    private final ProvisionProvider provisionProvider;
    private final ContentProvider contentProvider;
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private final ProvisionStore provisionStore;
    private volatile State state = State.STOPPED;
    private volatile long period = DEFAULT_PERIOD;
    private volatile Runnable handle;
    private volatile LifeCycle lifeCycleInstance;


    /**
     * Create an instance of a Boot Agent Provisioning Client, (BAPC).
     * <p/>
     * All parameters are required.  The client is not started until the
     * <code>start()</code> method is called.
     *
     * @param provisionProvider           provisioning queries are made against this provider
     * @param contentProvider             content listed by the provisioning provider are obtained from this provider
     * @param scheduledThreadPoolExecutor periodic queries are scheduled in this pool
     * @param provisionStore              the current provisioning cirective is stored in this store
     * @see #start()
     */
    public Client(ProvisionProvider provisionProvider, ContentProvider contentProvider, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, ProvisionStore provisionStore)
    {
        if (provisionProvider == null) throw new IllegalArgumentException("provisionProvider is null");
        if (contentProvider == null) throw new IllegalArgumentException("contentProvider is null");
        if (scheduledThreadPoolExecutor == null) throw new IllegalArgumentException("scheduledThreadPoolExecutor is null");
        if (provisionStore == null) throw new IllegalArgumentException("provisionStore is null");

        this.provisionProvider = provisionProvider;
        this.contentProvider = contentProvider;
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
        this.provisionStore = provisionStore;
    }

    /**
     * Obtain the current state of this client.
     *
     * @return the current state of this client
     */
    public State getState()
    {
        return state;
    }

    /**
     * Set the current state of this client and notify listeners of the state
     * change.
     *
     * @param state the next state for this client
     */
    protected void setState(State state)
    {
        listeners.stateChange(this.state, state);
        this.state = state;
    }

    /**
     * Obtain period, in seconds, used to query the provisioning provider.
     *
     * @return the period, in seconds, used to query the provisioning provider
     */
    public long getPeriod()
    {
        return period;
    }

    /**
     * Set the period, in seconds, used to query the provisioning provider.
     * <p/>
     * Changing the period to a new value will result in the removal of the old
     * provisioning checker and the scheduling of a new provisioning checker
     * with the new period.
     *
     * @param period the period, in seconds, to use to query the provisioning provider
     */
    public void setPeriod(long period)
    {
        if (this.period != period)
        {
            if (!scheduledThreadPoolExecutor.remove(handle)) listeners.warning("Was unable to remove task");

            handle = (Runnable) scheduledThreadPoolExecutor.scheduleWithFixedDelay(new ProvisionCheck(), period, period, TimeUnit.SECONDS);

            this.period = period;
        }
    }

    protected ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor()
    {
        return scheduledThreadPoolExecutor;
    }

    /**
     * Add a client listener which monitors changes in this client.
     *
     * @param listener the client listener to added
     */
    public void addListener(ClientListener listener)
    {
        listeners.addListener(listener);
    }

    /**
     * Remove a client listener.
     *
     * @param listener the client listener to be removed
     */
    public void removeListener(ClientListener listener)
    {
        listeners.removeListener(listener);
    }

    /**
     * Start the Boot Agent Provisioning Client.
     * <p/>
     * A provisioning check is immediately run which results in the provisioned
     * entries being started.  This method can potentially take a very long
     * time to complete.
     */
    public void start()
    {
        synchronized (LOCK)
        {
            if (state == State.STARTING || state == State.RUNNING) return;

            setState(State.STARTING);

            ProvisionCheck provisionCheck = new ProvisionCheck();

            provisionCheck.run();

            handle = (Runnable) scheduledThreadPoolExecutor.scheduleWithFixedDelay(provisionCheck, period, period, TimeUnit.SECONDS);

            setState(State.RUNNING);
        }
    }

    /**
     * Stop the Boot Agent Provisioning Client.
     * <p/>
     * The currently provisioned artifacts are stopped.  This method can
     * potentially take a very long time to complete.
     */
    public void stop()
    {
        synchronized (LOCK)
        {
            if (state == State.STOPPING || state == State.STOPPED) return;

            setState(State.STOPPING);

            if (!scheduledThreadPoolExecutor.remove(handle)) listeners.warning("Was unable to remove task");
            handle = null;

            try
            {
                semaphore.acquire();
            }
            catch (InterruptedException ie)
            {
                listeners.warning("Stop interrupted");
            }
            finally
            {
                semaphore.release();
            }

            shutdown();

            setState(State.STOPPED);
        }
    }

    /**
     * Internal startup method which loads a lifecycle instance and calls its
     * start method.
     */
    private void startup()
    {
        final ClassLoader saved = Thread.currentThread().getContextClassLoader();

        try
        {
            List<URL> urls = provisionStore.getClasspath();
            URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), saved);

            Thread.currentThread().setContextClassLoader(classLoader);

            @SuppressWarnings({"unchecked"}) Class<LifeCycle> bootClass = (Class<LifeCycle>) classLoader.loadClass(provisionStore.getCurrentProvisionDirective().getBootClass());

            lifeCycleInstance = bootClass.newInstance();

            lifeCycleInstance.start();
        }
        catch (ProvisionStoreException pse)
        {
            listeners.error("Provision check error", pse);
        }
        catch (ClassNotFoundException cnde)
        {
            listeners.error("Provision check error", cnde);
        }
        catch (IllegalAccessException iae)
        {
            listeners.error("Provision check error", iae);
        }
        catch (InstantiationException ie)
        {
            listeners.error("Provision check error", ie);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(saved);
        }
    }

    /**
     * Internal startup method which calls the stop method of the loaded
     * lifecycle instance.
     */
    private void shutdown()
    {
        if (lifeCycleInstance != null)
        {
            ClassLoader saved = Thread.currentThread().getContextClassLoader();
            try
            {
                Thread.currentThread().setContextClassLoader(lifeCycleInstance.getClass().getClassLoader());
                lifeCycleInstance.stop();
            }
            finally
            {
                Thread.currentThread().setContextClassLoader(saved);
                lifeCycleInstance = null;
            }
        }
    }

    private class ProvisionCheck implements Runnable
    {
        public void run()
        {
            try
            {
                semaphore.acquire();

                long currentVersion = provisionStore.getCurrentProvisionDirective().getVersion();
                String uuid = provisionStore.getUuid();

                listeners.provisionCheck(uuid, currentVersion);

                ProvisionDirective response = provisionProvider.hello(provisionStore.getUuid(), currentVersion);

                listeners.provisionDirective(response);

                if (response instanceof DoNothing) return;

                YouShould should = (YouShould) response;

                if (should.getVersion() == currentVersion) return;

                Set<ProvisionEntry> currentEntries = provisionStore.getCurrentProvisionDirective().getEntries();
                for (ProvisionEntry entry : should.getEntries())
                {
                    if (!currentEntries.contains(entry))
                    {
                        provisionStore.store(entry, contentProvider.pleaseProvide(entry.getName(), entry.getVersion()));
                    }
                }

                provisionStore.setNextProvisionDirective(new ProvisionConfiguration(should.getVersion(), should.getBootClass(), should.getEntries()));

                provisionStore.prepareNext();

                if (response instanceof YouMust)
                {
                    YouMust must = (YouMust) response;

                    if (must.isRestart())
                    {
                        if (state == State.RUNNING) shutdown();
                        startup();
                    }
                }
                else if (state == State.STARTING)
                {
                    startup();
                }
            }
            catch (BootException bse)
            {
                listeners.error("Provision check error", bse);
            }
            catch (ProvisionStoreException pse)
            {
                listeners.error("Provision check error", pse);
            }
            catch (InterruptedException ie)
            {
                listeners.error("Provision check error", ie);
            }
            finally
            {
                semaphore.release();
            }
        }
    }
}
