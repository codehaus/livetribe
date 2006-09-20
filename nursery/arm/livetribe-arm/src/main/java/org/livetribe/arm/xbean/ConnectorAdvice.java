package org.livetribe.arm.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.livetribe.arm.impl.LTObject;


/**
 * If the object is bad, then don't even bother the connector with the method
 * call.
 *
 * @version $Revision: $ $Date: $
 */
public class ConnectorAdvice implements MethodInterceptor
{
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        LTObject target = (LTObject) invocation.getThis();

        if (!target.isBad()) return invocation.proceed();
        else return null;
    }
}
