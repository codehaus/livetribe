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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.livetribe.forma.ManagerRegistry;

/**
 * @version $Rev$ $Date$
 */
public class DefaultManagerRegistry implements ManagerRegistry
{
    private Map<String, ManagerInfo<?>> managers = new HashMap<String, ManagerInfo<?>>();

    public <T> void put(String managerId, Class<? super T> managerType, T manager)
    {
        if (managers.containsKey(managerId)) throw new IllegalArgumentException("Manager id '" + "' is already registered");
        ManagerInfo<T> info = new ManagerInfo<T>(managerId, managerType, manager);
        managers.put(managerId, info);
    }

    public <T> T get(String managerId, Class<T> managerType)
    {
        ManagerInfo<?> info = managers.get(managerId);
        if (info != null)
        {
            if (managerType.isAssignableFrom(info.managerType))
            {
                return managerType.cast(info.manager);
            }
        }
        return null;
    }

    public void remove(String managerId)
    {
        managers.remove(managerId);
    }

    public Set<String> getManagerIds()
    {
        return managers.keySet();
    }

    public Class<?> getManagerType(String managerId)
    {
        ManagerInfo<?> info = managers.get(managerId);
        if (info != null) return info.managerType;
        return null;
    }

    private static class ManagerInfo<T>
    {
        private final String managerId;
        private final Class<? super T> managerType;
        private final T manager;

        private ManagerInfo(String managerId, Class<? super T> managerType, T manager)
        {
            this.managerId = managerId;
            this.managerType = managerType;
            this.manager = manager;
        }

        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final ManagerInfo that = (ManagerInfo)obj;
            return managerId.equals(that.managerId);
        }

        public int hashCode()
        {
            return managerId.hashCode();
        }
    }
}
