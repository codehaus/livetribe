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
package org.livetribe.arm.impl;

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;


/**
 * @version $Revision: $ $Date: $
 */
abstract class AbstractMetricDefinition extends AbstractObject implements ArmMetricDefinition, MetricDefinition
{
    private final ArmApplicationDefinition appDef;
    private final String name;
    private final String units;
    private final short usage;
    private final ArmID id;

    AbstractMetricDefinition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        this.appDef = appDef;
        this.name = name;
        this.units = units;
        this.usage = usage;
        this.id = id;
    }

    public ArmApplicationDefinition getAppDef()
    {
        return appDef;
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
