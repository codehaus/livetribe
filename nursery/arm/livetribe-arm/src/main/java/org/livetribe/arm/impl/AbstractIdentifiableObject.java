package org.livetribe.arm.impl;


/**
 * @version $Revision: $ $Date: $
 */
abstract class AbstractIdentifiableObject extends AbstractObject implements Identifiable
{
    private final String id;

    AbstractIdentifiableObject(String id)
    {
        this.id = id;
    }

    public String getObjectId()
    {
        return id;
    }
}
