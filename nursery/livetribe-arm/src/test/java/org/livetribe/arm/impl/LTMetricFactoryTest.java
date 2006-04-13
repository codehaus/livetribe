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
package org.livetribe.arm.impl;

import junit.framework.TestCase;
import org.opengroup.arm40.metric.ArmMetricCounter32Definition;
import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.LTInvalidObject;


/**
 * @version $Revision: $ $Date: $
 */
public class LTMetricFactoryTest extends TestCase implements MetricErrorCodes
{
    private LTMetricFactoryImpl factory;
    private ArmApplicationDefinition app;

    public void testNewArmMetricCounter32Definition()
    {
        ArmMetricCounter32Definition def = factory.newArmMetricCounter32Definition(null, "FOO", null, (short) 0, null);

        assertFalse(def.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(def instanceof LTInvalidObject);

        def.getID();
        assertFalse(def.getErrorCode() == 0);

        def.getName();
        assertFalse(def.getErrorCode() == 0);

        def.getUnits();
        assertFalse(def.getErrorCode() == 0);

        def.getUsage();
        assertFalse(def.getErrorCode() == 0);

        def = factory.newArmMetricCounter32Definition(app, "", null, (short) 0, null);

        assertFalse(def.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(def instanceof LTInvalidObject);
        assertSame(app, ((LTMetricDefinition) def).getAppDef());

        def.getID();
        assertFalse(def.getErrorCode() == 0);

        def.getName();
        assertFalse(def.getErrorCode() == 0);

        def.getUnits();
        assertFalse(def.getErrorCode() == 0);

        def.getUsage();
        assertFalse(def.getErrorCode() == 0);

        final String[] result = new String[1];
        factory.setErrorCallback(new ArmErrorCallback()
        {
            public void errorCodeSet(ArmInterface errorObject, String interfaceName, String methodName)
            {
                result[0] = methodName;
            }
        });

        def = factory.newArmMetricCounter32Definition(null, "", null, (short) 0, null);

        assertEquals("newArmMetricCounter32Definition", result[0]);
        assertFalse(def.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(def instanceof LTInvalidObject);

        def.getID();
        assertFalse(def.getErrorCode() == 0);
        assertEquals("getID", result[0]);

        def.getName();
        assertFalse(def.getErrorCode() == 0);
        assertEquals("getName", result[0]);

        def.getUnits();
        assertFalse(def.getErrorCode() == 0);
        assertEquals("getUnits", result[0]);

        def.getUsage();
        assertFalse(def.getErrorCode() == 0);
        assertEquals("getUsage", result[0]);
    }

    //TODO: uncomment
    public void XtestNewArmMetricGroupDefinition()
    {
        ArmMetricGroupDefinition def = factory.newArmMetricGroupDefinition(null);

        assertTrue(def.getErrorCode() == 0);
        assertTrue(factory.getErrorCode() == 0);
        assertFalse(def instanceof LTInvalidObject);


        final String[] result = new String[1];
        factory.setErrorCallback(new ArmErrorCallback()
        {
            public void errorCodeSet(ArmInterface errorObject, String interfaceName, String methodName)
            {
                result[0] = methodName;
            }
        });

        ArmMetricDefinition[] definitions = new ArmMetricDefinition[7];

        definitions[0] = null;// new LTMetricString32Definition();

        def = factory.newArmMetricGroupDefinition(definitions);

        assertEquals("newArmMetricGroupDefinition", result[0]);
        assertFalse(def.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(def instanceof LTInvalidObject);

        def.getMetricDefinition(0);
        assertFalse(def.getErrorCode() == 0);
        assertEquals("getMetricDefinition", result[0]);

        definitions[0] = null;// new LTMetricCounter32Definition();

        def = factory.newArmMetricGroupDefinition(definitions);

        assertTrue(def.getErrorCode() == 0);
        assertTrue(factory.getErrorCode() == 0);
        assertFalse(def instanceof LTInvalidObject);

        definitions[6] =null;// new LTMetricCounter32Definition();

        def = factory.newArmMetricGroupDefinition(definitions);

        assertEquals("newArmMetricGroupDefinition", result[0]);
        assertFalse(def.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(def instanceof LTInvalidObject);

        def.getMetricDefinition(0);
        assertFalse(def.getErrorCode() == 0);
        assertEquals("getMetricDefinition", result[0]);
    }

    public void setUp()
    {
        factory = new LTMetricFactoryImpl();
        app = (new LTTransactionFactory()).newArmApplicationDefinition("TEST", null, null);
    }
}
