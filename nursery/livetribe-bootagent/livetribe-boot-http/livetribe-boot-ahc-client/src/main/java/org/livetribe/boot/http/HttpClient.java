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
package org.livetribe.boot.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ahc.AsyncHttpClient;
import org.apache.ahc.codec.HttpRequestMessage;
import org.apache.ahc.codec.HttpResponseMessage;
import org.apache.ahc.codec.ResponseFuture;

import org.livetribe.boot.protocol.BootServer;
import org.livetribe.boot.protocol.BootServerException;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision: $ $Date: $
 */
public class HttpClient implements BootServer
{
    private final static String className = HttpClient.class.getName();
    private final static Logger logger = Logger.getLogger(className);
    private final URL url;
    private int timeout;

    public HttpClient(URL url)
    {
        if (url == null) throw new IllegalArgumentException("URL cannot be null");
        this.url = url;

        if (logger.isLoggable(Level.CONFIG)) logger.config("url: " + url);
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public YouShould hello(String uuid, long version) throws BootServerException
    {
        if (logger.isLoggable(Level.FINER)) logger.entering(className, "hello", new Object[]{uuid, version});

        try
        {
            URL hello = new URL(url, "hello/" + uuid + "/" + version);
            AsyncHttpClient ahc = new AsyncHttpClient();

            ResponseFuture rf = ahc.sendRequest(new HttpRequestMessage(hello, null));

            HttpResponseMessage message = rf.get(timeout, TimeUnit.SECONDS);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message.getContent())));
            String line = reader.readLine();

            if (line.length() == 0) throw new BootServerException("Empty message");

            String[] tokens = line.split(" ");
            long directedVersion = Long.parseLong(tokens[1]);
            String bootClass = tokens[2];
            Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();

            YouShould directive;
            if ("MUST".equals(tokens[0]))
            {
                directive = new YouMust(directedVersion, bootClass, entries, Boolean.parseBoolean(tokens[4]));
            }
            else
            {
                directive = new YouShould(directedVersion, bootClass, entries);
            }

            int numEntries = Integer.parseInt(tokens[3]);
            for (int i = 0; i < numEntries; i++)
            {
                line = reader.readLine();
                tokens = line.split(" ");

                ProvisionEntry entry = new ProvisionEntry(tokens[0], Long.parseLong(tokens[1]));

                entries.add(entry);
            }

            logger.exiting(className, "hello", directive);

            return directive;
        }
        catch (MalformedURLException mue)
        {
            logger.log(Level.SEVERE, "Unable to form URL for hello", mue);
            throw new BootServerException("Unable to form URL for hello", mue);
        }
        catch (ExecutionException ee)
        {
            logger.log(Level.SEVERE, "Unable to send hello", ee.getCause());
            throw new BootServerException("Unable to send hello", ee.getCause());
        }
        catch (InterruptedException ie)
        {
            logger.log(Level.SEVERE, "Hello interrupted", ie);
            throw new BootServerException("Hello interrupted", ie);
        }
        catch (TimeoutException te)
        {
            logger.log(Level.SEVERE, "Hello timed out", te);
            throw new BootServerException("Hello timed out", te);
        }
        catch (NumberFormatException nfe)
        {
            logger.log(Level.SEVERE, "Number could not be parsed", nfe);
            throw new BootServerException("Number could not be parsed", nfe);
        }
        catch (IOException ioe)
        {
            logger.log(Level.SEVERE, "Hello experience an IO exception", ioe);
            throw new BootServerException("Hello experience an IO exception", ioe);
        }
        catch (ArrayIndexOutOfBoundsException aioobe)
        {
            logger.log(Level.SEVERE, "Hello response was malformed", aioobe);
            throw new BootServerException("Hello response was malformed", aioobe);
        }
    }

    public InputStream pleaseProvide(String name, long version) throws BootServerException
    {
        if (logger.isLoggable(Level.FINER)) logger.entering(className, "pleaseProvide", new Object[]{name, version});

        try
        {
            URL hello = new URL(url, "provide/" + name + "/" + version);
            AsyncHttpClient ahc = new AsyncHttpClient();

            ResponseFuture rf = ahc.sendRequest(new HttpRequestMessage(hello, null));

            HttpResponseMessage message = rf.get(timeout, TimeUnit.SECONDS);

            InputStream response = new ByteArrayInputStream(message.getContent());

            logger.exiting(className, "pleaseProvide", response);

            return response;
        }
        catch (MalformedURLException mue)
        {
            logger.log(Level.SEVERE, "Unable to form URL for hello", mue);
            throw new BootServerException("Unable to form URL for hello", mue);
        }
        catch (ExecutionException ee)
        {
            logger.log(Level.SEVERE, "Unable to send hello", ee.getCause());
            throw new BootServerException("Unable to send hello", ee.getCause());
        }
        catch (InterruptedException ie)
        {
            logger.log(Level.SEVERE, "Hello interrupted", ie);
            throw new BootServerException("Hello interrupted", ie);
        }
        catch (TimeoutException te)
        {
            logger.log(Level.SEVERE, "Hello timed out", te);
            throw new BootServerException("Hello timed out", te);
        }
    }
}
