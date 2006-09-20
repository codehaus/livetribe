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

import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.metric.ArmTransactionWithMetricsDefinition;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;


/**
 * @version $Revision: $ $Date: $
 */
class LTTransactionWithMetricsDefinition extends AbstractIdentifiableObject implements ArmTransactionWithMetricsDefinition
{
    private final ArmApplicationDefinition app;
    private final String name;
    private final ArmIdentityPropertiesTransaction identityProperties;
    private final ArmMetricGroupDefinition definition;
    private final ArmID id;

    LTTransactionWithMetricsDefinition(String oid, ArmApplicationDefinition app, String name, ArmIdentityPropertiesTransaction identityProperties, ArmMetricGroupDefinition definition, ArmID id)
    {
        super(oid);

        this.app = app;
        this.name = name;
        this.identityProperties = identityProperties;
        this.definition = definition;
        this.id = id;
    }

    public ArmMetricGroupDefinition getMetricGroupDefinition()
    {
        return definition;
    }

    public ArmApplicationDefinition getApplicationDefinition()
    {
        return app;
    }

    public ArmID getID()
    {
        return id;
    }

    public ArmIdentityPropertiesTransaction getIdentityProperties()
    {
        return identityProperties;
    }

    public String getName()
    {
        return name;
    }
}
