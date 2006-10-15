package org.livetribe.arm40.impl;


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
