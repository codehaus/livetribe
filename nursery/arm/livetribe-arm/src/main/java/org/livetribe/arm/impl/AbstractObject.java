package org.livetribe.arm.impl;

/**
 * @version $Revision: $ $Date: $
 */
public abstract class AbstractObject extends AbstractBase
{
    private boolean bad = false;

    public boolean isBad()
    {
        return bad;
    }

    public void setBad(boolean bad)
    {
        this.bad = bad;
    }
}
