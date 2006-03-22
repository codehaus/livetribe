package org.livetribe.arm;

/**
 * @version $Revision: $ $Date: $
 */
public abstract class LTAbstractObjectBase extends LTAbstractBase
{
    private final LTAbstractFactoryBase factory;

    public LTAbstractObjectBase(LTAbstractFactoryBase factory)
    {
        assert factory != null;
        this.factory = factory;
    }

    public LTAbstractFactoryBase getFactory()
    {
        return factory;
    }
}
