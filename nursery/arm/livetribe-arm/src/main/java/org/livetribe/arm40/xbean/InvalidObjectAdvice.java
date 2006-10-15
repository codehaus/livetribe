package org.livetribe.arm40.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.livetribe.arm40.impl.AbstractObject;
import org.livetribe.arm40.impl.GeneralErrorCodes;
import org.livetribe.arm40.util.StaticArmAPIMonitor;
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
