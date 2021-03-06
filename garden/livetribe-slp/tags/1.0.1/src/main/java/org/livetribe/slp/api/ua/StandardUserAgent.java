/*
 * Copyright 2006 the original author or authors
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
package org.livetribe.slp.api.ua;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadFactory;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import org.livetribe.slp.Attributes;
import org.livetribe.slp.Scopes;
import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.ServiceType;
import org.livetribe.slp.ServiceURL;
import org.livetribe.slp.api.StandardAgent;
import org.livetribe.slp.spi.Defaults;
import org.livetribe.slp.spi.MessageRegistrationListener;
import org.livetribe.slp.spi.da.DirectoryAgentInfo;
import org.livetribe.slp.spi.da.DirectoryAgentInfoCache;
import org.livetribe.slp.spi.msg.AttributeListExtension;
import org.livetribe.slp.spi.msg.DAAdvert;
import org.livetribe.slp.spi.msg.Message;
import org.livetribe.slp.spi.msg.SAAdvert;
import org.livetribe.slp.spi.msg.SrvDeReg;
import org.livetribe.slp.spi.msg.SrvReg;
import org.livetribe.slp.spi.msg.SrvRply;
import org.livetribe.slp.spi.msg.URLEntry;
import org.livetribe.slp.spi.net.MessageEvent;
import org.livetribe.slp.spi.net.MessageListener;
import org.livetribe.slp.spi.sa.ServiceAgentInfo;
import org.livetribe.slp.spi.ua.StandardUserAgentManager;
import org.livetribe.slp.spi.ua.UserAgentManager;
import org.livetribe.util.ConcurrentListeners;

/**
 * @version $Rev$ $Date$
 */
public class StandardUserAgent extends StandardAgent implements UserAgent
{
    private boolean periodicDiscoveryEnabled = true;
    private long discoveryPeriod = Defaults.UA_DISCOVERY_PERIOD;
    private int discoveryInitialWaitBound = Defaults.UA_DISCOVERY_INITIAL_WAIT_BOUND;
    private UserAgentManager manager;
    private MessageListener multicastMessageListener;
    private MessageListener multicastNotificationListener;
    private final DirectoryAgentInfoCache daInfoCache = new DirectoryAgentInfoCache();
    private ScheduledExecutorService scheduledExecutorService;
    private final ConcurrentListeners listeners = new ConcurrentListeners();

    public void setUserAgentManager(UserAgentManager manager)
    {
        this.manager = manager;
    }

