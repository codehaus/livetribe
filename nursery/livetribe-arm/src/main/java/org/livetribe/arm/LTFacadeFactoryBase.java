package org.livetribe.arm;

import org.opengroup.arm40.transaction.ArmErrorCallback;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;


/**
 * @version $Revision: $ $Date: $
 */
public abstract class LTFacadeFactoryBase extends LTAbstractFactoryBase
{
    private final ClassPathXmlApplicationContext applicationContext;

    public LTFacadeFactoryBase()
    {
        String configLocation = System.getProperty("org.livetribe.arm.config", "org/livetribe/arm/impl/configure.xml");

        applicationContext = new ClassPathXmlApplicationContext(configLocation);
    }

    protected ClassPathXmlApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
}
