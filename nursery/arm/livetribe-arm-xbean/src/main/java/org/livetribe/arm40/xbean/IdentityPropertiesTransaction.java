package org.livetribe.arm40.xbean;

import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class IdentityPropertiesTransaction extends IdentityProperties
{
    private String uri;

    protected ArmInterface generate()
    {
        return getFactory().newArmIdentityPropertiesTransaction(getIdentityNames(), getIdentityNames(),
                                                                getIdentityNames(),
                                                                uri);
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }
}
