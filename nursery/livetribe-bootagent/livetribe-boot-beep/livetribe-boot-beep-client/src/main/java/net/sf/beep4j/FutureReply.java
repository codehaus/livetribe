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
package net.sf.beep4j;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @version $Revision$ $Date$
 */
public class FutureReply<V> implements Future<V>, ReplyListener
{
    private final CountDownLatch LATCH = new CountDownLatch(1);
    private final FutureReplyListener<V> listener;
    private boolean canceled = false;
    private boolean receivedANS = false;
    private boolean interrupted = false;
    private V value;
    private Throwable throwable;

    public FutureReply(FutureReplyListener<V> listener)
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

    public synchronized void receiveANS(Message message)
    {
        try
        {
            if (!canceled)
            {
                listener.receiveANS(message);
                receivedANS = true;
            }
        }
        catch (Throwable t)
        {
            throwable = t;
            LATCH.countDown();
        }
    }

    public synchronized void receiveNUL()
    {
        try
        {
            if (!canceled) listener.receiveNUL();
        }
        catch (Throwable t)
        {
            throwable = t;
        }
        LATCH.countDown();
    }

    public synchronized void receiveERR(Message message)
    {
        try
        {
            if (!canceled) listener.receiveERR(message);
        }
        catch (Throwable t)
        {
            throwable = t;
        }
        LATCH.countDown();
    }

    public synchronized void receiveRPY(Message message)
    {
        try
        {
            if (!canceled) listener.receiveRPY(message);
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
}
