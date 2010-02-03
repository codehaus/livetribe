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
package org.livetribe.boot.startup.jsw;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

import org.livetribe.boot.client.Client;
import org.livetribe.boot.client.DefaultProvisionStore;
import org.livetribe.boot.client.ProvisionStore;
import org.livetribe.boot.http.HttpContentProvider;
import org.livetribe.boot.http.HttpProvisionProvider;
import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.ProvisionProvider;


/**
 * @version $Revision: $ $Date: $
 */
public class BootAgentWrapperListener implements WrapperListener
{
    private final static String CLASS_NAME = BootAgentWrapperListener.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private volatile Client client;

    private BootAgentWrapperListener() { }

    /**
     * {@inheritDoc}
     */
    public Integer start(String[] arguments)
    {
        LOGGER.entering(CLASS_NAME, "start", arguments);

        URL url = null;
        int threadPoolSize = 5;
        File storeRoot = new File(".");

        Integer result = null;
        try
        {
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

            client.start(false);
        }
        catch (MalformedURLException mue)
        {
            result = 1;
            LOGGER.log(Level.SEVERE, "", mue);
        }

        LOGGER.exiting(CLASS_NAME, "start", result);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int stop(int exitCode)
    {
        LOGGER.entering(CLASS_NAME, "stop", exitCode);

        WrapperManager.signalStopping(60000);

        client.stop();
        client = null;

        LOGGER.exiting(CLASS_NAME, "stop", exitCode);

        return exitCode;
    }

    /**
     * {@inheritDoc}
     */
    public void controlEvent(int event)
    {
        if (event == WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT && WrapperManager.isLaunchedAsService())
        {
            // Ignore
        }
        else
        {
            WrapperManager.stop(0);
            // Will not get here.
        }
    }

    public static void main(String[] args)
    {
        // Start the application.  If the JVM was launched from the native
        //  Wrapper then the application will wait for the native Wrapper to
        //  call the application's start method.  Otherwise the start method
        //  will be called immediately.
        WrapperManager.start(new BootAgentWrapperListener(), args);
    }
}
