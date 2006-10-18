package org.livetribe.arm40.transaction;

import java.util.Properties;

import junit.framework.TestCase;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmTransactionFactory;

import org.livetribe.arm40.util.ARMException;
import org.livetribe.arm40.util.ARMUtil;


/**
 * Unit test for org.opengroup.arm40.transaction.ArmApplicationDefinition implementation.
 *
 * @version $Revision: $ $Date: $
 */
public class ArmApplicationDefinitionTest extends TestCase
{
    ArmTransactionFactory tranFactory;
    ArmApplicationDefinition appDef;

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
        //Create an instance with a name that is too long.
        appDef = tranFactory.newArmApplicationDefinition(LONG_NAME, null, null);
        //Error code is set because name is too long.
        assertTrue("Name is too long. Error code should be non-zero.", appDef.getErrorCode() != 0);

        //Create an instance with a zero length name.
        appDef = tranFactory.newArmApplicationDefinition(ZERO_LENGTH_NAME, null, null);
        //Error code is set because name is zero length.
        assertTrue("Name is zero length. Error code should be non-zero.", appDef.getErrorCode() != 0);

        // Create an instance with a null name.
        appDef = tranFactory.newArmApplicationDefinition(null, null, null);
        //Error code is set because name is null.
        assertTrue("Name is null. Error code should be non-zero.", appDef.getErrorCode() != 0);

        //Create an instance with a valid name.
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, null, null);
        //There shouldn't be an error.
        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Test that the the identity properties method is equal to that passed into the factory method.
     */
    public void testIdentityProperties()
    {
        //Create an instance with a valid name and properties.
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, null);
        //There shouldn't be an error.
        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Test that the id parameter is equal to that passed to the factory method.
     */
    public void testId()
    {
        //Create an instance with a valid name and ID.
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, null, armId);
        //There shouldn't be an error.
        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);
    }

    /**
     * Pass non null parameters to factory method and validate if the getters return the correct results
     */
    public void testCreationWithAllParams()
    {
        //Create an instance with a valid name and properties.
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, armId);
        //There shouldn't be an error.
        assertTrue("Error code should be zero (0).", appDef.getErrorCode() == 0);

        //Name should be what equal to that passed to the factory method
        assertTrue("Name should be equal to what was passed to newArmApplicationDefinition", appDef.getName().equals(VALID_NAME));

        //Identity Properties should be what equal to that passed to the factory method
        //TODO: It is not clear to me if this is required. Is it?
        assertTrue("Arm Id Properties should be what was passed to newArmApplicationDefinition", appDef.getIdentityProperties().equals(armIdProperties));

        //Id should be what equal to that passed to the factory method
        assertTrue("Id should be what was passed to newArmApplicationDefinition", appDef.getID().equals(armId));
    }

    /**
     * Test calling destroy on an application definition and that no error code is set.
     */
    public void testDestroy()
    {
        //Create an instance with a valid name and properties.
        appDef = tranFactory.newArmApplicationDefinition(VALID_NAME, armIdProperties, armId);

        appDef.destroy();
        //There shouldn't be an error.
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
    }

    public void tearDown()
    {
        armIdProperties = null;
        armId = null;
        tranFactory = null;
    }

}
