package org.livetribe.arm.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.livetribe.arm.impl.AbstractObject;
import org.livetribe.arm.impl.GeneralErrorCodes;
import org.livetribe.arm.util.StaticArmAPIMonitor;
import org.springframework.aop.framework.Advised;


/**
 * @version $Revision: $ $Date: $
 */
public class InvalidObjectAdvice implements MethodInterceptor
{
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        AbstractObject target = (AbstractObject) obtainTarget(invocation.getThis());

        if (target.isBad())
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }

        return invocation.proceed();
    }

    static Object obtainTarget(Object object) throws Exception
    {
        return (object instanceof Advised ? ((Advised) object).getTargetSource().getTarget() : object);
    }
}
