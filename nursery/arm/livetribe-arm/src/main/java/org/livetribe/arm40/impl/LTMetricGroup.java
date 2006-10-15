/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm40.impl;

import java.util.ArrayList;
import java.util.List;

import org.livetribe.arm40.util.StaticArmAPIMonitor;
import org.opengroup.arm40.metric.ArmMetric;
import org.opengroup.arm40.metric.ArmMetricGroup;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.springframework.aop.framework.Advised;


/**
 * @version $Revision: $ $Date: $
 */
class LTMetricGroup extends AbstractIdentifiableObject implements ArmMetricGroup
{
    private final ArmMetricGroupDefinition groupDefinition;
    private final ArmMetric[] proxiedMetrics;
    private final AbstractMetricBase[] metrics;
    private final List[] data;


    LTMetricGroup(String oid, ArmMetricGroupDefinition groupDefinition, ArmMetric[] metrics)
    {
        super(oid);

        this.groupDefinition = groupDefinition;
        this.proxiedMetrics = metrics;
        this.data = new List[metrics.length];

        this.metrics = new AbstractMetricBase[metrics.length];
        for (int i = 0; i < metrics.length; i++)
        {
            try
            {
                this.metrics[i] = (AbstractMetricBase) ((Advised) metrics[i]).getTargetSource().getTarget();
            }
            catch (Exception e)
            {
                this.metrics[i] = null;
            }
        }

        clear();
    }

    public ArmMetricGroupDefinition getDefinition()
    {
        return groupDefinition;
    }

    public ArmMetric getMetric(int index)
    {
        if (index < 0 || proxiedMetrics.length <= index)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.INDEX_OUT_OF_RANGE);
            return null;
        }

        return proxiedMetrics[index];
    }

    public boolean isMetricValid(int index)
    {
        if (index < 0 || metrics.length <= index)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.INDEX_OUT_OF_RANGE);
            return false;
        }

        if (metrics[index] != null) return metrics[index].isValid();
        else return false;
    }

    public int setMetricValid(int index, boolean value)
    {
        if (index < 0 || metrics.length <= index)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.INDEX_OUT_OF_RANGE);
            return GeneralErrorCodes.INDEX_OUT_OF_RANGE;
        }

        if (metrics[index] != null) return metrics[index].setValid(value);
        else return GeneralErrorCodes.INDEX_OUT_OF_RANGE;
    }

    void clear()
    {
        for (int i = 0; i < data.length; i++) data[i] = new ArrayList();
    }

    void start()
    {
        clear();
    }

    void snapshot()
    {
        for (int i = 0; i < metrics.length; i++)
            data[i].add(metrics[i] != null ? metrics[i].snapshot() : null);
    }

    List[] stop()
    {
        List[] result = new List[data.length];

        System.arraycopy(data, 0, result, 0, data.length);

        clear();

        return result;
    }
}
