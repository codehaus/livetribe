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
package org.livetribe.arm;

import org.opengroup.arm40.metric.ArmMetric;
import org.opengroup.arm40.metric.ArmMetricDefinition;


/**
 * @version $Revision: $ $Date: $
 */
public class LTAbstractMetricBase extends LTAbstractObjectBase implements ArmMetric
{
    private final ArmMetricDefinition definition;
    private boolean valid;

    public LTAbstractMetricBase(LTAbstractFactoryBase factory, ArmMetricDefinition definition)
    {
        super(factory);

        this.definition = definition;
    }

    public ArmMetricDefinition getDefinition()
    {
        return definition;
    }

    public boolean isValid()
    {
        return valid;
    }

    public int setValid(boolean valid)
    {
        this.valid = valid;
        return 0;
    }
}