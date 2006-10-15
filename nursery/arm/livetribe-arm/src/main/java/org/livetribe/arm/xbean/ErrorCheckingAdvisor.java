package org.livetribe.arm.xbean;

import java.lang.reflect.Method;

import org.opengroup.arm40.transaction.ArmInterface;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;


/**
 * The pointcut of this advisor is used to ensure that <code>ErrorCheckingAdvice</code>
 * is not used for methods that obtain the error code since <code>ErrorCheckingAdvice</code>
 * "clears" the error code.
 *
 * @version $Revision: $ $Date: $
 */
public class ErrorCheckingAdvisor extends DefaultPointcutAdvisor
{
    private final static Pointcut POINTCUT = new StaticMethodMatcherPointcut()
    {
        public boolean matches(Method method, Class targetClass)
        {
            Class methodClass = method.getDeclaringClass();

            return methodClass != ArmInterface.class;
        }
    };

    public ErrorCheckingAdvisor()
    {
        super(POINTCUT, new ErrorCheckingAdvice());
    }
}
