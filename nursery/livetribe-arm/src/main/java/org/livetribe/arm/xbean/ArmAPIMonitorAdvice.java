package org.livetribe.arm.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class ArmAPIMonitorAdvice implements MethodInterceptor
{
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Object rval = null;
        try
        {
            StaticArmAPIMonitor.begin((ArmInterface) invocation.getThis());

            rval = invocation.proceed();
        }
        finally
        {
            StaticArmAPIMonitor.end();
        }
        return rval;
    }
}
