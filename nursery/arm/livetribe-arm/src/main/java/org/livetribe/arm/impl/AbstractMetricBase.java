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
package org.livetribe.arm.impl;

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetric;


/**
 * @version $Revision: $ $Date: $
 */
abstract class AbstractMetricBase extends AbstractObject implements ArmMetric
{
    private final ArmMetricDefinition definition;
    private boolean valid;

    AbstractMetricBase(ArmMetricDefinition definition)
    {
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
        return GeneralErrorCodes.SUCCESS;
    }

    public abstract Object snapshot();
}
