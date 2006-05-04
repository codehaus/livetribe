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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class ContainerTest extends TestCase
{
    private Container ioc;

    @Override
    protected void setUp() throws Exception
    {
        TestRegistry container = new TestRegistry();
        container.addService("srv", Service.class, new Service());
        ioc = new DefaultContainer(container);
    }

    public void testNonInjectAnnotated()
    {
        NonInjectAnnotated target = new NonInjectAnnotated();
        ioc.resolve(target);
        assertNull(target.getService());
    }

    public void testInjectAnnotated()
    {
        InjectAnnotated target = new InjectAnnotated();
        ioc.resolve(target);
        assertNotNull(target.getService());
    }

    public void testSubClassAddsInjectAnnotation()
    {
        SubClassInjectAnnotated target = new SubClassInjectAnnotated();
        ioc.resolve(target);
        assertNotNull(target.getService());
    }

    public void testInjectAnnotatedField()
    {
        InjectAnnotatedField target = new InjectAnnotatedField();
        ioc.resolve(target);
        assertNotNull(target.getService());
    }

    public void testOverrideAnnotated()
    {
        OverrideInjectAnnotated target = new OverrideInjectAnnotated();
        ioc.resolve(target);
        assertNotNull(target.getService());
        assertEquals(1, target.count);
    }

    public void testPostConstruct()
    {
        PostConstructAnnotated target = new PostConstructAnnotated();
        ioc.resolve(target);
        assertTrue(target.postConstructed());
    }

    private static class TestRegistry implements Registry
    {
        private final Map<String, ServiceInfo> services = new HashMap<String, ServiceInfo>();

        public void addService(String name, Class<?> type, Object instance)
        {
            services.put(name, new ServiceInfo(type, instance));
        }

        public Set<String> getServiceNames()
        {
            return services.keySet();
        }

        public Class<?> getServiceType(String serviceName)
        {
            ServiceInfo info = services.get(serviceName);
            return info == null ? null : info.type;
        }

        public Object getService(String serviceName)
        {
            ServiceInfo info = services.get(serviceName);
            return info == null ? null : info.instance;
        }

        private static class ServiceInfo
        {
            private final Class<?> type;
            private final Object instance;

            public ServiceInfo(Class<?> type, Object instance)
            {
                this.type = type;
                this.instance = instance;
            }
        }
    }

    private static interface IService
    {
    }

    private static class Service implements IService
    {
    }

    private static class NonInjectAnnotated
    {
        private Service service;

        public Service getService()
        {
            return service;
        }

        public void setService(Service service)
        {
            this.service = service;
        }
    }

    private static class InjectAnnotated
    {
        private Service service;

        public Service getService()
        {
            return service;
        }

        @Inject
        public void setService(Service service)
        {
            this.service = service;
        }
    }

    private static class SubClassInjectAnnotated extends NonInjectAnnotated
    {
        @Override
        @Inject
        public void setService(Service service)
        {
            super.setService(service);
        }
    }

    private static class OverrideInjectAnnotated extends InjectAnnotated
    {
        private int count = 0;

        @Override
        @Inject
        public void setService(Service service)
        {
            ++count;
            super.setService(service);
        }
    }

    private static class PostConstructAnnotated
    {
        private boolean postConstructed;

        @PostConstruct
        private void postConstruct()
        {
            postConstructed = true;
        }

        public boolean postConstructed()
        {
            return postConstructed;
        }
    }

    private static class InjectAnnotatedField
    {
        @Inject
        private Service service;

        public Service getService()
        {
            return service;
        }
    }
}
