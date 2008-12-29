/**
 *
 * Copyright 2008 (C) The original author or authors
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
package org.livetribe.ec2.rest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.asyncweb.client.AsyncHttpClientCallback;
import org.apache.asyncweb.client.codec.HttpResponseMessage;

import org.livetribe.ec2.api.S3Exception;


/**
 * EC2 API specific implementation of <code>AsyncHttpClientCallback</code>
 *
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public class EC2Callback implements AsyncHttpClientCallback
{
    private final CountDownLatch complete = new CountDownLatch(1);
    private volatile boolean closed;
    private volatile Throwable throwable;
    private volatile HttpResponseMessage message;

    public void onResponse(HttpResponseMessage response)
    {
        message = response;
        complete.countDown();
    }

    public void onException(Throwable cause)
    {
        throwable = cause;
        complete.countDown();
    }

    public void onClosed()
    {
        closed = true;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public void onTimeout()
    {
        complete.countDown();
    }

    public HttpResponseMessage getMessage() throws InterruptedException, S3Exception
    {
        return getMessage(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

    public HttpResponseMessage getMessage(int timeout, TimeUnit unit) throws InterruptedException, S3Exception
    {
        if (!complete.await(timeout, unit)) throw new S3Exception("Timeout waiting for reply");
        if (throwable != null) throw new S3Exception("Error getting HTTP response", throwable);
        return message;
    }
}
