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
package org.livetribe.boot.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.ProvisionProvider;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * An HTTP based provisioning provider.
 * <p/>
 * Any provisioning commands returned from the server that are not understood
 * will cause a boot exception to be thrown.
 *
 * @version $Revision: $ $Date: $
 */
public class HttpProvisionProvider implements ProvisionProvider
{
    public final static int DEFAULT_TIMEOUT = 60 * 1000;
    private final static String CLASS_NAME = HttpProvisionProvider.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final URL url;
    private volatile int timeout = DEFAULT_TIMEOUT;

    public HttpProvisionProvider(URL url)
    {
        if (url == null) throw new IllegalArgumentException("URL cannot be null");
        this.url = url;

        if (LOGGER.isLoggable(Level.CONFIG)) LOGGER.config("url: " + url);
    }

    /**
     * Obtain the HTTP read timeout in seconds
     *
     * @return the HTTP read timeout in seconds
     */
    public int getTimeout()
    {
        return timeout;
    }

    /**
     * Set the HTTP read timeout in seconds
     *
     * @param timeout the HTTP read timeout in seconds
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    /**
     * {@inheritDoc}
     */
    public ProvisionDirective hello(String uuid, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "hello", new Object[]{uuid, version});

        if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");

        ProvisionDirective directive;
        try
        {
            directive = doHello(uuid + "/current", version);
        }
        catch (BootException be)
        {
            directive = doHello("current", version);
        }

        LOGGER.exiting(CLASS_NAME, "hello", directive);

        return directive;
    }

    private ProvisionDirective doHello(String path, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "doHello", new Object[]{path, version});

        if (path == null) throw new IllegalArgumentException("path cannot be null");

        try
        {
            URL hello = new URL(url, path);

            HttpURLConnection connection = (HttpURLConnection)hello.openConnection();

            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(timeout);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = reader.readLine();

            if (line.length() == 0) throw new BootException("Empty message");

            ProvisionDirective directive;
            String[] tokens = line.split(" ");
            long directedVersion = Long.parseLong(tokens[2]);

            if (directedVersion <= version)
            {
                directive = new DoNothing();

                LOGGER.exiting(CLASS_NAME, "doHello", directive);

                return directive;
            }

            String bootClass = tokens[1];

            Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
            int numEntries = Integer.parseInt(tokens[3]);
            for (int i = 0; i < numEntries; i++)
            {
                line = reader.readLine();
                String[] t = line.split(" ");

                entries.add(new ProvisionEntry(t[0], Long.parseLong(t[1])));
            }

            if ("MUST".equals(tokens[0]))
            {
                directive = new YouMust(directedVersion, bootClass, entries, Boolean.parseBoolean(tokens[4]));
            }
            else if ("SHOULD".equals(tokens[0]))
            {
                directive = new YouShould(directedVersion, bootClass, entries);
            }
            else if ("DO_NOTHING".equals(tokens[0]))
            {
                directive = new DoNothing();
            }
            else
            {
                LOGGER.warning("Unable to recognize directive " + tokens[0]);
                throw new BootException("Unable to recognize directive " + tokens[0]);
            }

            LOGGER.exiting(CLASS_NAME, "doHello", directive);

            return directive;
        }
        catch (MalformedURLException mue)
        {
            throw new BootException("Unable to form URL for hello", mue);
        }
        catch (SocketTimeoutException ste)
        {
            throw new BootException("Hello timed out", ste);
        }
        catch (NumberFormatException nfe)
        {
            throw new BootException("Number could not be parsed", nfe);
        }
        catch (IOException ioe)
        {
            throw new BootException("Hello experienced an IO exception", ioe);
        }
        catch (ArrayIndexOutOfBoundsException aioobe)
        {
            throw new BootException("Hello response was malformed", aioobe);
        }
    }
}