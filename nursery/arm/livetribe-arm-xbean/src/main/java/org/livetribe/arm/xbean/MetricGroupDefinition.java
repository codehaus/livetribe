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
package org.livetribe.arm.xbean;

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class MetricGroupDefinition extends MetricBase
{
    MetricDefinitionBase[] metricDefinitions = new MetricDefinitionBase[0];

    protected ArmInterface generate()
    {
        ArmMetricDefinition[] unwrapped = new ArmMetricDefinition[metricDefinitions.length];
        for (int i = 0; i < unwrapped.length; i++)
        {
            unwrapped[i] = (ArmMetricDefinition) metricDefinitions[i].unWrap();
        }

        return getMetricFactory().newArmMetricGroupDefinition(unwrapped);
    }

    public MetricDefinitionBase[] getMetricDefinitions()
    {
        return metricDefinitions;
    }

    public void setMetricDefinitions(MetricDefinitionBase[] metricDefinitions)
    {
        if (metricDefinitions.length > 7) throw new IllegalArgumentException("Too many metric definitions");

        for (int i = 0; i < 6; i++)
        {
            if (metricDefinitions[i] != null && (metricDefinitions[i] instanceof MetricString32Definition))
            {
                throw new IllegalArgumentException("String definition in wrong place");
            }
        }

        if (metricDefinitions.length == 7 && metricDefinitions[6] != null && !(metricDefinitions[6] instanceof MetricString32Definition))
        {
            throw new IllegalArgumentException("Last entry not a string definition");
        }

        this.metricDefinitions = metricDefinitions;
    }
}
