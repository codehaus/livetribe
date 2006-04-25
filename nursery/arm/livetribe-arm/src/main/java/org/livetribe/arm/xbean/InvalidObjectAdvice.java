package org.livetribe.arm.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.LTObject;
import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class InvalidObjectAdvice implements MethodInterceptor
{
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        LTObject target = (LTObject) invocation.getThis();

        if (target.isBad())
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }

        return invocation.proceed();
    }
}
