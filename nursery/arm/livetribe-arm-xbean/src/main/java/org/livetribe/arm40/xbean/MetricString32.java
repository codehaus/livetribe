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

import org.opengroup.arm40.metric.ArmMetricString32Definition;
import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class MetricString32 extends MetricBase
{
    MetricString32Definition metricDefinition;

    protected ArmInterface generate()
    {
        return getMetricFactory().newArmMetricString32((ArmMetricString32Definition) metricDefinition.unWrap());
    }

    public MetricString32Definition getMetricDefinition()
    {
        return metricDefinition;
    }

    public void setMetricDefinition(MetricString32Definition metricDefinition)
    {
        this.metricDefinition = metricDefinition;
    }
}
