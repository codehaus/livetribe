/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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
 * entries, a scheduled thread pool, to schedule and execute its provisioning
 * queries, and the provisioning store, to store its current provisioning
 * directive.
 * <p/>
 * When the BAPC receives a provisioning directive and it is directed to
 * restart, or if it is starting up for the first time, it will load the boot
 * class of the provisioning directive and call the boot class' lifecycle start
 * method.  The loading of the class is done using the provisioning classloader
 * which is constructed using the provisioning entries of the provision
 * directive and the parent classloader that was passed in the constructor.
 * <p/>
 * The BAPC itself does not perform any logging.  Instead it uses
 * <code>ClientListener</code> for state change notifications as well as
 * warning and error messages.
 *
 * @version $Revision$ $Date$
 * @see ProvisionProvider
 * @see ContentProvider
 * @see ProvisionStore
 * @see ClientListener
 */
public class Client
{
    public final static long DEFAULT_PERIOD = 900;

    // Used to prevent start and stop from concurrently executing
    private final Object lock = new Object();

    // Used to prevent provisioning requests from concurrently executing
    private final Semaphore semaphore = new Semaphore(1);

    private final ClientListenerHelper listeners = new ClientListenerHelper();
    private final ProvisionProvider provisionProvider;
    private final ContentProvider contentProvider;
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private final ProvisionStore provisionStore;
    private final ClassLoader parentClassLoader;
    private volatile State state = State.STOPPED;
    private volatile long period = DEFAULT_PERIOD;
    private volatile Runnable handle;
    private volatile LifeCycle lifeCycleInstance;


