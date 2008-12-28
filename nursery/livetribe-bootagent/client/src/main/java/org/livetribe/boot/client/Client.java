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
 * @version $Revision$ $Date$
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

    public State getState()
    {
        return state;
    }

    protected void setState(State state)
    {
        listeners.stateChange(this.state, state);
        this.state = state;
    }

    public long getPeriod()
    {
        return period;
    }

    public void setPeriod(long period)
    {
        this.period = period;
    }

    public void addListener(ClientListener listener)
    {
        listeners.addListener(listener);
    }

    public void removeListener(ClientListener listener)
    {
        listeners.removeListener(listener);
    }

    public void start()
    {
        synchronized (LOCK)
        {
            if (state == State.STARTED || state == State.RUNNING) return;

            setState(State.STARTED);

            ProvisionCheck provisionCheck = new ProvisionCheck();

            provisionCheck.run();

            handle = (Runnable) scheduledThreadPoolExecutor.scheduleWithFixedDelay(provisionCheck, period, period, TimeUnit.SECONDS);

            setState(State.RUNNING);
        }
    }

    public void stop()
    {
        synchronized (LOCK)
        {
            if (state == State.STOPPING || state == State.STOPPED) return;

            setState(State.STOPPING);

            scheduledThreadPoolExecutor.remove(handle);
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

    @SuppressWarnings({"unchecked"})
    private void startup()
    {
        ClassLoader saved = Thread.currentThread().getContextClassLoader();
        try
        {
            List<URL> urls = provisionStore.getClasspath();
            URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), saved);

            Thread.currentThread().setContextClassLoader(classLoader);

            Class<LifeCycle> bootClass = (Class<LifeCycle>) classLoader.loadClass(provisionStore.getCurrentProvisionDirective().getBootClass());

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
            }
        }
        lifeCycleInstance = null;
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
                else if (state == State.STARTED)
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
