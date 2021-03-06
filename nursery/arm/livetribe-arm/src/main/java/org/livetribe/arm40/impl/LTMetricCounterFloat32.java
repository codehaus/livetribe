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

import org.opengroup.arm40.metric.ArmMetricCounterFloat32;
import org.opengroup.arm40.metric.ArmMetricDefinition;


/**
 * @version $Revision: $ $Date: $
 */
class LTMetricCounterFloat32 extends AbstractMetricBase implements ArmMetricCounterFloat32
{
    private float metric;

    LTMetricCounterFloat32(ArmMetricDefinition definition)
    {
        super(definition);
    }

    public float get()
    {
        return metric;
    }

    public int set(float value)
    {
        metric = value;
        return GeneralErrorCodes.SUCCESS;
    }

    public Object snapshot()
    {
        return (this.isValid() ? new Float(metric) : null);
    }
}
