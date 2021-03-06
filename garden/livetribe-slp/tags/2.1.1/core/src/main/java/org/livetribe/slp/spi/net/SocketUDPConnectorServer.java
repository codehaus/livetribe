/*
 * Copyright 2007-2008 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.slp.spi.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;

import org.livetribe.slp.SLPError;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.settings.Defaults;
import static org.livetribe.slp.settings.Keys.ADDRESSES_KEY;
import static org.livetribe.slp.settings.Keys.BROADCAST_ENABLED_KEY;
import static org.livetribe.slp.settings.Keys.MAX_TRANSMISSION_UNIT_KEY;
import static org.livetribe.slp.settings.Keys.MULTICAST_TIME_TO_LIVE_KEY;
import static org.livetribe.slp.settings.Keys.NOTIFICATION_PORT_KEY;
import static org.livetribe.slp.settings.Keys.PORT_KEY;
import org.livetribe.slp.settings.Settings;
import org.livetribe.slp.spi.msg.Message;


/**
 * @version $Revision$ $Date$
 */
public abstract class SocketUDPConnectorServer extends AbstractConnectorServer implements UDPConnectorServer
{
    private final ExecutorService threadPool;
    private final int bindPort;
    private String[] addresses = Defaults.get(ADDRESSES_KEY);
    private int maxTransmissionUnit = Defaults.get(MAX_TRANSMISSION_UNIT_KEY);
    private int multicastTimeToLive = Defaults.get(MULTICAST_TIME_TO_LIVE_KEY);
    private volatile CountDownLatch startBarrier;
    private volatile CountDownLatch stopBarrier;
    private MulticastSocket[] multicastSockets;

    public SocketUDPConnectorServer(ExecutorService threadPool, int bindPort)
    {
        this(threadPool, bindPort, null);
    }

    public SocketUDPConnectorServer(ExecutorService threadPool, int bindPort, Settings settings)
    {
        this.threadPool = threadPool;
        this.bindPort = bindPort;
        if (settings != null) setSettings(settings);
    }

    private void setSettings(Settings settings)
    {
        if (settings.containsKey(ADDRESSES_KEY)) this.addresses = settings.get(ADDRESSES_KEY);
        if (settings.containsKey(MAX_TRANSMISSION_UNIT_KEY))
            this.maxTransmissionUnit = settings.get(MAX_TRANSMISSION_UNIT_KEY);
        if (settings.containsKey(MULTICAST_TIME_TO_LIVE_KEY))
            this.multicastTimeToLive = settings.get(MULTICAST_TIME_TO_LIVE_KEY);
    }

    protected int getBindPort()
    {
        return bindPort;
    }

    public String[] getAddresses()
    {
        return addresses;
    }

    public void setAddresses(String[] addresses)
    {
        this.addresses = addresses;
    }

    public int getMaxTransmissionUnit()
    {
        return maxTransmissionUnit;
    }

    public void setMaxTransmissionUnit(int maxTransmissionUnit)
    {
        this.maxTransmissionUnit = maxTransmissionUnit;
    }

    public int getMulticastTimeToLive()
    {
        return multicastTimeToLive;
    }

    public void setMulticastTimeToLive(int multicastTimeToLive)
    {
        this.multicastTimeToLive = multicastTimeToLive;
    }

    protected void doStart()
    {
        int size = addresses.length;
        startBarrier = new CountDownLatch(size);
        stopBarrier = new CountDownLatch(size);
        multicastSockets = new MulticastSocket[size];
        Runnable[] receivers = new Runnable[size];
        for (int i = 0; i < size; ++i)
        {
            InetSocketAddress bindAddress = new InetSocketAddress(addresses[i], getBindPort());
            multicastSockets[i] = newMulticastSocket(bindAddress);
            receivers[i] = new Receiver(multicastSockets[i]);
            receive(receivers[i]);
        }
        waitForStart();
    }

    private void waitForStart()
    {
        try
        {
            startBarrier.await();
        }
        catch (InterruptedException x)
        {
            Thread.currentThread().interrupt();
            throw new ServiceLocationException("Could not start TCPConnectorServer " + this, SLPError.NETWORK_INIT_FAILED);
        }
    }

    protected abstract MulticastSocket newMulticastSocket(InetSocketAddress bindAddress);

    @Override
    public boolean isRunning()
    {
        return super.isRunning() && stopBarrier.getCount() > 0;
    }

    protected void doStop()
    {
        for (MulticastSocket multicastSocket : multicastSockets) closeMulticastSocket(multicastSocket);
        threadPool.shutdownNow();
        clearMessageListeners();
        waitForStop();
    }

    private void waitForStop()
    {
        try
        {
            stopBarrier.await();
        }
        catch (InterruptedException x)
        {
            Thread.currentThread().interrupt();
            throw new ServiceLocationException("Could not stop TCPConnectorServer " + this, SLPError.NETWORK_ERROR);
        }
    }

