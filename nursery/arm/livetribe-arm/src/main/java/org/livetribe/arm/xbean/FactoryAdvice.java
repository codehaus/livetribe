/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm.xbean;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.util.StaticArmAPIMonitor;
import org.livetribe.util.WeakHashSet;


/**
 * @version $Revision: $ $Date: $
 */
public class FactoryAdvice implements MethodInterceptor
{
    private ProxyFactory proxyFactory;

    public ProxyFactory getProxyFactory()
    {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory)
    {
        this.proxyFactory = proxyFactory;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Object rval = null;
        try
        {
            rval = invocation.proceed();

            if (rval instanceof ArmInterface)
            {
                Set interfaces = collectInterfaces(rval.getClass());
//                synchronized (proxyFactory)
//                {
//                    try
//                    {
                ProxyFactory pf = new ProxyFactory();

                pf.setAdvisors(proxyFactory.getAdvisors());
                pf.addInterfaces(interfaces);
                pf.setTarget(rval);

                rval = pf.getProxy();
//                    }
//                    finally
//                    {
//                        proxyFactory.removeInterfaces(interfaces);
//                    }
//                }
            }
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
        }
        return rval;
    }

    private final static Map interfaceCache = Collections.synchronizedMap(new WeakHashMap());

    private Set collectInterfaces(Class clazz)
    {
        Set result = (Set) interfaceCache.get(clazz);

        if (result == null)
        {
            result = new WeakHashSet();

            if (clazz != Object.class)
            {
                Class[] interfaces = clazz.getInterfaces();
                for (int i = 0; i < interfaces.length; i++)
                {
                    result.add(interfaces[i]);
                }

                result.addAll(collectInterfaces(clazz.getSuperclass()));
            }

            interfaceCache.put(clazz, result);
        }

        return result;
    }
}
