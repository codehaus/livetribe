/**
 *
 * Copyright 2005 (C) The original author or authors.
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
package org.livetribe.totem.single;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.TimeoutException;
import junit.framework.TestCase;
import org.activeio.command.AsyncChannelToAsyncCommandChannel;
import org.activeio.command.AsyncCommandChannel;
import org.activeio.command.CommandListener;
import org.activeio.net.DatagramSocketSyncChannelFactory;


/**
 * @version $Revision: $ $Date: $
 */
public class TokenRingChannelTest extends TestCase
{
    // TODO: use a shared Executor for the channels
    private ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(5);

    public void XtestRotation() throws Exception
    {
        final Semaphore available = new Semaphore(1, true);
        final boolean[] done = new boolean[]{false};
        final boolean[] error = new boolean[]{false};

        URI meURI = new URI("udp://localhost:2265");
        URI youURI = new URI("udp://localhost:2122");

        TokenRingChannel meChannel = new TokenRingChannel(new DatagramSocketSyncChannelFactory().openSyncChannel(null, meURI));
        meChannel.setAddress(InetAddress.getByName(youURI.getHost()));
        meChannel.setPort(youURI.getPort());
        final AsyncCommandChannel me = new AsyncChannelToAsyncCommandChannel(meChannel, new DefaultWireFormat());
        me.setCommandListener(new CommandListener()
        {
            public void onCommand(Object command)
            {
                try
                {
                    me.writeCommand(command);
                }
                catch (IOException e)
                {
                    error[0] = true;
                }
            }

            public void onError(Exception e)
            {
                error[0] = true;
            }
        });
        me.start();

        TokenRingChannel youChannel = new TokenRingChannel(new DatagramSocketSyncChannelFactory().openSyncChannel(null, youURI));
        youChannel.setAddress(InetAddress.getByName(meURI.getHost()));
        youChannel.setPort(meURI.getPort());
        final AsyncCommandChannel you = new AsyncChannelToAsyncCommandChannel(youChannel, new DefaultWireFormat());
        you.setCommandListener(new CommandListener()
        {
            public void onCommand(Object command)
            {
                try
                {
                    if ("GOODBYE".equals(command))
                    {
                        done[0] = true;
                        available.release();
                    }
                    else if ("HELLO".equals(command))
                    {
                        you.writeCommand("GOODBYE");
                    }
                }
                catch (IOException e)
                {
                    error[0] = true;
                }
            }

            public void onError(Exception e)
            {
                error[0] = true;
            }
        });
        you.start();

        available.acquire();

        scheduler.schedule(new Runnable()
        {
            public void run()
            {
                error[0] = true;
                done[0] = true;
            }
        }, 1, TimeUnit.MINUTES);

        you.writeCommand("HELLO");

        while (!done[0])
        {
            available.tryAcquire(100, TimeUnit.MILLISECONDS);
        }

        me.stop(0);
        you.stop(0);

        if (error[0])
        {
            throw new Exception("Error occured");
        }
    }

    public void testBenchmark() throws Exception
    {
        final Semaphore available = new Semaphore(1, true);
        final boolean[] done = new boolean[]{false};
        final Exception[] error = new Exception[]{null};
        final boolean[] timeout = new boolean[]{false};
        final int[] count = new int[1];
        final int MAX = 10000;

        URI meURI = new URI("udp://localhost:2265");
        URI youURI = new URI("udp://localhost:2122");

        TokenRingChannel meChannel = new TokenRingChannel(new DatagramSocketSyncChannelFactory().openSyncChannel(null, meURI));
        meChannel.setAddress(InetAddress.getByName(youURI.getHost()));
        meChannel.setPort(youURI.getPort());
        final AsyncCommandChannel me = new AsyncChannelToAsyncCommandChannel(meChannel, new DefaultWireFormat());
        me.setCommandListener(new CommandListener()
        {
            public void onCommand(Object command)
            {
                try
                {
                    me.writeCommand(command);
                }
                catch (IOException e)
                {
                    error[0] = e;
                }
            }

            public void onError(Exception e)
            {
                error[0] = e;
            }
        });
        me.start();

        TokenRingChannel youChannel = new TokenRingChannel(new DatagramSocketSyncChannelFactory().openSyncChannel(null, youURI));
        youChannel.setAddress(InetAddress.getByName(meURI.getHost()));
        youChannel.setPort(meURI.getPort());
        final AsyncCommandChannel you = new AsyncChannelToAsyncCommandChannel(youChannel, new DefaultWireFormat());
        you.setCommandListener(new CommandListener()
        {
            public void onCommand(Object command)
            {
                try
                {
                    if ("GOODBYE".equals(command))
                    {
                        done[0] = true;
                        available.release();
                    }
                    else if ("HELLO".equals(command))
                    {
                        if (count[0]++ > MAX)
                        {
                            you.writeCommand("GOODBYE");
                        }
                        else
                        {
                            you.writeCommand(command);
                        }
                    }
                }
                catch (IOException e)
                {
                    error[0] = e;
                }
            }

            public void onError(Exception e)
            {
                error[0] = e;
            }
        });
        you.start();

        long start = System.currentTimeMillis();

        available.acquire();

        scheduler.schedule(new Runnable()
        {
            public void run()
            {
                timeout[0] = true;
                done[0] = true;
            }
        }, 1, TimeUnit.MINUTES);

        you.writeCommand("HELLO");

        while (!done[0])
        {
            available.tryAcquire(100, TimeUnit.MILLISECONDS);
        }

        me.stop(0);
        you.stop(0);

        long stop = System.currentTimeMillis();

        double seconds = (stop - start) / 1000.0;
        System.err.println("Elapsed time: " + seconds + "s");
        System.err.println("Average round trip: " + (double) (stop - start) / MAX + "ms");

        if (error[0] != null)
        {
            throw error[0];
        }

        if (timeout[0])
        {
            throw new TimeoutException();
        }
    }

}
