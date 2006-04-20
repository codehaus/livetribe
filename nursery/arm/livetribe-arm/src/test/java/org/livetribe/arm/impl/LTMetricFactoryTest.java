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

import org.livetribe.arm.LTObject;


/**
 * @version $Revision: $ $Date: $
 */
public class LTMetricFactoryTest extends TestCase implements MetricErrorCodes
{
    private LTMetricFactory factory;
    private ArmApplicationDefinition app;

    public void testNewArmMetricCounter32Definition() throws Exception
    {
        ArmMetricCounter32Definition bad = factory.newArmMetricCounter32Definition(null, "FOO", null, (short) 0, null);

        assertFalse(bad.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(((LTObject) bad).isBad());

        bad.getID();
        assertFalse(bad.getErrorCode() == 0);

        bad.getName();
        assertFalse(bad.getErrorCode() == 0);

        bad.getUnits();
        assertFalse(bad.getErrorCode() == 0);

        bad.getUsage();
        assertFalse(bad.getErrorCode() == 0);

        bad = factory.newArmMetricCounter32Definition(app, "", null, (short) 0, null);

        assertFalse(bad.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(((LTObject) bad).isBad());
        assertSame(app, ((LTMetricDefinition) bad).getAppDef());

        bad.getID();
        assertFalse(bad.getErrorCode() == 0);

        bad.getName();
        assertFalse(bad.getErrorCode() == 0);

        bad.getUnits();
        assertFalse(bad.getErrorCode() == 0);

        bad.getUsage();
        assertFalse(bad.getErrorCode() == 0);

        final String[] result = new String[1];
        factory.setErrorCallback(new ArmErrorCallback()
        {
            public void errorCodeSet(ArmInterface errorObject, String interfaceName, String methodName)
            {
                result[0] = methodName;
            }
        });

        bad = factory.newArmMetricCounter32Definition(null, "", null, (short) 0, null);

        assertEquals("newArmMetricCounter32Definition", result[0]);
        assertFalse(bad.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(((LTObject) bad).isBad());

        bad.getID();
        assertFalse(bad.getErrorCode() == 0);
        assertEquals("getID", result[0]);

        bad.getName();
        assertFalse(bad.getErrorCode() == 0);
        assertEquals("getName", result[0]);

        bad.getUnits();
        assertFalse(bad.getErrorCode() == 0);
        assertEquals("getUnits", result[0]);

        bad.getUsage();
        assertFalse(bad.getErrorCode() == 0);
        assertEquals("getUsage", result[0]);
    }

    public void testNewArmMetricGroupDefinition()
    {
        ArmMetricGroupDefinition bad = factory.newArmMetricGroupDefinition(null);

        assertTrue(bad.getErrorCode() == 0);
        assertTrue(factory.getErrorCode() == 0);

        final String[] result = new String[1];
        factory.setErrorCallback(new ArmErrorCallback()
        {
            public void errorCodeSet(ArmInterface errorObject, String interfaceName, String methodName)
            {
                result[0] = methodName;
            }
        });

        ArmMetricDefinition[] definitions = new ArmMetricDefinition[7];

        definitions[0] = new LTMetricString32Definition(null, null, null, (short) 0, null);

        bad = factory.newArmMetricGroupDefinition(definitions);

        assertEquals("newArmMetricGroupDefinition", result[0]);
        assertFalse(bad.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(((LTObject) bad).isBad());

        bad.getMetricDefinition(0);
        assertFalse(bad.getErrorCode() == 0);
        assertEquals("getMetricDefinition", result[0]);

        definitions[0] = new LTMetricCounter32Definition(null, null, null, (short) 0, null);

        ArmMetricGroupDefinition good = factory.newArmMetricGroupDefinition(definitions);

        assertTrue(good.getErrorCode() == 0);
        assertTrue(factory.getErrorCode() == 0);
        assertFalse(((LTObject) good).isBad());

        definitions[6] = new LTMetricCounter32Definition(null, null, null, (short) 0, null);

        bad = factory.newArmMetricGroupDefinition(definitions);

        assertEquals("newArmMetricGroupDefinition", result[0]);
        assertFalse(bad.getErrorCode() == 0);
        assertFalse(factory.getErrorCode() == 0);
        assertTrue(((LTObject) bad).isBad());

        bad.getMetricDefinition(0);
        assertFalse(bad.getErrorCode() == 0);
        assertEquals("getMetricDefinition", result[0]);
    }

    public void setUp()
    {
        factory = new LTMetricFactory();
        app = (new LTTransactionFactory()).newArmApplicationDefinition("TEST", null, null);
    }
}
