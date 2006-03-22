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
package org.livetribe.arm.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.ArmRuntimeException;
import org.livetribe.arm.LTAbstractObjectBase;

/**
 * @version $Revision: $ $Date: $
 */
public class LTProxyFactory
{
    public static Object newProxyInstance(LTAbstractObjectBase object)
    {
        Set interfaces = collectInterfaces(object.getClass());
        return Proxy.newProxyInstance(LTProxyFactory.class.getClassLoader(), (Class[]) interfaces.toArray(new Class[interfaces.size()]), new BaseHandler(object));
    }

    static class BaseHandler implements InvocationHandler
    {
        private final LTAbstractObjectBase base;

        BaseHandler(LTAbstractObjectBase base)
        {
            this.base = base;
        }

        public Object invoke(Object object, Method method, Object[] objects) throws Throwable
        {
            Object result = null;


            if (!method.getDeclaringClass().equals(ArmInterface.class))
            {
                base.setErrorCode(0);
            }

            try
            {
                result = method.invoke(base, objects);
            }
            catch (InvocationTargetException ite)
            {
                if (ite.getTargetException() instanceof ArmRuntimeException)
                {
                    base.setErrorCode(((ArmRuntimeException) ite.getTargetException()).getErrorCode());

                    ArmErrorCallback callback = base.getFactory().getCallback();
                    if (callback != null)
                        callback.errorCodeSet((ArmInterface) object, method.getDeclaringClass().getName(), method.getName());
                }
                else
                {
                    throw ite;
                }
            }
            return result;
        }
    }

    static Set collectInterfaces(Class object)
    {
        Set interfaces = new HashSet();

        if (Object.class.equals(object))
        {
            return Collections.EMPTY_SET;
        }
        else
        {
            interfaces.addAll(Arrays.asList(object.getInterfaces()));
            interfaces.addAll(collectInterfaces(object.getSuperclass()));
        }

        return interfaces;
    }
}
