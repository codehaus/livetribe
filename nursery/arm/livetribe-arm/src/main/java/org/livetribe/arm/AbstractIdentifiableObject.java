package org.livetribe.arm;

import org.livetribe.arm.impl.ArmAPIUtil;

/**
 * @version $Revision: $ $Date: $
 */
public abstract class AbstractIdentifiableObject extends AbstractObject implements Identifiable
{
    private final byte[] id = ArmAPIUtil.newArmCorrelator(false).getBytes();

    public byte[] getObjectId()
    {
        return id;
    }
}
