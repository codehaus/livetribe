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
package org.livetribe.arm.util;

import junit.framework.TestCase;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;

/**
 * @version $Revision: $ $Date: $
 */
public class ARMUtilTest extends TestCase
{
    public void testcreateArmMetricFactory() throws Exception
    {
        ArmMetricFactory factory = ARMUtil.createArmMetricFactory();

        assertEquals("com.acme.arm.metric.AcmeMetricFactory", factory.getClass().getName());
    }

    public void testcreateArmTransactionFactory() throws Exception
    {
        ArmTransactionFactory factory = ARMUtil.createArmTransactionFactory();

        assertEquals("com.acme.arm.transaction.AcmeTransactionFactory", factory.getClass().getName());
    }

    public void testcreateArmTranReportFactory() throws Exception
    {
        ArmTranReportFactory factory = ARMUtil.createArmTranReportFactory();

        assertEquals("com.acme.arm.tranreport.AcmeTranReportFactory", factory.getClass().getName());
    }

    public void setUp()
    {
        System.setProperty(ArmMetricFactory.propertyKey, "com.acme.arm.metric.AcmeMetricFactory");
        System.setProperty(ArmTransactionFactory.propertyKey, "com.acme.arm.transaction.AcmeTransactionFactory");
        System.setProperty(ArmTranReportFactory.propertyKey, "com.acme.arm.tranreport.AcmeTranReportFactory");
    }
}
