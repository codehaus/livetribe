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
package org.livetribe.ioc;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version $Revision$ $Date$
 */
public class StandardRegistry implements Registry
{
    private final Map<String, ServiceEntry> services = new ConcurrentHashMap<String, ServiceEntry>();

    public Object putService(String serviceName, Class<?> serviceClass, Object service)
    {
        ServiceEntry entry = new ServiceEntry(serviceClass, service);
        ServiceEntry existing = services.put(serviceName, entry);
        return existing == null ? null : existing.service;
    }

    public Object removeService(String serviceName)
    {
        ServiceEntry existing = services.remove(serviceName);
        return existing == null ? null : existing.service;
    }

    public Set<String> getServiceNames()
    {
        return services.keySet();
    }

    public Class<?> getServiceType(String serviceName)
    {
        ServiceEntry entry = services.get(serviceName);
        return entry == null ? null : entry.serviceClass;
    }

    public Object getService(String serviceName)
    {
        ServiceEntry entry = services.get(serviceName);
        return entry == null ? null : entry.service;
    }

    private static class ServiceEntry
    {
        private final Class<?> serviceClass;
        private final Object service;

        private ServiceEntry(Class<?> serviceClass, Object service)
        {
            this.serviceClass = serviceClass;
            this.service = service;
        }
    }
}
