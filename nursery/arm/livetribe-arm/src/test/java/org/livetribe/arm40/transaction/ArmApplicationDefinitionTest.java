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
package org.livetribe.arm40.transaction;

import java.util.Properties;

import junit.framework.TestCase;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionFactory;

import org.livetribe.arm40.impl.GeneralErrorCodes;
import org.livetribe.arm40.util.ARMException;
import org.livetribe.arm40.util.ARMUtil;
import org.livetribe.arm40.util.ArmAPIMonitor;
import org.livetribe.arm40.util.StaticArmAPIMonitor;


/**
 * Unit test for org.opengroup.arm40.transaction.ArmApplicationDefinition implementation.
 *
 * @version $Revision: $ $Date: $
 */
public class ArmApplicationDefinitionTest extends TestCase
{
    ArmTransactionFactory tranFactory;

    // A name valid name for an ArmApplicationDefinition.
    private final String VALID_NAME = "ArmApplicationDefinition Test";
    // A name that is too long (length > 127) for an ArmApplicationDefinition.
    private final String LONG_NAME = "The Quick Brown Fox Jumped Over The Lazy Dog. This Name Should Be Longer Than 127 Characters To Be Deemed Invalid. This Should Do It.";
    // A zero length string is also invalid for an ArmApplicationDefinition.
    private final String ZERO_LENGTH_NAME = "";

    //Byte array for creation of arm id.
    private final byte[] id = {125};

    private ArmID armId;

    // String arrays to be passed to factory method for creation of an implementation of ArmIdentityProperties
    private final String[] identityNames = {"NAME"};
    private final String[] identityValues = {"VALUE"};
    private final String[] contextNames = {"CONTEXT"};

    private ArmIdentityProperties armIdProperties;

    private boolean warning;
    private int warningCode;
    private boolean error;
    private int errorCode;
    private boolean callbackCalled;
    private ArmInterface callbackTarget;
    private String callbackInterfaceName;
    private String callbackMethodName;

    /**
     * Test that the name parameter is valid according to the ARM spec.
     * Valid names are defined by the ARM spec as follows:
     * <ul>
     * <li>The maximum length is 127 characters (CIM allows 256 but ARM 4.0 C Bindings
     * allow 128 characters, including the null-termination character, so 127 is used)</li>
     * <li>The name must not be null or zero-length.</li>
     * </ul>
     * <ul>The following are also recommended by the spec but are not tested here:
     * <li>A name should be chosen that is unique</li>
     * <li>Names should not contain trailing blank characters or consist of only blank characters.</li>
     * </ul>
     */
    public void testName()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(LONG_NAME, null, null);

        assertTrue("Name is too long. A warning should have been given.", warning);
        assertEquals("Name is too long. A proper warning code should have been given.", GeneralErrorCodes.NAME_LENGTH_LARGE, warningCode);
        assertFalse("A name that is too long is not an error.", error);
        assertEquals("", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("", callbackCalled);
        assertNull("", callbackTarget);
        assertNull("", callbackInterfaceName);
        assertNull("", callbackMethodName);

        reset();
        appDef = tranFactory.newArmApplicationDefinition(ZERO_LENGTH_NAME, null, null);

        assertFalse("", warning);
        assertEquals("", 0, warningCode);
        assertTrue("", error);
        assertEquals("", GeneralErrorCodes.NAME_NULL_OR_EMPTY, errorCode);
        assertEquals("", GeneralErrorCodes.NAME_NULL_OR_EMPTY, appDef.getErrorCode());
        assertTrue("", callbackCalled);
        assertSame("", tranFactory, callbackTarget);
        assertEquals("", "org.opengroup.arm40.transaction.ArmTransactionFactory", callbackInterfaceName);
        assertEquals("", "newArmApplicationDefinition", callbackMethodName);

        assertTrue("Name is zero length. Error code should be non-zero.", appDef.getErrorCode() != 0);

        reset();
        appDef = tranFactory.newArmApplicationDefinition(null, null, null);

        assertTrue("Name is null. Error code should be non-zero.", appDef.getErrorCode() != 0);

        reset();
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, null, null);

        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Test that the the identity properties method is equal to that passed into the factory method.
     */
    public void testIdentityProperties()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, null);

        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Test that the id parameter is equal to that passed to the factory method.
     */
    public void testId()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, null, armId);

        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Pass non null parameters to factory method and validate if the getters return the correct results
     */
    public void testCreationWithAllParams()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, armId);

        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);

        assertTrue("Name should be equal to what was passed to newArmApplicationDefinition", appDef.getName().equals(VALID_NAME));

        //TODO: It is not clear to me if this is required. Is it?
        assertTrue("Arm Id Properties should be what was passed to newArmApplicationDefinition", appDef.getIdentityProperties().equals(armIdProperties));

        assertTrue("Id should be what was passed to newArmApplicationDefinition", appDef.getID().equals(armId));
    }

    /**
     * Test calling destroy on an application definition and that no error code is set.
     */
    public void testDestroy()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, armId);

        appDef.destroy();

        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    public void setUp() throws ARMException
    {
        Properties p = System.getProperties();
        p.setProperty(ArmTransactionFactory.propertyKey, "org.livetribe.arm40.impl.LTTransactionFactory");
        p.setProperty(ArmMetricFactory.propertyKey, "org.livetribe.arm40.impl.LTMetricFactory");

        tranFactory = ARMUtil.createArmTransactionFactory();
        armId = tranFactory.newArmID(id);
        armIdProperties = tranFactory.newArmIdentityProperties(identityNames, identityValues, contextNames);

        tranFactory.setErrorCallback(new ArmErrorCallback()
        {
            public void errorCodeSet(ArmInterface target, String className, String methodName)
            {
                callbackCalled = true;
                callbackTarget = target;
                callbackInterfaceName = className;
                callbackMethodName = methodName;
            }
        });

        reset();

        StaticArmAPIMonitor.addArmAPIMonitor(new ArmAPIMonitor()
        {
            public void begin(ArmInterface object)
            {
            }

            public void warning(int code)
            {
                warning = true;
                warningCode = code;
            }

            public void error(int code)
            {
                error = true;
                errorCode = code;
            }

            public void end()
            {
            }
        });
    }

    public void tearDown()
    {
        armIdProperties = null;
        armId = null;
        tranFactory = null;
    }

    private void reset()
    {
        warning = false;
        warningCode = 0;
        error = false;
        errorCode = 0;
        callbackCalled = false;
        callbackTarget = null;
        callbackInterfaceName = null;
        callbackMethodName = null;
    }
}
