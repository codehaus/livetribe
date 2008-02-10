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
package org.livetribe.boot.http;

import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.ahc.AsyncHttpClientCallback;
import org.apache.ahc.codec.HttpResponseMessage;


/**
 * @version $Revision$ $Date$
 */
public class FutureResponse<V> implements Future<V>, AsyncHttpClientCallback
{
    private final CountDownLatch LATCH = new CountDownLatch(1);
    private final FutureAhcCallback<V> listener;
    private boolean canceled = false;
    private boolean receivedANS = false;
    private boolean interrupted = false;
    private V value;
    private Throwable throwable;

    public FutureResponse(FutureAhcCallback<V> listener)
    {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");

        this.listener = listener;
        this.listener.setOwner(this);
    }

    public synchronized boolean cancel(boolean mayInterruptIfRunning)
    {
        if (isDone()) return false;
        canceled = true;
        interrupted = receivedANS && mayInterruptIfRunning;
        return canceled;
    }

    public synchronized boolean isCancelled()
    {
        return canceled;
    }

    public synchronized boolean isDone()
    {
        return LATCH.getCount() == 0;
    }

    public V get() throws InterruptedException, ExecutionException
    {
        LATCH.await();

        if (canceled) throw new CancellationException();
        if (interrupted) throw new InterruptedException();
        if (throwable != null) throw new ExecutionException(throwable);

        return value;
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        if (!LATCH.await(timeout, unit)) throw new TimeoutException();

        if (canceled) throw new CancellationException();
        if (interrupted) throw new InterruptedException();
        if (throwable != null) throw new ExecutionException(throwable);

        return value;
    }

    public void onResponse(HttpResponseMessage message)
    {
        try
        {
            if (!canceled) listener.onResponse(message);
        }
        catch (Throwable t)
        {
            throwable = t;
        }
        LATCH.countDown();
    }

    public void onException(Throwable cause)
    {
        try
        {
            if (!canceled) listener.onException(cause);
        }
        catch (Throwable t)
        {
            throwable = t;
        }
        LATCH.countDown();
    }

    public void onClosed()
    {
        try
        {
            if (!canceled) listener.onClosed();
        }
        catch (Throwable t)
        {
            throwable = t;
        }
        LATCH.countDown();
    }

    public void onTimeout()
    {
        try
        {
            if (!canceled) listener.onTimeout();
        }
        catch (Throwable t)
        {
            throwable = t;
        }
        LATCH.countDown();
    }

    protected final void setValue(V value)
    {
        this.value = value;
    }

    protected final void setThrowable(Throwable throwable)
    {
        this.throwable = throwable;
    }
}