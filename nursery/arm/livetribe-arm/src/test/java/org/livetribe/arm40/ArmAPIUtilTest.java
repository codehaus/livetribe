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
package org.livetribe.arm40;

import java.util.Properties;

import junit.framework.TestCase;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.springframework.context.ApplicationContext;

import org.livetribe.arm40.util.ARMException;
import org.livetribe.arm40.util.ARMUtil;


/**
 * @version $Revision: $ $Date: $
 */
public class ArmAPIUtilTest extends TestCase
{
    ApplicationContext applicationContext;
    ArmTransactionFactory tranFactory;
    ArmApplicationDefinition appDef;
    ArmApplication app;
    ArmTransactionDefinition tranDef;
    ArmTransaction tran;

    public void testUUIDGen()
    {
        tran.start();
        ArmCorrelator correlator = tran.getCorrelator();
        tran.stop(0);
    }

    public void setUp() throws ARMException
    {
        applicationContext = new ClassPathXmlApplicationContext("org/livetribe/arm40/ArmAPIUtilTest.xml");

        Properties p = System.getProperties();
        p.setProperty(ArmTransactionFactory.propertyKey, "org.livetribe.arm40.impl.LTTransactionFactory");
        p.setProperty(ArmMetricFactory.propertyKey, "org.livetribe.arm40.impl.LTMetricFactory");

        tranFactory = ARMUtil.createArmTransactionFactory();
        appDef = tranFactory.newArmApplicationDefinition("Simple Test Application", null, null);
        app = tranFactory.newArmApplication(appDef, "Examples", null, null);
        tranDef = tranFactory.newArmTransactionDefinition(appDef, "Transaction", null, null);
        tran = tranFactory.newArmTransaction(app, tranDef);
    }

    public void tearDown()
    {
        app.end();
        appDef.destroy();
    }
}
