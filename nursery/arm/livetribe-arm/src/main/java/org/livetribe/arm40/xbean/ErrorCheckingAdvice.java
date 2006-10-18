package org.livetribe.arm40.xbean;

import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;
import org.springframework.aop.framework.Advised;

import org.livetribe.arm40.impl.AbstractFactoryBase;
import org.livetribe.arm40.impl.AbstractObject;
import org.livetribe.arm40.impl.GeneralErrorCodes;
import org.livetribe.arm40.util.ArmAPIMonitor;
import org.livetribe.arm40.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class ErrorCheckingAdvice implements MethodInterceptor
{
    private static final ThreadLocal status = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return new Level();
        }
    };

    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        ArmInterface target = (ArmInterface) invocation.getThis();
        Object rval = null;
        try
        {
            target.setErrorCode(GeneralErrorCodes.SUCCESS);

            rval = invocation.proceed();
        }
        finally
        {
            if (isError())
            {
                int errorCode = getErrorCode();

                target.setErrorCode(errorCode);

                Object test = (rval instanceof Advised ? ((Advised) rval).getTargetSource().getTarget() : rval);
                if (test != null && AbstractObject.class.isAssignableFrom(test.getClass()))
                {
                    AbstractObject result = (AbstractObject) test;

                    result.setBad(true);
                    result.setErrorCode(errorCode);
                }

                ArmErrorCallback callback = AbstractFactoryBase.getCallback();
                if (callback != null)
                {
                    try
                    {
                        callback.errorCodeSet((ArmInterface) getRegisteredProxy(target),
                                              invocation.getMethod().getDeclaringClass().getName(),
                                              invocation.getMethod().getName());
                    }
                    catch (Throwable ignore)
                    {
                        // We're notifying the client, they should be nice to us...
                    }
                }
            }
        }
        return rval;
    }

    /**
     * A mapping from a target object and the proxy that the application receives.
     */
    private final static Map OBJECTS_PROXIES = Collections.synchronizedMap(new WeakHashMap());

    /**
     * Obtain the proxy of a target object.
     * <p/>
     * All of this is so that if there is an error, we can return the proper
     * target object in the error callback.
     *
     * @param key the target pbject
     * @return the proxy
     * @see ErrorCheckingAdvice#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    private static Object getRegisteredProxy(Object key)
    {
        return OBJECTS_PROXIES.get(key);
    }

    /**
     * Register a proxy using the target object as a key.
     * <p/>
     * All of this is so that if there is an error, we can return the proper
     * target object in the error callback.
     *
     * @param key   the target object that is currently wrapped in a Spring proxy
     * @param value the proxy to be registered
     */
    public static void registerProxy(Object key, Object value)
    {
        try
        {
            OBJECTS_PROXIES.put(((Advised) key).getTargetSource().getTarget(), value);
        }
        catch (Exception doNothing)
        {
        }
    }

    static boolean isError()
    {
        return ((Level) status.get()).isError();
    }

    static int getErrorCode()
    {
        return ((Level) status.get()).getErrorCode();
    }

    static
    {
        StaticArmAPIMonitor.addArmAPIMonitor(new ArmAPIMonitor()
        {
            public void begin(ArmInterface object)
            {
                ((Level) status.get()).push();
            }

            public void warning(int doNothing)
            {
            }

            public void error(int code)
            {
                ((Level) status.get()).error(code);
            }

            public void end()
            {
                ((Level) status.get()).pop();
            }
        });
    }

    static class Level
    {
        Stack stack = new Stack();
        int current = GeneralErrorCodes.SUCCESS;

        boolean isError()
        {
            return current != GeneralErrorCodes.SUCCESS;
        }

        int getErrorCode()
        {
            return current;
        }

        void push()
        {
            stack.push(new Integer(current));
            current = GeneralErrorCodes.SUCCESS;
        }

        void error(int code)
        {
            current = code;
        }

        void pop()
        {
            int saved = current;
            current = ((Integer) stack.pop()).intValue();
            if (!stack.isEmpty() && current == GeneralErrorCodes.SUCCESS) current = saved;
        }
    }
}
