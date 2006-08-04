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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @version $Rev$ $Date$
 */
public class StandardContainer implements Container
{
    private final Registry container;

    public StandardContainer(Registry container)
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
        injectFields(target);
        injectMethods(target);
    }

    protected void postConstruct(Object target)
    {
        Method postConstruct = findPostConstructMethod(target);
        if (postConstruct != null) invokeVoid(postConstruct, target);
    }

    protected void injectFields(Object target)
    {
        List<Field> injected = findInjectedFields(target);
        for (Field field : injected)
        {
            Inject inject = field.getAnnotation(Inject.class);
            Class<?> injectedClass = field.getType();
            Object service = findService(inject, injectedClass, target);
            setField(field, target, service);
        }
    }

    protected void injectMethods(Object target)
    {
        List<Method> injected = findInjectedMethods(target);
        for (Method method : injected)
        {
            Inject inject = method.getAnnotation(Inject.class);
            Class<?> injectedClass = method.getParameterTypes()[0];
            Object service = findService(inject, injectedClass, target);
            invokeSetter(method, target, service);
        }
    }

    private List<Field> findInjectedFields(Object target)
    {
        List<Field> result = new ArrayList<Field>();
        Class<?> cls = target.getClass();
        while (Object.class != cls)
        {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields)
            {
                if (field.getAnnotation(Inject.class) != null)
                {
                    result.add(field);
                }
            }
            cls = cls.getSuperclass();
        }
        return result;
    }

    private List<Method> findInjectedMethods(Object target)
    {
        List<Method> result = new ArrayList<Method>();
        Class<?> cls = target.getClass();
        Method[] methods = cls.getMethods();
        for (Method method : methods)
        {
            if (method.getParameterTypes().length == 1 && Void.TYPE.equals(method.getReturnType()))
            {
                if (method.getAnnotation(Inject.class) != null)
                {
                    result.add(method);
                }
            }
        }
        return result;
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

    private Object findService(Inject inject, Class<?> injectedClass, Object target)
    {
        String injectedServiceName = inject.serviceName();
        if (injectedServiceName.length() == 0)
        {
            // Figure out the service that matches
            Set<String> serviceNames = container.getServiceNames();
            for (String serviceName : serviceNames)
            {
                if (injectedClass.isAssignableFrom(container.getServiceType(serviceName)))
                {
                    Object service = container.getService(serviceName);
                    assert injectedClass.isInstance(service);
                    return service;
                }
            }
            throw new InjectionException("Could not inject service of type " + injectedClass.getName() + " in " + target + ": no services of that type exist");
        }
        else
        {
            Object service = container.getService(injectedServiceName);
            if (!injectedClass.isInstance(service)) throw new InjectionException("Could not inject service named " + injectedServiceName + " in " + target + ": it is not an instance of " + injectedClass.getName());
            return service;
        }
    }

    private void setField(Field field, Object target, Object service)
    {
        try
        {
            field.setAccessible(true);
            field.set(target, service);
        }
        catch (IllegalAccessException x)
        {
            throw new InjectionException(x);
        }
    }

    private void invokeSetter(Method method, Object target, Object service)
    {
        try
        {
            method.setAccessible(true);
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
