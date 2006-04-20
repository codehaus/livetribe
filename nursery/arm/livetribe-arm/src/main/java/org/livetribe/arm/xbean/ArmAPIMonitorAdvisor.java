package org.livetribe.arm.xbean;

import org.springframework.aop.support.DefaultPointcutAdvisor;


/**
 * @version $Revision: $ $Date: $
 */
public class ArmAPIMonitorAdvisor extends DefaultPointcutAdvisor
{
    public ArmAPIMonitorAdvisor()
    {
        super(new ArmAPIMonitorAdvice());
    }
}