    protected abstract void closeMulticastSocket(MulticastSocket multicastSocket);

    protected void receive(Runnable receiver)
    {
        threadPool.execute(receiver);
    }

    private void handle(Runnable handler) throws RejectedExecutionException
    {
        try
        {
            threadPool.execute(handler);
        }
        catch (RejectedExecutionException x)
        {
            // Connector server stopped just after having received a datagram
            if (logger.isLoggable(Level.FINEST))
                logger.log(Level.FINEST, "UDPConnectorServer " + this + " stopping, rejecting execution of " + handler);
            throw x;
        }
    }

    protected class Receiver implements Runnable
    {
        private final DatagramSocket datagramSocket;

        public Receiver(DatagramSocket datagramSocket)
        {
            this.datagramSocket = datagramSocket;
        }

        public void run()
        {
            if (logger.isLoggable(Level.FINER))
                logger.finer("DatagramSocket acceptor running for " + datagramSocket + " in thread " + Thread.currentThread().getName());

            startBarrier.countDown();

            try
            {
                InetSocketAddress localAddress = (InetSocketAddress)datagramSocket.getLocalSocketAddress();
                while (true)
                {
                    byte[] buffer = new byte[maxTransmissionUnit];
                    DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                    datagramSocket.receive(packet);
                    if (logger.isLoggable(Level.FINER))
                        logger.finer("Received datagram packet " + packet + " on socket " + datagramSocket + ": " + packet.getLength() + " bytes from " + packet.getSocketAddress());
                    InetSocketAddress remoteAddress = (InetSocketAddress)packet.getSocketAddress();
                    handle(new Handler(packet, localAddress, remoteAddress));
                }
            }
            catch (SocketException x)
            {
                if (logger.isLoggable(Level.FINEST))
                    logger.log(Level.FINEST, "Closed MulticastSocket " + datagramSocket);
            }
            catch (RejectedExecutionException x)
            {
                // The connector server is stopping, just exit (see handle(Runnable))
            }
            catch (IOException x)
            {
                if (logger.isLoggable(Level.INFO)) logger.log(Level.INFO, "Unexpected IOException", x);
            }
            finally
            {
                if (logger.isLoggable(Level.FINER))
                    logger.finer("MulticastSocket acceptor exiting for " + datagramSocket + " in thread " + Thread.currentThread().getName());

                stopBarrier.countDown();
            }
        }
    }

    private class Handler implements Runnable
    {
        private final DatagramPacket packet;
        private final InetSocketAddress localAddress;
        private final InetSocketAddress remoteAddress;

        public Handler(DatagramPacket packet, InetSocketAddress localAddress, InetSocketAddress remoteAddress)
        {
            this.packet = packet;
            this.localAddress = localAddress;
            this.remoteAddress = remoteAddress;
        }

        public void run()
        {
            if (logger.isLoggable(Level.FINER))
                logger.finer("DatagramPacket handler running for " + packet + " in thread " + Thread.currentThread().getName());

            try
            {
                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, data.length);
                Message message = Message.deserialize(data);
                MessageEvent event = new MessageEvent(packet, message, localAddress, remoteAddress);
                if (logger.isLoggable(Level.FINEST))
                    logger.finest("Notifying message listeners of new message " + message + " from " + remoteAddress);
                notifyMessageListeners(event);
            }
            catch (ServiceLocationException x)
            {
                if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "", x);
            }
            finally
            {
                if (logger.isLoggable(Level.FINER))
                    logger.finer("DatagramPacket handler exiting for " + packet + " in thread " + Thread.currentThread().getName());
            }
        }
    }

    public static class Factory implements UDPConnectorServer.Factory
    {
        public UDPConnectorServer newUDPConnectorServer(Settings settings)
        {
            int udpBindPort = settings == null ? Defaults.get(PORT_KEY) : settings.get(PORT_KEY, Defaults.get(PORT_KEY));
            return newUDPConnectorServer(settings, udpBindPort);
        }

        public UDPConnectorServer newNotificationUDPConnectorServer(Settings settings)
        {
            int udpBindPort = settings == null ? Defaults.get(NOTIFICATION_PORT_KEY) : settings.get(NOTIFICATION_PORT_KEY, Defaults.get(NOTIFICATION_PORT_KEY));
            return newUDPConnectorServer(settings, udpBindPort);
        }

        private UDPConnectorServer newUDPConnectorServer(Settings settings, int bindPort)
        {
            ExecutorService threadPool = Executors.newCachedThreadPool();
            Boolean broadcastEnabled = settings == null ? Boolean.FALSE : settings.get(BROADCAST_ENABLED_KEY, Defaults.get(BROADCAST_ENABLED_KEY));
            if (broadcastEnabled == null || !broadcastEnabled)
                return new MulticastSocketUDPConnectorServer(threadPool, bindPort, settings);
            else
                return new BroadcastSocketUDPConnectorServer(threadPool, bindPort, settings);
        }
    }
}
