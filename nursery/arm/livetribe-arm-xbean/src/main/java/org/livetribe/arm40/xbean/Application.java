package org.livetribe.arm40.xbean;

import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionFactory;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean namespace="http://livetribe.org/schemas/server/1_0" 
 */
public class Application extends Base
{
    private ArmTransactionFactory factory;
    private ApplicationDefinition applicationDefinition;
    private String group;
    private String instance;
    private String[] contextValues;

    protected ArmInterface generate()
    {
        return factory.newArmApplication((ArmApplicationDefinition) applicationDefinition.unWrap(),
                                         group, instance, contextValues);
    }

    public ArmTransactionFactory getFactory()
    {
        return factory;
    }

    public void setFactory(ArmTransactionFactory factory)
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
}
