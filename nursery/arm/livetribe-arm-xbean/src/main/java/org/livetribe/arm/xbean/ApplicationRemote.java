package org.livetribe.arm.xbean;

import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class ApplicationRemote extends Base
{
    private ArmTranReportFactory factory;
    private ApplicationDefinition applicationDefinition;
    private String group;
    private String instance;
    private String[] contextValues;
    private SystemAddress systemAddress;

    protected ArmInterface generate()
    {
        return factory.newArmApplicationRemote((ArmApplicationDefinition) applicationDefinition.unWrap(),
                                               group, instance, contextValues,
                                               (ArmSystemAddress) systemAddress.unWrap());
    }

    public ArmTranReportFactory getFactory()
    {
        return factory;
    }

    public void setFactory(ArmTranReportFactory factory)
    {
        this.factory = factory;
    }

    public ApplicationDefinition getApplicationDefinition()
    {
        return applicationDefinition;
    }

    public void setApplicationDefinition(ApplicationDefinition applicationDefinition)
    {
        this.applicationDefinition = applicationDefinition;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public String getInstance()
    {
        return instance;
    }

    public void setInstance(String instance)
    {
        this.instance = instance;
    }

    public String[] getContextValues()
    {
        return contextValues;
    }

    public void setContextValues(String[] contextValues)
    {
        this.contextValues = contextValues;
    }

    public SystemAddress getSystemAddress()
    {
        return systemAddress;
    }

    public void setSystemAddress(SystemAddress systemAddress)
    {
        this.systemAddress = systemAddress;
    }
}
