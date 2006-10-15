package org.livetribe.arm40.impl;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;


/**
 * @version $Revision: $ $Date: $
 */
abstract class FacadeFactoryBase extends AbstractFactoryBase
{
    private final ApplicationContext applicationContext;

    FacadeFactoryBase()
    {
        String configLocation = System.getProperty("org.livetribe.arm40.config",
                                                   "/META-INF/org/livetribe/arm40/impl/configure_" + getFactoryType() + ".xml");

        applicationContext = new ClassPathXmlApplicationContext(configLocation);
    }

    protected ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    protected abstract String getFactoryType();
}
