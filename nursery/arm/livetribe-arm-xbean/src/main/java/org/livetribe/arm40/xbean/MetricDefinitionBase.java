/**
 *
 * Copyright 2006 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.arm40.xbean;

import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.transaction.ArmTransactionFactory;


/**
 * @version $Revision: $ $Date$
 */
public abstract class MetricDefinitionBase extends Base
{
    private ArmMetricFactory metricFactory;
    private ArmTransactionFactory transFactory;
    private ApplicationDefinition applicationDefinition;
    private String name;
    private String units;
    private short usage;
    private byte[] armId;

    public ArmMetricFactory getMetricFactory()
    {
        return metricFactory;
    }

    public void setMetricFactory(ArmMetricFactory metricFactory)
    {
        this.metricFactory = metricFactory;
    }

    public ArmTransactionFactory getTransFactory()
    {
        return transFactory;
    }

    public void setTransFactory(ArmTransactionFactory transFactory)
    {
        this.transFactory = transFactory;
    }

    public ApplicationDefinition getApplicationDefinition()
    {
        return applicationDefinition;
    }

    public void setApplicationDefinition(ApplicationDefinition applicationDefinition)
    {
        this.applicationDefinition = applicationDefinition;
    }

    /**
     * @org.apache.xbean.Property alias="armName"
     */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUnits()
    {
        return units;
    }

    public void setUnits(String units)
    {
        this.units = units;
    }

    public short getUsage()
    {
        return usage;
    }

    public void setUsage(short usage)
    {
        this.usage = usage;
    }

    public byte[] getArmId()
    {
        return armId;
    }

    public void setArmId(byte[] armId)
    {
        this.armId = armId;
    }
}
