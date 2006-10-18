package org.livetribe.arm40.xbean;

import java.util.Stack;

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
                        callback.errorCodeSet(target,
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
