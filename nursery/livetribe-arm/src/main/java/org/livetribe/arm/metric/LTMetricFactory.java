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

import org.opengroup.arm40.metric.ArmMetric;
import org.opengroup.arm40.metric.ArmMetricCounter32;
import org.opengroup.arm40.metric.ArmMetricCounter32Definition;
import org.opengroup.arm40.metric.ArmMetricCounter64;
import org.opengroup.arm40.metric.ArmMetricCounter64Definition;
import org.opengroup.arm40.metric.ArmMetricCounterFloat32;
import org.opengroup.arm40.metric.ArmMetricCounterFloat32Definition;
import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.metric.ArmMetricGauge32;
import org.opengroup.arm40.metric.ArmMetricGauge32Definition;
import org.opengroup.arm40.metric.ArmMetricGauge64;
import org.opengroup.arm40.metric.ArmMetricGauge64Definition;
import org.opengroup.arm40.metric.ArmMetricGaugeFloat32;
import org.opengroup.arm40.metric.ArmMetricGaugeFloat32Definition;
import org.opengroup.arm40.metric.ArmMetricGroup;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.metric.ArmMetricNumericId32;
import org.opengroup.arm40.metric.ArmMetricNumericId32Definition;
import org.opengroup.arm40.metric.ArmMetricNumericId64;
import org.opengroup.arm40.metric.ArmMetricNumericId64Definition;
import org.opengroup.arm40.metric.ArmMetricString32;
import org.opengroup.arm40.metric.ArmMetricString32Definition;
import org.opengroup.arm40.metric.ArmTranReportWithMetrics;
import org.opengroup.arm40.metric.ArmTransactionWithMetrics;
import org.opengroup.arm40.metric.ArmTransactionWithMetricsDefinition;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;

import org.livetribe.arm.LTAbstractFactoryBase;
import org.livetribe.arm.util.LTProxyFactory;


/**
 * @version $Revision: $ $Date: $
 */
