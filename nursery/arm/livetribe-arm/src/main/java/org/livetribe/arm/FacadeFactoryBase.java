package org.livetribe.arm;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;


/**
 * @version $Revision: $ $Date: $
 */
public abstract class FacadeFactoryBase extends AbstractFactoryBase
{
    private final ApplicationContext applicationContext;

    public FacadeFactoryBase()
    {
        String configLocation = System.getProperty("org.livetribe.arm.config",
                                                   "/META-INF/org/livetribe/arm/impl/configure_" + getFactoryType() + ".xml");

        applicationContext = new ClassPathXmlApplicationContext(configLocation);
    }

    protected ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    protected abstract String getFactoryType();
}
