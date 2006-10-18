package org.livetribe.arm40.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.Advised;

import org.livetribe.arm40.impl.AbstractObject;


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
        AbstractObject target = (AbstractObject) obtainTarget(invocation.getThis());

        if (!target.isBad()) return invocation.proceed();
        else return null;
    }

    static Object obtainTarget(Object object) throws Exception
    {
        return (object instanceof Advised ? ((Advised) object).getTargetSource().getTarget() : object);
    }
}
