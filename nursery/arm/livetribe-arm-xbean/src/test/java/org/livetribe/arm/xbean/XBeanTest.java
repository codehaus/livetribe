/**
 *
 * Copyright 2006 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.arm.xbean;

import junit.framework.TestCase;
import org.apache.xbean.server.spring.main.SpringBootstrap;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.springframework.context.ApplicationContext;


/**
 * @version $Revision: $ $Date$
 */
public class XBeanTest extends TestCase
{
    public void testSimple()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("org/livetribe/arm/xbean/simple.xml");

        ApplicationDefinition def = (ApplicationDefinition) context.getBean("myAppDef");
        ArmApplicationDefinition wrapped = (ArmApplicationDefinition) def.unWrap();
    }

    public void testXBeanDeployer()
    {
        SpringBootstrap bootstrap = new SpringBootstrap();
        bootstrap.setConfigurationFile("test-xbean.xml");
        bootstrap.initialize(new String[0]);

        try
        {
            bootstrap.boot();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error booting test");
        }
    }

    public void setUp()
    {
        System.setProperty(ArmTransactionFactory.propertyKey, "org.livetribe.arm.impl.LTTransactionFactory");
        System.setProperty(ArmMetricFactory.propertyKey, "org.livetribe.arm.impl.LTMetricFactory");
    }
}
