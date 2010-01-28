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
package org.livetribe.boot.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ContentProvider;


/**
 * An HTTP based content provider.
 *
 * @version $Revision: $ $Date: $
 */
public class HttpContentProvider implements ContentProvider
{
    public final static int DEFAULT_TIMEOUT = 60 * 1000;
    private final static String CLASS_NAME = HttpContentProvider.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final URL url;
    private volatile int timeout = DEFAULT_TIMEOUT;

    public HttpContentProvider(URL url)
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
    public InputStream pleaseProvide(String name, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "pleaseProvide", new Object[]{name, version});

        if (name == null) throw new IllegalArgumentException("name cannot be null");

        try
        {
            URL hello = new URL(url, "provide/" + name + "/" + version);

            HttpURLConnection connection = (HttpURLConnection)hello.openConnection();

            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(timeout);

            InputStream response = connection.getInputStream();

            LOGGER.exiting(CLASS_NAME, "pleaseProvide", response);

            return response;
        }
        catch (MalformedURLException mue)
        {
            throw new BootException("Unable to form URL for hello", mue);
        }
        catch (SocketTimeoutException ste)
        {
            throw new BootException("Hello timed out", ste);
        }
        catch (IOException ioe)
        {
            throw new BootException("Hello experienced an IO exception", ioe);
        }
    }
}