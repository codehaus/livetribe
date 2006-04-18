package org.livetribe.arm.xbean;

import java.lang.reflect.Method;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import org.livetribe.arm.LTObject;


/**
 * @version $Revision: $ $Date: $
 */
public class InvalidObjectAdvisor extends DefaultPointcutAdvisor
{
    private final static Pointcut POINTCUT = new StaticMethodMatcherPointcut()
    {
        public boolean matches(Method method, Class targetClass)
        {
            return LTObject.class.isAssignableFrom(targetClass);
        }
    };

    public InvalidObjectAdvisor()
    {
        super(InvalidObjectAdvisor.POINTCUT, new InvalidObjectAdvice());
    }
}
