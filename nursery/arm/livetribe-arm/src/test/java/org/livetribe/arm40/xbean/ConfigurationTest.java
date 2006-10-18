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
package org.livetribe.arm40.xbean;

import junit.framework.TestCase;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.springframework.aop.framework.Advised;

import org.livetribe.arm40.impl.AbstractObject;
import org.livetribe.arm40.impl.LTMetricFactory;


/**
 * @version $Revision: $ $Date: $
 */
public class ConfigurationTest extends TestCase
{
    public void test0001() throws Exception
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/org/livetribe/arm40/impl/configure_metric.xml");
        try
        {
            ArmMetricFactory factory = (ArmMetricFactory) context.getBean("metricFactory");
            assertNotNull(factory);

            Object result = factory.newArmMetricCounter32(null);
            assertNotNull(result);
            assertTrue(((AbstractObject) obtainTarget(result)).isBad());
        }
        finally
        {
            context.destroy();
        }
    }

    public void testLTMetricFactory() throws Exception
    {
        LTMetricFactory factory = new LTMetricFactory();

        Object result = factory.newArmMetricCounter32(null);
        assertNotNull(result);
        assertTrue(((AbstractObject) obtainTarget(result)).isBad());
    }

    static Object obtainTarget(Object object) throws Exception
    {
        return (object instanceof Advised ? ((Advised) object).getTargetSource().getTarget() : object);
    }
}
