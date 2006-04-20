package org.livetribe.arm.xbean;

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

    public ProxyFactory getProxyFactory()
    {
        return factoryAdvice.getProxyFactory();
    }

    public void setProxyFactory(ProxyFactory factory)
    {
        factoryAdvice.setProxyFactory(factory);
    }
}
