package org.livetribe.arm40.impl;

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
