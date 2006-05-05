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
package org.livetribe.forma.boot;

import java.util.Set;

import org.livetribe.ioc.Registry;
import org.livetribe.forma.ManagerRegistry;

/**
 * Bridge class between Forma's ManagerRegistry and IOC Registry,
 * so that IOC's Container will be able to inject services contained
 * in ManagerRegistry.
 * @version $Rev$ $Date$
 */
public class DefaultContainerManager implements Registry
{
    private final ManagerRegistry managerRegistry;

    public DefaultContainerManager(ManagerRegistry managerRegistry)
    {
        this.managerRegistry = managerRegistry;
    }

    public Set<String> getServiceNames()
    {
        return managerRegistry.getManagerIds();
    }

    public Class<?> getServiceType(String managerId)
    {
        return managerRegistry.getManagerType(managerId);
    }

    public Object getService(String managerId)
    {
        return managerRegistry.get(managerId, getServiceType(managerId));
    }
}