    /**
     * Sets the ScheduledExecutorService used to perform periodic tasks such as discovery
     * of DirectoryAgents.
     * @see #createScheduledExecutorService()
     */
    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService)
    {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * Returns whether this UserAgent should periodically discover DirectoryAgents
     * @see #setPeriodicDiscoveryEnabled(boolean)
     * @see #getDiscoveryPeriod()
     */
    public boolean isPeriodicDiscoveryEnabled()
    {
        return periodicDiscoveryEnabled;
    }

    /**
     * Sets whether this UserAgent should periodically discover DirectoryAgents
     * @see #isPeriodicDiscoveryEnabled()
     * @see #getDiscoveryPeriod()
     */
    public void setPeriodicDiscoveryEnabled(boolean periodicDiscoveryEnabled)
    {
        this.periodicDiscoveryEnabled = periodicDiscoveryEnabled;
    }

    /**
     * Returns the period, in seconds, between discovery of DirectoryAgents
     * @see #setDiscoveryPeriod(long)
     * @see #isPeriodicDiscoveryEnabled()
     */
    public long getDiscoveryPeriod()
    {
        return discoveryPeriod;
    }

    /**
     * Sets the period, in seconds, between discovery of DirectoryAgents
     * @see #getDiscoveryPeriod()
     * @see #isPeriodicDiscoveryEnabled()
     */
    public void setDiscoveryPeriod(long discoveryPeriod)
    {
        this.discoveryPeriod = discoveryPeriod;
    }

    public int getDiscoveryInitialWaitBound()
    {
        return discoveryInitialWaitBound;
    }

    /**
     * Sets the bound (in seconds) to the initial random delay this UserAgent waits
     * before attempting to discover DirectoryAgents
     */
    public void setDiscoveryInitialWaitBound(int discoveryInitialWaitBound)
    {
        this.discoveryInitialWaitBound = discoveryInitialWaitBound;
    }

    protected void doStart() throws IOException
    {
        if (manager == null) manager = createUserAgentManager();
        configureUserAgentManager(manager);
        manager.start();

        multicastMessageListener = new MulticastMessageListener();
        manager.addMessageListener(multicastMessageListener, true);
        multicastNotificationListener = new MulticastNotificationListener();
        manager.addNotificationListener(multicastNotificationListener);

        if (scheduledExecutorService == null) scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        long delay = new Random(System.currentTimeMillis()).nextInt(getDiscoveryInitialWaitBound() + 1) * 1000L;
        if (isPeriodicDiscoveryEnabled())
            scheduledExecutorService.scheduleWithFixedDelay(new DirectoryAgentDiscovery(), delay, getDiscoveryPeriod() * 1000L, TimeUnit.MILLISECONDS);
    }

    protected UserAgentManager createUserAgentManager()
    {
        return new StandardUserAgentManager();
    }

    protected void configureUserAgentManager(UserAgentManager uaManager)
    {
        if (uaManager instanceof StandardUserAgentManager)
        {
            StandardUserAgentManager userAgentManager = (StandardUserAgentManager)uaManager;
            userAgentManager.setPort(getPort());
        }
    }

    /**
     * Creates and returns the default ScheduledExecutorService, configured to use one daemon thread.
     */
    protected ScheduledExecutorService createScheduledExecutorService()
    {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
        {
            public Thread newThread(Runnable runnable)
            {
                Thread thread = new Thread(runnable, "SLP UserAgent Scheduler");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    protected void doStop() throws IOException
    {
        if (scheduledExecutorService != null)
        {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }

        manager.removeNotificationListener(multicastNotificationListener);
        manager.removeMessageListener(multicastMessageListener, true);
        manager.stop();
        manager = null;
    }

    public void addMessageRegistrationListener(MessageRegistrationListener listener)
    {
        listeners.add(listener);
    }

    public void removeMessageRegistrationListener(MessageRegistrationListener listener)
    {
        listeners.remove(listener);
    }

    private void notifyMessageRegistration(SrvReg srvReg)
    {
        listeners.notify("handleSrvReg", srvReg);
    }

    private void notifyMessageDeregistration(SrvDeReg srvDeReg)
    {
        listeners.notify("handleSrvDeReg", srvDeReg);
    }

    /**
     * Returns a list of {@link ServiceInfo} that are available on the network, and that match the given arguments.
     * @param serviceType The ServiceType of the services to find
     * @param scopes The scopes of the services to find
     * @param filter An LDAPv3 filter expression for the attributes of the services to find
     * @param language The language of the services to find
     */
    public List findServices(ServiceType serviceType, Scopes scopes, String filter, String language) throws IOException, ServiceLocationException
    {
        List result = new ArrayList();

        List das = findDirectoryAgents(scopes, null);
        if (!das.isEmpty())
        {
            for (int i = 0; i < das.size(); ++i)
            {
                DirectoryAgentInfo info = (DirectoryAgentInfo)das.get(i);
                InetAddress address = InetAddress.getByName(info.getHost());
                SrvRply srvRply = manager.tcpSrvRqst(address, serviceType, scopes, filter, language);
                result.addAll(srvRplyToServiceInfos(srvRply, scopes, language));
            }
        }
        else
        {
            List serviceAgents = findServiceAgents(scopes, null);
            List tcpServiceAgents = new ArrayList();
            for (Iterator iterator = serviceAgents.iterator(); iterator.hasNext();)
            {
                ServiceAgentInfo serviceAgentInfo = (ServiceAgentInfo)iterator.next();
                if (serviceAgentInfo.isTCPListening())
                {
                    tcpServiceAgents.add(serviceAgentInfo);
                    iterator.remove();
                }
            }
            // Query via TCP first
            for (int i = 0; i < tcpServiceAgents.size(); ++i)
            {
                ServiceAgentInfo info = (ServiceAgentInfo)serviceAgents.get(i);
                InetAddress address = InetAddress.getByName(info.getHost());
                SrvRply srvRply = manager.tcpSrvRqst(address, serviceType, scopes, filter, language);
                result.addAll(srvRplyToServiceInfos(srvRply, scopes, language));
            }

            // Query via multicast
            if (!serviceAgents.isEmpty())
            {
                SrvRply[] srvRplys = manager.multicastSrvRqst(serviceType, scopes, filter, language, -1);
                for (int i = 0; i < srvRplys.length; ++i)
                {
                    SrvRply srvRply = srvRplys[i];
                    result.addAll(srvRplyToServiceInfos(srvRply, scopes, language));
                }
            }
        }
        return result;
    }

    private List srvRplyToServiceInfos(SrvRply srvRply, Scopes scopes, String language)
    {
        List result = new ArrayList();
        List serviceAttributes = AttributeListExtension.findAll(srvRply.getExtensions());
        List entries = srvRply.getURLEntries();
        for (int j = 0; j < entries.size(); ++j)
        {
            URLEntry entry = (URLEntry)entries.get(j);
            ServiceURL serviceURL = entry.toServiceURL();
            Attributes attributes = null;
            for (int k = 0; k < serviceAttributes.size(); ++k)
            {
                AttributeListExtension attrListExt = (AttributeListExtension)serviceAttributes.get(k);
                if (attrListExt.getURL().equals(serviceURL.getURL()))
                {
                    attributes = attrListExt.getAttributes();
                    break;
                }
            }
            ServiceInfo service = new ServiceInfo(serviceURL, scopes, attributes, language);
            result.add(service);
        }
        return result;
    }

    public List findDirectoryAgents(Scopes scopes, String filter) throws IOException, ServiceLocationException
    {
        List das = getCachedDirectoryAgents(scopes, filter);
        if (das.isEmpty())
        {
            das = discoverDirectoryAgents(scopes, filter);
            cacheDirectoryAgents(das);
        }
        return das;
    }

    protected List getCachedDirectoryAgents(Scopes scopes, String filter)
    {
        // TODO: filter cached DAs upon the given filter
        return daInfoCache.getByScopes(scopes);
    }

    private void cacheDirectoryAgents(List infos)
    {
        daInfoCache.addAll(infos);
    }

    private boolean cacheDirectoryAgent(DirectoryAgentInfo info)
    {
        return daInfoCache.add(info);
    }

    private boolean uncacheDirectoryAgent(DirectoryAgentInfo info)
    {
        return daInfoCache.remove(info);
    }

    protected List discoverDirectoryAgents(Scopes scopes, String filter) throws IOException
    {
        List result = new ArrayList();
        DAAdvert[] daAdverts = manager.multicastDASrvRqst(scopes, filter, null, -1);
        for (int i = 0; i < daAdverts.length; ++i)
        {
            DAAdvert daAdvert = daAdverts[i];
            DirectoryAgentInfo info = DirectoryAgentInfo.from(daAdvert);
            result.add(info);
        }
        if (logger.isLoggable(Level.FINE)) logger.fine("UserAgent " + this + " discovered DAs: " + result);
        return result;
    }

    public List findServiceAgents(Scopes scopes, String filter) throws IOException, ServiceLocationException
    {
        return discoverServiceAgents(scopes, filter);
    }

    private List discoverServiceAgents(Scopes scopes, String filter) throws IOException
    {
        List result = new ArrayList();
        SAAdvert[] saAdverts = manager.multicastSASrvRqst(scopes, filter, null, -1);
        for (int i = 0; i < saAdverts.length; ++i)
        {
            SAAdvert saAdvert = saAdverts[i];
            ServiceAgentInfo info = ServiceAgentInfo.from(saAdvert);
            result.add(info);
        }
        if (logger.isLoggable(Level.FINE)) logger.fine("UserAgent " + this + " discovered SAs: " + result);
        return result;
    }

    protected void handleMulticastDAAdvert(DAAdvert message, InetSocketAddress address)
    {
        if (!getScopes().weakMatch(message.getScopes()))
        {
            if (logger.isLoggable(Level.FINE))
                logger.fine("UserAgent " + this + " dropping message " + message + ": no scopes match among UA scopes " + getScopes() + " and message scopes " + message.getScopes());
            return;
        }

        DirectoryAgentInfo info = DirectoryAgentInfo.from(message);
        if (message.getBootTime() == 0L)
        {
            boolean removed = uncacheDirectoryAgent(info);
            // TODO
//            if (removed) notifyListeners();
        }
        else
        {
            boolean added = cacheDirectoryAgent(info);
            // TODO
//            if (added) notifyListeners();
        }
    }

    protected void handleMulticastSrvReg(SrvReg message)
    {
        notifyMessageRegistration(message);
    }

    protected void handleMulticastSrvDeReg(SrvDeReg message)
    {
        notifyMessageDeregistration(message);
    }

    /**
     * UserAgents listen for multicast UDP messages that may arrive.
     * They are interested in:
     * <ul>
     * <li>DAAdverts, from DAs that boot or shutdown</li>
     * </ul>
     */
    private class MulticastMessageListener implements MessageListener
    {
        public void handle(MessageEvent event)
        {
            InetSocketAddress address = event.getSocketAddress();
            try
            {
                Message message = Message.deserialize(event.getMessageBytes());
                if (logger.isLoggable(Level.FINEST))
                    logger.finest("UserAgent multicast message listener received message " + message);

                if (!message.isMulticast())
                {
                    if (logger.isLoggable(Level.FINE))
                        logger.fine("UserAgent " + this + " dropping message " + message + ": expected multicast flag set");
                    return;
                }

                switch (message.getMessageType())
                {
                    case Message.DA_ADVERT_TYPE:
                        handleMulticastDAAdvert((DAAdvert)message, address);
                        break;
                    default:
                        if (logger.isLoggable(Level.FINE))
                            logger.fine("UserAgent " + this + " dropping multicast message " + message + ": not handled by UserAgents");
                        break;
                }
            }
            catch (ServiceLocationException x)
            {
                if (logger.isLoggable(Level.FINE))
                    logger.log(Level.FINE, "UserAgent " + this + " received bad multicast message from: " + address + ", ignoring", x);
            }
        }
    }

    private class MulticastNotificationListener implements MessageListener
    {
        public void handle(MessageEvent event)
        {
            InetSocketAddress address = event.getSocketAddress();
            try
            {
                Message message = Message.deserialize(event.getMessageBytes());
                if (logger.isLoggable(Level.FINEST))
                    logger.finest("UserAgent " + this + " multicast notification listener received message " + message);

                if (!message.isMulticast())
                {
                    if (logger.isLoggable(Level.FINE))
                        logger.fine("UserAgent " + this + " dropping notification " + message + ": expected multicast flag set");
                    return;
                }

                switch (message.getMessageType())
                {
                    case Message.SRV_REG_TYPE:
                        handleMulticastSrvReg((SrvReg)message);
                        break;
                    case Message.SRV_DEREG_TYPE:
                        handleMulticastSrvDeReg((SrvDeReg)message);
                        break;
                    default:
                        if (logger.isLoggable(Level.FINE))
                            logger.fine("UserAgent " + this + " dropping multicast notification " + message + ": not handled by UserAgents");
                        break;
                }
            }
            catch (ServiceLocationException x)
            {
                if (logger.isLoggable(Level.FINE))
                    logger.log(Level.FINE, "UserAgent " + this + " received bad multicast notification from: " + address + ", ignoring", x);
            }
        }
    }

    private class DirectoryAgentDiscovery implements Runnable
    {
        public void run()
        {
            if (logger.isLoggable(Level.FINE)) logger.fine("UserAgent " + this + " executing periodic discovery of DAs");
            try
            {
                List das = discoverDirectoryAgents(getScopes(), null);
                cacheDirectoryAgents(das);
            }
            catch (IOException x)
            {
                if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Could not discover DAs", x);
            }
        }
    }
}
