package org.livetribe.arm.xbean;

import org.aopalliance.intercept.MethodInterceptor;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.util.ArmAPIMonitor;
import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public abstract class AbstractAdvice implements MethodInterceptor
{
    private static final Integer SUCCESS = new Integer(GeneralErrorCodes.SUCCESS);
    private static final String INIT = new String("INIT");
    private static final String CLEARED = new String("CLEARED");
    private static final ThreadLocal status = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return INIT;
        }
    };

    protected boolean isError()
    {
        return status.get() != SUCCESS;
    }

    protected int getErrorCode()
    {
        return ((Integer) status.get()).intValue();
    }

    static
    {
        StaticArmAPIMonitor.addArmAPIMonitor(new ArmAPIMonitor()
        {
            public void begin(ArmInterface object)
            {
                status.set(SUCCESS);
            }

            public void warning(int doNothing)
            {
            }

            public void error(int code)
            {
                status.set(new Integer(code));
            }

            public void end()
            {
                status.set(CLEARED);
            }
        });
    }
}
