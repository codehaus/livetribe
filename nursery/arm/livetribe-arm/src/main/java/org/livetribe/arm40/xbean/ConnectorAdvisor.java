package org.livetribe.arm40.xbean;

import java.lang.reflect.Method;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import org.livetribe.arm40.connection.Connection;


/**
 * @version $Revision: $ $Date: $
 */
public class ConnectorAdvisor extends DefaultPointcutAdvisor
{
    private final static Pointcut POINTCUT = new StaticMethodMatcherPointcut()
    {
        public boolean matches(Method method, Class targetClass)
        {
            Class methodClass = method.getDeclaringClass();

            return methodClass == Connection.class;
        }
    };

    public ConnectorAdvisor()
    {
        super(ConnectorAdvisor.POINTCUT, new ErrorCheckingAdvice());
    }
}
