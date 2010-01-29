/**
 *
 * Copyright 2009-2010 (C) The original author or authors
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

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.ProvisionProvider;


/**
 * A simple utility "wrapper" that provides <code>Future</code> handles to the
 * start and stop methods of a client instance.
 * <p/>
 * Use this wrapper to asynchronously start and stop this client instnace.
 *
 * @version $Revision$ $Date$
 * @see Client
 */
public class AsynchronousClientStarter extends Client
{
    /**
     * {@inheritDoc}
     */
    public AsynchronousClientStarter(ProvisionProvider provisionProvider, ContentProvider contentProvider, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, ProvisionStore provisionStore)
    {
        super(provisionProvider, contentProvider, scheduledThreadPoolExecutor, provisionStore);
    }

    /**
     * Start the Boot Agent Provisioning Client.
     * <p/>
     * A provisioning check is scheduled to run as soon as possible which
     * results in the provisioned entries being started.
     *
     * @return a Future representing pending completion of the start task
     * @see Client#start()
     */
    Future<Void> asyncStart()
    {
        return getScheduledThreadPoolExecutor().submit(new Callable<Void>()
        {
            public Void call() throws Exception
            {
                start();

                return null;
            }
        });
    }

    /**
     * Stop the Boot Agent Provisioning Client.
     * <p/>
     * The currently provisioned artifacts are stopped.
     *
     * @return a Future representing pending completion of the stop task
     * @see Client#stop()
     */
    Future<Void> asyncStop()
    {
        return getScheduledThreadPoolExecutor().submit(new Callable<Void>()
        {
            public Void call() throws Exception
            {
                stop();

                return null;
            }
        });
    }
}
