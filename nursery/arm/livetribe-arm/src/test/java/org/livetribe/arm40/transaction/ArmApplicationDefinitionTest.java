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
        assertEquals("A name that is too long is not an error. Error code must be set to success.", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("A name that is too long is not an error. Error code must be set to success in application definition.", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("A name that is too long is not an error. Error callback should not have been called.", callbackCalled);
        assertNull("A name that is too long is not an error. Callback target should not have been set.", callbackTarget);
        assertNull("A name that is too long is not an error. Callback interface name should not have been set.", callbackInterfaceName);
        assertNull("A name that is too long is not an error. Callback method name should not have been set.", callbackMethodName);

        reset();
        appDef = tranFactory.newArmApplicationDefinition(ZERO_LENGTH_NAME, null, null);

        assertFalse("Received a warning but a zero length name is an error.", warning);
        assertEquals("A zero length name is an error. Warning code must be zero.", 0, warningCode);
        assertTrue("Name is zero length. An error should have been given.", error);
        assertEquals("Name is zero length. Error code should have been set appropriately by callback.", GeneralErrorCodes.NAME_NULL_OR_EMPTY, errorCode);
        assertEquals("Name is zero length. Error code should have been set appropriately in application definition.", GeneralErrorCodes.NAME_NULL_OR_EMPTY, appDef.getErrorCode());
        assertTrue("Name is zero length. Error callback should have been called.", callbackCalled);
        assertSame("Incorrect object passed as callback target.", tranFactory, callbackTarget);
        assertEquals("Incorrect interface name passed to callback.", "org.opengroup.arm40.transaction.ArmTransactionFactory", callbackInterfaceName);
        assertEquals("Incorrect method name passed to callback.", "newArmApplicationDefinition", callbackMethodName);
        assertTrue("Name is zero length. Error code should be non-zero.", appDef.getErrorCode() != 0);

        reset();
        appDef = tranFactory.newArmApplicationDefinition(null, null, null);
        assertFalse("Received a warning but a null name is an error.", warning);
        assertEquals("A null is an error. Warning code must be zero.", 0, warningCode);
        assertTrue("Name is null. An error should have been given.", error);
        assertEquals("Name is null. Error code should have been set appropriately by callback.", GeneralErrorCodes.NAME_NULL_OR_EMPTY, errorCode);
        assertEquals("Name is null. Error code should have been set appropriately in application definition.", GeneralErrorCodes.NAME_NULL_OR_EMPTY, appDef.getErrorCode());
        assertTrue("Name is null. Error callback should have been called.", callbackCalled);
        assertSame("Incorrect object passed as callback target.", tranFactory, callbackTarget);
        assertEquals("Incorrect interface name passed to callback.", "org.opengroup.arm40.transaction.ArmTransactionFactory", callbackInterfaceName);
        assertEquals("Incorrect method name passed to callback.", "newArmApplicationDefinition", callbackMethodName);
        assertTrue("Name is null. Error code should be non-zero.", appDef.getErrorCode() != 0);

        reset();
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, null, null);

        assertFalse("Name is valid. Should not have gotten a warning.", warning);
        assertEquals("Name is valid. A warning code should not have been given.", GeneralErrorCodes.SUCCESS, warningCode);
        assertFalse("Name is valid. An error should not have been set.", error);
        assertEquals("Name is valid. Error code must be set to success.", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("Name is valid. Error code must be set to success in application definition.", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("Name is valid. Error callback should not have been called.", callbackCalled);
        assertNull("Name is valid. Callback target should not have been set.", callbackTarget);
        assertNull("Name is valid. Callback interface name should not have been set.", callbackInterfaceName);
        assertNull("Name is valid. Callback method name should not have been set.", callbackMethodName);
        assertTrue("Name is valid. Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Test that a valid identity properties parameter does not produce any errors or warnings.
     */
    public void testIdentityProperties()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, null);

        assertFalse("Both name and ARM ID properties are valid. A warning should not have been given.", warning);
        assertEquals("Both name and ARM ID properties are valid. A warning code should not have been given.", GeneralErrorCodes.SUCCESS, warningCode);
        assertFalse("Both name and ARM ID properties are valid. An error should not have been set.", error);
        assertEquals("Both name and ARM ID properties are valid. Error code must be set to success.", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("Both name and ARM ID properties are valid. Error code must be set to success in application definition.", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("Both name and ARM ID properties are valid. Error callback should not have been called.", callbackCalled);
        assertNull("Both name and ARM ID properties are valid. Callback target should not have been set.", callbackTarget);
        assertNull("Both name and ARM ID properties are valid. Callback interface name should not have been set.", callbackInterfaceName);
        assertNull("Both name and ARM ID properties are valid. Callback method name should not have been set.", callbackMethodName);
        assertTrue("Both name and ARM ID properties are valid. Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Test that a valid id parameter does not produce any errors or warnings.
     */
    public void testId()
    {
        reset();
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, null, armId);

        assertFalse("Both name and ARM ID are valid. A warning should not have been given.", warning);
        assertEquals("Both name and ARM ID are valid. A warning code should not have been given.", GeneralErrorCodes.SUCCESS, warningCode);
        assertFalse("Both name and ARM ID are valid. An error should not have been set.", error);
        assertEquals("Both name and ARM ID are valid. Error code must be set to success.", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("Both name and ARM ID are valid. Error code must be set to success in application definition.", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("Both name and ARM ID are valid. Error callback should not have been called.", callbackCalled);
        assertNull("Both name and ARM ID are valid. Callback target should not have been set.", callbackTarget);
        assertNull("Both name and ARM ID are valid. Callback interface name should not have been set.", callbackInterfaceName);
        assertNull("Both name and ARM ID are valid. Callback method name should not have been set.", callbackMethodName);
        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Pass non null parameters to factory method and validate if the getters return the correct results
     */
    public void testCreationWithAllParams()
    {
        ArmApplicationDefinition appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, armId);

        assertFalse("All parameters are valid. A warning should not have been given.", warning);
        assertEquals("All parameters are valid. A warning code should not have been given.", GeneralErrorCodes.SUCCESS, warningCode);
        assertFalse("All parameters are valid. An error should not have been set.", error);
        assertEquals("All parameters are valid. Error code must be set to success.", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("All parameters are valid. Error code must be set to success in application definition.", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("All parameters are valid. Error callback should not have been called.", callbackCalled);
        assertNull("All parameters are valid. Callback target should not have been set.", callbackTarget);
        assertNull("All parameters are valid. Callback interface name should not have been set.", callbackInterfaceName);
        assertNull("All parameters are valid. Callback method name should not have been set.", callbackMethodName);
        assertTrue("All parameters are valid. Error code should be zero (0).", appDef.getErrorCode() == 0);

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

        assertFalse("Calling destroy should not set a warning.", warning);
        assertEquals("Calling destroy should not have set a warning code.", GeneralErrorCodes.SUCCESS, warningCode);
        assertFalse("Calling destroy should not have set an error.", error);
        assertEquals("Destroy called. Error code must be set to success.", GeneralErrorCodes.SUCCESS, errorCode);
        assertEquals("Destroy called. Error code must be set to success in application definition.", GeneralErrorCodes.SUCCESS, appDef.getErrorCode());
        assertFalse("Destroy called. Error callback should not have been called.", callbackCalled);
        assertNull("Destroy called. Callback target should not have been set.", callbackTarget);
        assertNull("Destroy called. Callback interface name should not have been set.", callbackInterfaceName);
        assertNull("Destroy called. Callback method name should not have been set.", callbackMethodName);
        assertTrue("Destroy called. Error code should be zero (0).", appDef.getErrorCode() == 0);
        assertTrue("Destroy called. Error code should be zero (0).", appDef.getErrorCode() == 0);
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
