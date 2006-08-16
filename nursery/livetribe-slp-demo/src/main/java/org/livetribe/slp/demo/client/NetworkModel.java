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
package org.livetribe.slp.demo.client;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import org.livetribe.slp.Scopes;
import org.livetribe.slp.ServiceType;
import org.livetribe.slp.api.MatchingServiceInfoCache;
import org.livetribe.slp.api.ServiceRegistrationEvent;
import org.livetribe.slp.api.ServiceRegistrationListener;
import org.livetribe.slp.api.ua.StandardUserAgent;

/**
 * @version $Revision$ $Date$
 */
public class NetworkModel extends Observable implements ServiceRegistrationListener
{
    private StandardUserAgent userAgent;
    private MatchingServiceInfoCache serviceInfos;
    private ScheduledExecutorService scheduler;

    public void start() throws Exception
    {
        userAgent = new StandardUserAgent();
        userAgent.setPort(1427);
        userAgent.start();

        ServiceType serviceType = new ServiceType("service:jmx");
        Scopes scopes = Scopes.DEFAULT;
        String filter = null;
        String language = Locale.ENGLISH.getLanguage();
        serviceInfos = new MatchingServiceInfoCache(serviceType, scopes, filter, language);
        userAgent.addMessageRegistrationListener(serviceInfos);
        serviceInfos.addServiceRegistrationListener(this);

        List found = userAgent.findServices(serviceType, scopes, filter, language);
        serviceInfos.putAll(found);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(new Expirer(), 0, 1, TimeUnit.SECONDS);
    }

    public void stop() throws Exception
    {
        scheduler.shutdown();
        userAgent.stop();
        serviceInfos.clear();
    }

    public void serviceRegistered(ServiceRegistrationEvent event)
    {
        if (event.getPreviousServiceInfo() == null)
            update(event);
    }

    public void serviceUpdated(ServiceRegistrationEvent event)
    {
        update(event);
    }

    public void serviceDeregistered(ServiceRegistrationEvent event)
    {
        update(event);
    }

    public void serviceExpired(ServiceRegistrationEvent event)
    {
        update(event);
    }

    private void update(final Object event)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                setChanged();
                notifyObservers(event);
            }
        });
    }

    public Collection getServiceInfos()
    {
        return serviceInfos.getServiceInfos();
    }

    private class Expirer implements Runnable
    {
        public void run()
        {
            serviceInfos.purge();
        }
    }
}
