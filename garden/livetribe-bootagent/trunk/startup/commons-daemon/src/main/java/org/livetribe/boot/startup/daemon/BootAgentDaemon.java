/**
 *
 * Copyright 2010 (C) The original author or authors
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
package org.livetribe.boot.startup.daemon;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;

import org.livetribe.boot.client.Client;
import org.livetribe.boot.client.ClientListener;
import org.livetribe.boot.client.DefaultProvisionStore;
import org.livetribe.boot.client.ProvisionStore;
import org.livetribe.boot.client.State;
import org.livetribe.boot.http.HttpContentProvider;
import org.livetribe.boot.http.HttpProvisionProvider;
import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionProvider;


/**
 * @version $Revision: $ $Date: $
 */
public class BootAgentDaemon implements Daemon
{
    private final static String CLASS_NAME = BootAgentDaemon.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private volatile Client client;

    public void init(DaemonContext context) throws Exception
    {
        LOGGER.entering(CLASS_NAME, "init", context);

        String[] arguments = context.getArguments();

        URL url = null;
        int threadPoolSize = 5;
        File storeRoot = new File(".");

        for (int i = 0; i < arguments.length; i++)
        {
            if ("-url".equals(arguments[i])) url = new URL(arguments[++i]);
            if ("-pool".equals(arguments[i])) threadPoolSize = Integer.getInteger(arguments[++i]);
            if ("-root".equals(arguments[i])) storeRoot = new File(arguments[++i]);
        }

        if (url == null) throw new IllegalArgumentException("URL is missing");

        ProvisionProvider provisionProvider = new HttpProvisionProvider(url);
        ContentProvider contentProvider = new HttpContentProvider(url);
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(threadPoolSize);
        ProvisionStore provisionStore = new DefaultProvisionStore(storeRoot);

        client = new Client(provisionProvider, contentProvider, pool, provisionStore);

        final DaemonController controller = context.getController();

        client.addListener(new ClientListener()
        {
            public void stateChange(State oldState, State newState) { }

            public void provisionCheck(String uuid, long version) { }

            public void provisionDirective(ProvisionDirective directive) { }

            public void warning(String message) { }

            public void warning(String message, Throwable throwable) { }

            public void error(String message)
            {
                controller.fail(message);
            }

            @SuppressWarnings({"ThrowableInstanceNeverThrown"})
            public void error(String message, Throwable throwable)
            {
                controller.fail(message, new Exception("Wrapped exception", throwable));
            }
        });

        LOGGER.exiting(CLASS_NAME, "init");
    }

    public void start() throws Exception
    {
        LOGGER.entering(CLASS_NAME, "start");

        client.start();

        LOGGER.exiting(CLASS_NAME, "start");
    }

    public void stop() throws Exception
    {
        LOGGER.entering(CLASS_NAME, "stop");

        client.stop();

        LOGGER.exiting(CLASS_NAME, "stop");
    }

    public void destroy()
    {
        LOGGER.entering(CLASS_NAME, "destroy");

        client = null;

        LOGGER.exiting(CLASS_NAME, "destroy");
    }
}
