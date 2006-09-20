package org.livetribe.arm.xbean;

import java.lang.reflect.Method;

import org.opengroup.arm40.transaction.ArmInterface;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import org.livetribe.arm.impl.LTObject;


/**
 * @version $Revision: $ $Date: $
 */
public class ErrorCheckingAdvisor extends DefaultPointcutAdvisor
{
    private final static Pointcut POINTCUT = new StaticMethodMatcherPointcut()
    {
        public boolean matches(Method method, Class targetClass)
        {
            Class methodClass = method.getDeclaringClass();

            if (methodClass == ArmInterface.class || methodClass == LTObject.class) return false;

            return true;
        }
    };

    public ErrorCheckingAdvisor()
    {
        super(POINTCUT, new ErrorCheckingAdvice());
    }
}
