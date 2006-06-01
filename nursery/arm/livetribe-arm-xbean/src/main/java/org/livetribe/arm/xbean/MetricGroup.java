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

import org.opengroup.arm40.metric.ArmMetric;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class MetricGroup extends MetricBase
{
    private MetricGroupDefinition groupDefinition;
    private MetricBase[] metrics;

    protected ArmInterface generate()
    {
        ArmMetric[] unwrapped = new ArmMetric[metrics.length];
        for (int i = 0; i < unwrapped.length; i++)
        {
            unwrapped[i] = (ArmMetric) metrics[i].unWrap();
        }

        return getMetricFactory().newArmMetricGroup((ArmMetricGroupDefinition) groupDefinition.unWrap(),
                                                    unwrapped);
    }

    public MetricGroupDefinition getGroupDefinition()
    {
        return groupDefinition;
    }

    public void setGroupDefinition(MetricGroupDefinition groupDefinition)
    {
        this.groupDefinition = groupDefinition;
    }

    public MetricBase[] getMetrics()
    {
        return metrics;
    }

    public void setMetrics(MetricBase[] metrics)
    {
        if (metrics.length > 7) throw new IllegalArgumentException("Too many metrics");

        for (int i = 0; i < 6; i++)
        {
            if (metrics[i] != null && (metrics[i] instanceof MetricString32))
            {
                throw new IllegalArgumentException("String metric in wrong place");
            }
        }

        if (metrics.length == 7 && metrics[6] != null && !(metrics[6] instanceof MetricString32))
        {
            throw new IllegalArgumentException("Last entry not a string metric");
        }

        this.metrics = metrics;
    }
}