public class LTMetricFactory extends LTAbstractFactoryBase implements ArmMetricFactory
{
    public ArmMetricCounter32Definition newArmMetricCounter32Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricCounter32Definition) LTProxyFactory.newProxyInstance(new LTMetricCounter32Definition(this, name, units, usage, id));
    }

    public ArmMetricCounter64Definition newArmMetricCounter64Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricCounter64Definition) LTProxyFactory.newProxyInstance(new LTMetricCounter64Definition(this, name, units, usage, id));
    }

    public ArmMetricCounterFloat32Definition newArmMetricCounterFloat32Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricCounterFloat32Definition) LTProxyFactory.newProxyInstance(new LTMetricCounterFloat32Definition(this, name, units, usage, id));
    }

    public ArmMetricGauge32Definition newArmMetricGauge32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricGauge32Definition) LTProxyFactory.newProxyInstance(new LTMetricGauge32Definition(this, name, units, usage, id));
    }

    public ArmMetricGauge64Definition newArmMetricGauge64Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricGauge64Definition) LTProxyFactory.newProxyInstance(new LTMetricGauge64Definition(this, name, units, usage, id));
    }

    public ArmMetricGaugeFloat32Definition newArmMetricGaugeFloat32Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricGaugeFloat32Definition) LTProxyFactory.newProxyInstance(new LTMetricGaugeFloat32Definition(this, name, units, usage, id));
    }

    public ArmMetricNumericId32Definition newArmMetricNumericId32Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricNumericId32Definition) LTProxyFactory.newProxyInstance(new LTMetricNumericId32Definition(this, name, units, usage, id));
    }

    public ArmMetricNumericId64Definition newArmMetricNumericId64Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricNumericId64Definition) LTProxyFactory.newProxyInstance(new LTMetricNumericId64Definition(this, name, units, usage, id));
    }

    public ArmMetricString32Definition newArmMetricString32Definition(ArmApplicationDefinition app, String name, String units, short usage, ArmID id)
    {
        return (ArmMetricString32Definition) LTProxyFactory.newProxyInstance(new LTMetricString32Definition(this, name, units, usage, id));
    }

    public ArmMetricGroupDefinition newArmMetricGroupDefinition(ArmMetricDefinition[] definitions)
    {
        return (ArmMetricGroupDefinition) LTProxyFactory.newProxyInstance(new LTMetricGroupDefinition(this, definitions));
    }

    public ArmTransactionWithMetricsDefinition newArmTransactionWithMetricsDefinition(ArmApplicationDefinition app, String name, ArmIdentityPropertiesTransaction identityProperties, ArmMetricGroupDefinition definition, ArmID id)
    {
        return (ArmTransactionWithMetricsDefinition) LTProxyFactory.newProxyInstance(new LTTransactionWithMetricsDefinition(this, app, name, identityProperties, definition, id));
    }

    public ArmMetricCounter32 newArmMetricCounter32(ArmMetricCounter32Definition definition)
    {
        return (ArmMetricCounter32) LTProxyFactory.newProxyInstance(new LTMetricCounter32(this, definition));
    }

    public ArmMetricCounter64 newArmMetricCounter64(ArmMetricCounter64Definition definition)
    {
        return (ArmMetricCounter64) LTProxyFactory.newProxyInstance(new LTMetricCounter64(this, definition));
    }

    public ArmMetricCounterFloat32 newArmMetricCounterFloat32(ArmMetricCounterFloat32Definition definition)
    {
        return (ArmMetricCounterFloat32) LTProxyFactory.newProxyInstance(new LTMetricCounterFloat32(this, definition));
    }

    public ArmMetricGauge32 newArmMetricGauge32(ArmMetricGauge32Definition definition)
    {
        return (ArmMetricGauge32) LTProxyFactory.newProxyInstance(new LTMetricGauge32(this, definition));
    }

    public ArmMetricGauge64 newArmMetricGauge64(ArmMetricGauge64Definition definition)
    {
        return (ArmMetricGauge64) LTProxyFactory.newProxyInstance(new LTMetricGauge64(this, definition));
    }

    public ArmMetricGaugeFloat32 newArmMetricGaugeFloat32(ArmMetricGaugeFloat32Definition definition)
    {
        return (ArmMetricGaugeFloat32) LTProxyFactory.newProxyInstance(new LTMetricGaugeFloat32(this, definition));
    }

    public ArmMetricNumericId32 newArmMetricNumericId32(ArmMetricNumericId32Definition definition)
    {
        return (ArmMetricNumericId32) LTProxyFactory.newProxyInstance(new LTMetricNumericId32(this, definition));
    }

    public ArmMetricNumericId64 newArmMetricNumericId64(ArmMetricNumericId64Definition definition)
    {
        return (ArmMetricNumericId64) LTProxyFactory.newProxyInstance(new LTMetricNumericId64(this, definition));
    }

    public ArmMetricString32 newArmMetricString32(ArmMetricString32Definition definition)
    {
        return (ArmMetricString32) LTProxyFactory.newProxyInstance(new LTMetricString32(this, definition));
    }

    public ArmMetricGroup newArmMetricGroup(ArmMetricGroupDefinition groupDefinition, ArmMetric[] metrics)
    {
        return (ArmMetricGroup) LTProxyFactory.newProxyInstance(new LTMetricGroup(this, groupDefinition, metrics));
    }

    public ArmTranReportWithMetrics newArmTranReportWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        return (ArmTranReportWithMetrics) LTProxyFactory.newProxyInstance(new LTTranReportWithMetrics(this, app,  definition,  group));
    }

    public ArmTransactionWithMetrics newArmTransactionWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        return (ArmTransactionWithMetrics) LTProxyFactory.newProxyInstance(new LTTransactionWithMetrics(this, app,  definition,  group));
    }
}
