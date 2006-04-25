package org.livetribe.arm;

/**
 * @version $Revision: $ $Date: $
 */
public abstract class AbstractObject extends AbstractBase implements LTObject
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
