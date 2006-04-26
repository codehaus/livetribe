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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @version $Rev$ $Date$
 */
public class DefaultIOCService implements IOCService
{
    private IOCContainer container;

    public void setIOCContainer(IOCContainer container)
    {
        this.container = container;
    }

    public void resolve(Object target)
    {
        if (target == null) return;
        inject(target);
        postConstruct(target);
    }

    protected void inject(Object target)
    {
        List<Method> injected = findInjectMethods(target);
        for (Method method : injected)
        {
            Inject inject = method.getAnnotation(Inject.class);
            Class injectedClass = method.getParameterTypes()[0];

            String injectedServiceName = inject.serviceName();
            if (injectedServiceName.length() == 0)
            {
                // Figure out the service that matches
                Set<String> serviceNames = container.getServiceNames();
                boolean found = false;
                for (String serviceName : serviceNames)
                {
                    if (injectedClass.isAssignableFrom(container.getServiceType(serviceName)))
                    {
                        Object service = container.getService(serviceName);
                        invokeSetter(method, target, service);
                        found = true;
                        break;
                    }
                }
                if (!found)
                    throw new InjectionException("No suitable service to inject in '" + method + "' of " + target.getClass());
            }
            else
            {
                Object service = container.getService(injectedServiceName);

                if (service == null)
                    throw new InjectionException("Could not inject unknown service '" + injectedServiceName + "' into " + target);

                if (injectedClass.isInstance(service))
                {
                    invokeSetter(method, target, service);
                }
            }
        }
    }

    protected void postConstruct(Object target)
    {
        Method postConstruct = findPostConstructMethod(target);
        if (postConstruct != null) invokeVoid(postConstruct, target);
    }

    private List<Method> findInjectMethods(Object target)
    {
        List<Method> injected = new ArrayList<Method>();
        Class<?> targetClass = target.getClass();
        Method[] targetMethods = targetClass.getMethods();
        for (Method method : targetMethods)
        {
            if (method.getParameterTypes().length == 1 && Void.TYPE.equals(method.getReturnType()))
            {
                if (method.getAnnotation(Inject.class) != null)
                {
                    injected.add(method);
                }
            }
        }
        return injected;
    }

    private Method findPostConstructMethod(Object target)
    {
        Class<?> cls = target.getClass();
        while (Object.class != cls)
        {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getParameterTypes().length == 0 && Void.TYPE.equals(method.getReturnType()))
                {
                    if (method.getAnnotation(PostConstruct.class) != null)
                    {
                        return method;
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private void invokeSetter(Method method, Object target, Object service)
    {
        try
        {
            method.invoke(target, service);
        }
        catch (IllegalAccessException x)
        {
            throw new InjectionException(x);
        }
        catch (InvocationTargetException x)
        {
            throw new InjectionException(x.getCause());
        }
    }

    private void invokeVoid(Method method, Object target)
    {
        try
        {
            method.setAccessible(true);
            method.invoke(target);
        }
        catch (IllegalAccessException x)
        {
            throw new InjectionException(x);
        }
        catch (InvocationTargetException x)
        {
            throw new InjectionException(x.getCause());
        }
    }
}
