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
package org.livetribe.boot.ahc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.ProvisionProvider;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision: $ $Date: $
 */
public class HttpClient implements ProvisionProvider, ContentProvider
{
    public final static int DEFAULT_TIMEOUT = 60 * 1000;
    private final static String CLASS_NAME = HttpClient.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final URL url;
    private volatile int timeout = DEFAULT_TIMEOUT;

    public HttpClient(URL url)
    {
        if (url == null) throw new IllegalArgumentException("URL cannot be null");
        this.url = url;

        if (LOGGER.isLoggable(Level.CONFIG)) LOGGER.config("url: " + url);
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public ProvisionDirective hello(String uuid, long version) throws BootException
    {
        if (LOGGER.isLoggable(Level.FINER)) LOGGER.entering(CLASS_NAME, "hello", new Object[]{uuid, version});

        try
        {
            URL hello = new URL(url, "hello/" + uuid + "/" + version);

            HttpURLConnection connection = (HttpURLConnection) hello.openConnection();

            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(timeout);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = reader.readLine();

            if (line.length() == 0) throw new BootException("Empty message");

            String[] tokens = line.split(" ");
            long directedVersion = Long.parseLong(tokens[2]);
            String bootClass = tokens[1];

            Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
            int numEntries = Integer.parseInt(tokens[3]);
            for (int i = 0; i < numEntries; i++)
            {
                line = reader.readLine();
                String[] t = line.split(" ");

                entries.add(new ProvisionEntry(t[0], Long.parseLong(t[1])));
            }

            ProvisionDirective directive;
            if ("MUST".equals(tokens[0]))
            {
                directive = new YouMust(directedVersion, bootClass, entries, Boolean.parseBoolean(tokens[4]));
            }
            else if ("SHOULD".equals(tokens[0]))
            {
                directive = new YouShould(directedVersion, bootClass, entries);
            }
            else
            {
                directive = new DoNothing();
            }

            LOGGER.exiting(CLASS_NAME, "hello", directive);

            return directive;
        }
        catch (MalformedURLException mue)
        {
            LOGGER.log(Level.SEVERE, "Unable to form URL for hello", mue);
            throw new BootException("Unable to form URL for hello", mue);
        }
        catch (SocketTimeoutException ste)
        {
            LOGGER.log(Level.SEVERE, "Hello timed out", ste);
            throw new BootException("Hello timed out", ste);
        }
        catch (NumberFormatException nfe)
        {
            LOGGER.log(Level.SEVERE, "Number could not be parsed", nfe);
            throw new BootException("Number could not be parsed", nfe);
        }
        catch (IOException ioe)
        {
            LOGGER.log(Level.SEVERE, "Hello experience an IO exception", ioe);
            throw new BootException("Hello experience an IO exception", ioe);
        }
        catch (ArrayIndexOutOfBoundsException aioobe)
        {
            LOGGER.log(Level.SEVERE, "Hello response was malformed", aioobe);
            throw new BootException("Hello response was malformed", aioobe);
        }
    }

    public InputStream pleaseProvide(String name, long version) throws BootException
    {
        if (LOGGER.isLoggable(Level.FINER)) LOGGER.entering(CLASS_NAME, "pleaseProvide", new Object[]{name, version});

        try
        {
            URL hello = new URL(url, "provide/" + name + "/" + version);

            HttpURLConnection connection = (HttpURLConnection) hello.openConnection();

            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(timeout);

            InputStream response = connection.getInputStream();

            LOGGER.exiting(CLASS_NAME, "pleaseProvide", response);

            return response;
        }
        catch (MalformedURLException mue)
        {
            LOGGER.log(Level.SEVERE, "Unable to form URL for hello", mue);
            throw new BootException("Unable to form URL for hello", mue);
        }
        catch (SocketTimeoutException ste)
        {
            LOGGER.log(Level.SEVERE, "Hello timed out", ste);
            throw new BootException("Hello timed out", ste);
        }
        catch (IOException ioe)
        {
            LOGGER.log(Level.SEVERE, "Hello experience an IO exception", ioe);
            throw new BootException("Hello experience an IO exception", ioe);
        }
    }
}
