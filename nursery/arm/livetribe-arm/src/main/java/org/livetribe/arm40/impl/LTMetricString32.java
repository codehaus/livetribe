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

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricString32;


/**
 * @version $Revision: $ $Date: $
 */
class LTMetricString32 extends AbstractMetricBase implements ArmMetricString32
{
    private String metric;

    LTMetricString32(ArmMetricDefinition definition)
    {
        super(definition);
    }

    public String get()
    {
        return metric;
    }

    public int set(String s)
    {
        metric = s;
        return GeneralErrorCodes.SUCCESS;
    }

    public Object snapshot()
    {
        return (this.isValid() ? metric : null);
    }
}
