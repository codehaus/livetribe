/**
 *
 * Copyright 2009 (C) The original author or authors
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
package org.livetribe.slp.osgi;

import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.sa.ServiceAgent;


/**
 * @version $Revision$ $Date$
 */
public class ByServiceInfoServiceTracker extends ServiceTracker
{
    public final static String SLP_SERVICE_TYPE = "slp.service.type";
    public final static String SLP_URL = "slp.url";
    public final static String SLP_URL_LIFETIME = "slp.url.lifetime";
    public final static String SLP_LANGUAGE = "slp.language";
    public final static String SLP_SCOPES = "slp.scopes";
    private final static String CLASS_NAME = ByServiceInfoServiceTracker.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ServiceAgent serviceAgent;

    public ByServiceInfoServiceTracker(BundleContext context, ServiceAgent serviceAgent)
    {
        super(context, ServiceInfo.class.getName(), null);

        if (serviceAgent == null) throw new IllegalArgumentException("Service agent cannot be null");

        this.serviceAgent = serviceAgent;
    }

    @Override
    public Object addingService(ServiceReference reference)
    {
        LOGGER.entering(CLASS_NAME, "addingService", reference);

        ServiceInfo serviceInfo = (ServiceInfo) context.getService(reference);

        serviceAgent.register(serviceInfo);

        LOGGER.exiting(CLASS_NAME, "addingService", serviceInfo);

        return serviceInfo;
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service)
    {
        LOGGER.entering(CLASS_NAME, "modifiedService", new Object[]{reference, service});

        ServiceInfo serviceInfo = (ServiceInfo) service;
        serviceAgent.deregister(serviceInfo.getServiceURL(), serviceInfo.getLanguage());

        serviceAgent.register(serviceInfo);

        LOGGER.exiting(CLASS_NAME, "modifiedService");
    }

    @Override
    public void removedService(ServiceReference reference, Object service)
    {
        LOGGER.entering(CLASS_NAME, "removedService", new Object[]{reference, service});

        context.ungetService(reference);

        ServiceInfo serviceInfo = (ServiceInfo) service;

        serviceAgent.deregister(serviceInfo.getServiceURL(), serviceInfo.getLanguage());

        LOGGER.exiting(CLASS_NAME, "removedService");
    }

}