/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm.ra.hibernate;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.Referenceable;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;
import javax.naming.Reference;
import javax.naming.NamingException;
import java.util.HashMap;

import edu.emory.mathcs.backport.java.util.concurrent.CountDownLatch;


/**
 * @version $Revision$ $Date$
 */
public class HibernateResourceAdapter implements ResourceAdapter, Referenceable
{
    private final HashMap endpointWorkers = new HashMap();
    private final HibernateConnectionRequestInfo info = new HibernateConnectionRequestInfo();
    private Reference reference;
    private BootstrapContext bootstrapContext;
    private WorkManager workManager;
    private CountDownLatch latch;


    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException
    {
        this.bootstrapContext = bootstrapContext;
        workManager = bootstrapContext.getWorkManager();
    }

    public void stop()
    {
        if (endpointWorkers.size() > 0)
        {
            latch = new CountDownLatch(endpointWorkers.size());

            while (endpointWorkers.size() > 0)
            {
                EndpointActivationKey key = (EndpointActivationKey) endpointWorkers.keySet().iterator().next();
                endpointDeactivation(key.getMessageEndpointFactory(), key.getActivationSpec());
            }

            try
            {
                latch.await();
            }
            catch (InterruptedException doNothing)
            {
            }
        }
        bootstrapContext = null;
    }

    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws ResourceException
    {
        // spec section 5.3.3
        if (activationSpec.getResourceAdapter() != this)
            throw new ResourceException("Activation spec not initialized with this ResourceAdapter instance");

        if (activationSpec.getClass().equals(HibernateActivationSpec.class))
        {
            EndpointActivationKey key = new EndpointActivationKey(messageEndpointFactory, activationSpec);

            if (endpointWorkers.containsKey(key)) throw new IllegalStateException("Endpoint previously activated");

            EndpointWorker worker = new EndpointWorker(this, key);

            endpointWorkers.put(key, worker);

            workManager.scheduleWork(worker, WorkManager.INDEFINITE, null, null);
        }
        else
        {
            throw new NotSupportedException("That type of ActicationSpec not supported: " + activationSpec.getClass());
        }
    }

    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec)
    {
        if (activationSpec.getClass().equals(ActivationSpec.class))
        {
            EndpointActivationKey key = new EndpointActivationKey(messageEndpointFactory, activationSpec);
            EndpointWorker worker = (EndpointWorker) endpointWorkers.remove(key);

            if (worker == null) return;

            worker.release();
        }
    }

    void countDown()
    {
        if (latch != null)
        {
            latch.countDown();
        }
    }

    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException
    {
        return new XAResource[]{};
    }

    HibernateConnectionRequestInfo getInfo()
    {
        return info;
    }

    public String getDialect()
    {
        return info.getDialect();
    }

    public void setDialect(String dialect)
    {
        info.setDialect(dialect);
    }

    public String getDriverClass()
    {
        return info.getDriverClass();
    }

    public void setDriverClass(String driverClass)
    {
        info.setDriverClass(driverClass);
    }

    public String getUrl()
    {
        return info.getUrl();
    }

    public void setUrl(String url)
    {
        info.setUrl(url);
    }

    public String getUsername()
    {
        return info.getUsername();
    }

    public void setUsername(String username)
    {
        info.setUsername(username);
    }

    public String getPassword()
    {
        return info.getPassword();
    }

    public void setPassword(String password)
    {
        info.setPassword(password);
    }

    public Integer getPoolSize()
    {
        return info.getPoolSize();
    }

    public void setPoolSize(Integer poolSize)
    {
        info.setPoolSize(poolSize);
    }

    public String getCacheProviderClass()
    {
        return info.getCacheProviderClass();
    }

    public void setCacheProviderClass(String cacheProviderClass)
    {
        info.setCacheProviderClass(cacheProviderClass);
    }

    public void setReference(Reference reference)
    {
        this.reference = reference;
    }

    public Reference getReference() throws NamingException
    {
        if (reference == null) throw new NamingException("Reference is null");
        return reference;
    }

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof HibernateResourceAdapter)) return false;

        final HibernateResourceAdapter other = (HibernateResourceAdapter) obj;

        return info.equals(other.info);
    }

    public int hashCode()
    {
        return info.hashCode();
    }

}
