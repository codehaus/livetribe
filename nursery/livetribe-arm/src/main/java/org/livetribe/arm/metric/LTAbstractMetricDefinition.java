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
package org.livetribe.arm.metric;

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.transaction.ArmID;

import org.livetribe.arm.LTAbstractBase;
import org.livetribe.arm.LTAbstractObjectBase;
import org.livetribe.arm.LTAbstractFactoryBase;

/**
 * @version $Revision: $ $Date: $
 */
class LTAbstractMetricDefinition extends LTAbstractObjectBase implements ArmMetricDefinition
{
    private final String name;
    private final String units;
    private final short usage;
    private final ArmID id;

    LTAbstractMetricDefinition(LTAbstractFactoryBase factory, String name, String units, short usage, ArmID id)
    {
        super(factory);

        this.name = name;
        this.units = units;
        this.usage = usage;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getUnits()
    {
        return units;
    }

    public short getUsage()
    {
        return usage;
    }

    public ArmID getID()
    {
        return id;
    }
}
