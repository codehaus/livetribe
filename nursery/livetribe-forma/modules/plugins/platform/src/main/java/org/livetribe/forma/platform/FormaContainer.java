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
package org.livetribe.forma.platform;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.livetribe.ioc.IOCContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @version $Rev$ $Date$
 */
public class FormaContainer implements ApplicationContextAware, IOCContainer
{
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    public Set<String> getServiceNames()
    {
        return new HashSet<String>(Arrays.asList(applicationContext.getBeanDefinitionNames()));
    }

    public Class<?> getServiceType(String serviceName)
    {
        return applicationContext.getType(serviceName);
    }

    public Object getService(String serviceName)
    {
        return applicationContext.getBean(serviceName);
    }
}
