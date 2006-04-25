/**
 *
 * Copyright 2006 (C) The original author or authors.
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
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.opengroup.arm40.metric.ArmMetricFactory;

import org.livetribe.arm.LTObject;
import org.livetribe.arm.impl.LTMetricFactory;


/**
 * @version $Revision: $ $Date: $
 */
public class XBeanTest extends TestCase
{
    public void test0001() throws Exception
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/org/livetribe/arm/impl/configure.xml");
        try
        {
            ArmMetricFactory factory = (ArmMetricFactory) context.getBean("metricFactory");
            assertNotNull(factory);

            LTObject result = (LTObject) factory.newArmMetricCounter32(null);
            assertNotNull(result);
            assertTrue(result.isBad());
        }
        finally
        {
            context.destroy();
        }
    }

    public void testLTMetricFactory()
    {
        LTMetricFactory factory = new LTMetricFactory();

        LTObject result = (LTObject) factory.newArmMetricCounter32(null);
        assertNotNull(result);
        assertTrue(result.isBad());
    }
}
