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

import org.aopalliance.intercept.MethodInvocation;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.LTAbstractFactoryBase;
import org.livetribe.arm.LTObject;
import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class ObjectAdvice extends AbstractAdvice
{
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        ArmInterface target = (ArmInterface) invocation.getThis();
        Object rval = null;
        try
        {
            StaticArmAPIMonitor.begin(target);
            if (invocation.getMethod().getDeclaringClass() != ArmInterface.class) target.setErrorCode(GeneralErrorCodes.SUCCESS);

            rval = invocation.proceed();

            if (((LTObject)target).isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
        }
        finally
        {
            if (invocation.getMethod().getDeclaringClass() != ArmInterface.class && isError())
            {
                target.setErrorCode(getErrorCode());

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
}
