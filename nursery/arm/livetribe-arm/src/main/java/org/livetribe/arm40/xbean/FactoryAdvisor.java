package org.livetribe.arm40.xbean;

import java.util.List;

import org.springframework.aop.support.DefaultPointcutAdvisor;


/**
 * @version $Revision: $ $Date: $
 */
public class FactoryAdvisor extends DefaultPointcutAdvisor
{
    private final FactoryAdvice factoryAdvice;

    public FactoryAdvisor()
    {
        super(new FactoryAdvice());

        factoryAdvice = (FactoryAdvice) getAdvice();
    }

    public List getAdvisors()
    {
        return factoryAdvice.getAdvisors();
    }

    public void setAdvisors(List advisors)
    {
        factoryAdvice.setAdvisors(advisors);
    }
}
