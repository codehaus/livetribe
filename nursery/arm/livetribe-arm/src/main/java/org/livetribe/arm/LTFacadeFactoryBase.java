package org.livetribe.arm;

import org.springframework.context.ApplicationContext;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;


/**
 * @version $Revision: $ $Date: $
 */
public abstract class LTFacadeFactoryBase extends LTAbstractFactoryBase
{
    private final ApplicationContext applicationContext;

    public LTFacadeFactoryBase()
    {
        String configLocation = System.getProperty("org.livetribe.arm.config", "org/livetribe/arm/impl/configure.xml");

        applicationContext = new ClassPathXmlApplicationContext(configLocation);
    }

    protected ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
}
