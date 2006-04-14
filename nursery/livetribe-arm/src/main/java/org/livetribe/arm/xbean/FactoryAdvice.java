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
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;
import org.springframework.aop.framework.Advised;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.LTAbstractFactoryBase;
import org.livetribe.arm.LTAbstractObject;
import org.livetribe.arm.LTObject;
import org.livetribe.arm.util.ArmAPIMonitor;
import org.livetribe.arm.util.StaticArmAPIMonitor;
import org.livetribe.util.WeakHashSet;


/**
 * @version $Revision: $ $Date: $
 */
public class FactoryAdvice extends AbstractAdvice
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
        ArmInterface target = (ArmInterface) invocation.getThis();
        Object rval = null;
        try
        {
            StaticArmAPIMonitor.begin(target);
            if (invocation.getMethod().getDeclaringClass() != ArmInterface.class)
            {
                target.setErrorCode(GeneralErrorCodes.SUCCESS);
            }

            rval = invocation.proceed();

            if (rval instanceof ArmInterface)
            {
                Set interfaces = collectInterfaces(rval.getClass());
                try
                {
                    proxyFactory.addInterfaces(interfaces);
                    proxyFactory.setTarget(rval);

                    rval = proxyFactory.getProxy();
                }
                finally
                {
                    proxyFactory.removeInterfaces(interfaces);
                }
            }
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
        }
        finally
        {
            if (invocation.getMethod().getDeclaringClass() != ArmInterface.class && isError())
            {
                int code = getErrorCode();
                Object result = null;

                if (rval instanceof LTObject)
                {
                    result = ((Advised) rval).getTargetSource().getTarget();
                    ((LTAbstractObject) result).setBad(true);
                    ((LTAbstractObject) result).setErrorCode(code);
                }


                target.setErrorCode(code);

                ArmErrorCallback callback = LTAbstractFactoryBase.getCallback();
                if (callback != null)
                {
                    try
                    {
                        callback.errorCodeSet(target,
                                              invocation.getMethod().getDeclaringClass().getName(),
                                              invocation.getMethod().getName());
                    }
                    catch (Throwable ignore)
                    {
                        // We're notifying the client, they should be nice to us...
                    }
                }
            }

            StaticArmAPIMonitor.end();
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