    /**
     * Create an instance of a Boot Agent Provisioning Client.
     * <p/>
     * All parameters are required.  The client is not started until the
     * <code>start()</code> method is called.  The classloader used as parent
     * classloader when constructing the provisioning classloader is the
     * thread's context classloader
     *
     * @param provisionProvider           provisioning queries are made against this provider
     * @param contentProvider             content listed by the provisioning provider are obtained from this provider
     * @param scheduledThreadPoolExecutor periodic queries are scheduled in this pool
     * @param provisionStore              the current provisioning directive is stored in this store
     * @see #start()
     */
    public Client(ProvisionProvider provisionProvider, ContentProvider contentProvider, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, ProvisionStore provisionStore)
    {
        this(provisionProvider, contentProvider, scheduledThreadPoolExecutor, provisionStore, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Create an instance of a Boot Agent Provisioning Client.
     * <p/>
     * All parameters are required.  The client is not started until the
     * <code>start()</code> method is called.
     *
     * @param provisionProvider           provisioning queries are made against this provider
     * @param contentProvider             content listed by the provisioning provider are obtained from this provider
     * @param scheduledThreadPoolExecutor periodic queries are scheduled in this pool
     * @param provisionStore              the current provisioning directive is stored in this store
     * @param parentClassLoader           the classloader to use as the parent classloader when constructing the provisioning classloader
     * @see #start()
     */
    public Client(ProvisionProvider provisionProvider, ContentProvider contentProvider, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, ProvisionStore provisionStore, ClassLoader parentClassLoader)
    {
        if (provisionProvider == null) throw new IllegalArgumentException("provisionProvider is null");
        if (contentProvider == null) throw new IllegalArgumentException("contentProvider is null");
        if (scheduledThreadPoolExecutor == null) throw new IllegalArgumentException("scheduledThreadPoolExecutor is null");
        if (provisionStore == null) throw new IllegalArgumentException("provisionStore is null");
        if (parentClassLoader == null) throw new IllegalArgumentException("classLoader is null");

        this.provisionProvider = provisionProvider;
        this.contentProvider = contentProvider;
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
        this.provisionStore = provisionStore;
        this.parentClassLoader = parentClassLoader;
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
        synchronized (lock)
        {
            if (this.period != period)
            {
                if (handle != null)
                {
                    if (!scheduledThreadPoolExecutor.remove(handle)) listeners.warning("Was unable to remove task");

                    handle = (Runnable)scheduledThreadPoolExecutor.scheduleWithFixedDelay(new ProvisionCheck(), period, period, TimeUnit.SECONDS);
                }
                this.period = period;
            }
        }
    }

    /**
     * Protected access to the thread pool for client extensions
     *
     * @return the scheduled thread pool
     */
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
     */
    public void start()
    {
        synchronized (lock)
        {
            if (state == State.STARTING || state == State.RUNNING) return;

            setState(State.STARTING);

            try
            {
                ProvisionCheck provisionCheck = new ProvisionCheck();

                handle = (Runnable)scheduledThreadPoolExecutor.scheduleWithFixedDelay(provisionCheck, 0, period, TimeUnit.SECONDS);
            }
            catch (Throwable throwable)
            {
                listeners.error("Unable to start client", throwable);
                setState(State.STOPPED);
            }
        }
    }

    /**
     * Stop the Boot Agent Provisioning Client.
     * <p/>
     * The currently provisioned entries are stopped.  This method can
     * potentially take a very long time to complete.
     */
    public void stop()
    {
        synchronized (lock)
        {
            if (state == State.STOPPING || state == State.STOPPED) return;

            setState(State.STOPPING);

            if (!scheduledThreadPoolExecutor.remove(handle)) listeners.warning("Was unable to remove task");
            handle = null;

            /**
             * wait for any starting entry to complete
             */
            try
            {
                semaphore.acquire();
            }
            catch (InterruptedException ie)
            {
                listeners.warning("Stop interrupted");
                Thread.currentThread().interrupt();
            }
            finally
            {
                semaphore.release();
            }

            try
            {
                shutdown();
            }
            finally
            {
                setState(State.STOPPED);
            }
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
            URL[] urls = provisionStore.getClasspath();

            if (urls.length == 0) return;

            URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);

            Thread.currentThread().setContextClassLoader(classLoader);

            @SuppressWarnings({"unchecked"}) Class<LifeCycle> bootClass = (Class<LifeCycle>)classLoader.loadClass(provisionStore.getCurrentProvisionDirective().getBootClass());

            lifeCycleInstance = bootClass.newInstance();

            lifeCycleInstance.start();

            setState(State.RUNNING);
        }
        catch (ProvisionStoreException pse)
        {
            listeners.error("Startup error", pse);
        }
        catch (ClassNotFoundException cnde)
        {
            listeners.error("Startup error", cnde);
        }
        catch (IllegalAccessException iae)
        {
            listeners.error("Startup error", iae);
        }
        catch (InstantiationException ie)
        {
            listeners.error("Startup error", ie);
        }
        catch (Throwable t)
        {
            listeners.error("Startup error", t);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(saved);
        }
    }

    /**
     * Internal shutdown method which calls the stop method of the loaded
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
            catch (Throwable t)
            {
                listeners.error("Shutdown error", t);
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
        /**
         * Perform a provision check.
         * <p/>
         * If an update needs to take place then the entities are downloaded
         * and stored first.  If there are no problems then the store is
         * instructed to mark the next provision directive.  If there are no
         * errors after that, the provision set is started if required.
         */
        public void run()
        {
            try
            {
                if (!semaphore.tryAcquire())
                {
                    listeners.warning("Attempted to run a provision check when one was already in play");
                    return;
                }

                final long currentVersion = provisionStore.getCurrentProvisionDirective().getVersion();
                final String uuid = provisionStore.getUuid();

                listeners.provisionCheck(uuid, currentVersion);

                ProvisionDirective response = provisionProvider.hello(uuid, currentVersion);

                listeners.provisionDirective(response);

                if (response instanceof DoNothing)
                {
                    if (getState() == State.STARTING) startup();
                    return;
                }

                YouShould should = (YouShould)response;

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

                try
                {
                    /**
                     * First make sure that we can instantiate an instance of
                     * the boot class so we don't end up needlessly restarting
                     * the server.
                     */
                    URL[] urls = provisionStore.getClasspath();
                    URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);

                    @SuppressWarnings({"unchecked"}) Class<LifeCycle> bootClass = (Class<LifeCycle>)classLoader.loadClass(provisionStore.getCurrentProvisionDirective().getBootClass());

                    bootClass.newInstance();

                    /**
                     * Start the server if we must
                     */
                    if (response instanceof YouMust)
                    {
                        YouMust must = (YouMust)response;

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

                    try
                    {
                        provisionStore.commitNext();
                    }
                    catch (ProvisionStoreException pse)
                    {
                        listeners.error("Provision check error", pse);
                    }
                }
                catch (ClassNotFoundException cnde)
                {
                    listeners.warning("Startup error", cnde);
                    provisionStore.rollbackNext();
                }
                catch (ClassCastException cce)
                {
                    listeners.warning("Startup error: boot class does not implement org.livetribe.boot.LifeCycle", cce);
                    provisionStore.rollbackNext();
                }
                catch (IllegalAccessException iae)
                {
                    listeners.warning("Startup error", iae);
                    provisionStore.rollbackNext();
                }
                catch (InstantiationException ie)
                {
                    listeners.warning("Startup error", ie);
                    provisionStore.rollbackNext();
                }
                catch (Throwable t)
                {
                    listeners.error("Startup error", t);
                    provisionStore.rollbackNext();
                }
            }
            catch (BootException bse)
            {
                listeners.warning("Provision check error", bse);
            }
            catch (ProvisionStoreException pse)
            {
                listeners.error("Provision check error", pse);
            }
            finally
            {
                semaphore.release();
            }
        }
    }
}
