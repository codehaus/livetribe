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
import org.livetribe.arm.LTObject;
import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class LTMetricFactoryImpl extends LTAbstractFactoryBase implements ArmMetricFactory, MetricErrorCodes
{
    public ArmMetricCounter32Definition newArmMetricCounter32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricCounter32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricCounter64Definition newArmMetricCounter64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricCounter64Definition(appDef, name, units, usage, id);
    }

    public ArmMetricCounterFloat32Definition newArmMetricCounterFloat32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricCounterFloat32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGauge32Definition newArmMetricGauge32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricGauge32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGauge64Definition newArmMetricGauge64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricGauge64Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGaugeFloat32Definition newArmMetricGaugeFloat32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricGaugeFloat32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricNumericId32Definition newArmMetricNumericId32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricNumericId32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricNumericId64Definition newArmMetricNumericId64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricNumericId64Definition(appDef, name, units, usage, id);
    }

    public ArmMetricString32Definition newArmMetricString32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTMetricString32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGroupDefinition newArmMetricGroupDefinition(ArmMetricDefinition[] definitions)
    {
        if (definitions == null) definitions = new ArmMetricDefinition[0];

        ArmMetricDefinition[] cleanDefinitions = new ArmMetricDefinition[7];

        System.arraycopy(definitions, 0, cleanDefinitions, 0, Math.min(definitions.length, 7));

        LTMetricGroupDefinition object = new LTMetricGroupDefinition(cleanDefinitions);

        for (int i = 0; i < 6; i++)
        {
            if (cleanDefinitions[i] != null && (cleanDefinitions[i] instanceof ArmMetricString32Definition || ((LTObject)cleanDefinitions[6]).isBad()))
            {
                StaticArmAPIMonitor.error(MetricErrorCodes.GRP_DEF_ARRAY_INVALID);
                break;
            }
        }

        if ((cleanDefinitions[6] != null && !(cleanDefinitions[6] instanceof ArmMetricString32Definition) || ((LTObject)cleanDefinitions[6]).isBad()))
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.GRP_DEF_ARRAY_INVALID);
        }

        return object;
    }

    public ArmTransactionWithMetricsDefinition newArmTransactionWithMetricsDefinition(ArmApplicationDefinition appDef, String name, ArmIdentityPropertiesTransaction identityProperties, ArmMetricGroupDefinition definition, ArmID id)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        name = ArmAPIUtil.checkRequired(name);
        identityProperties = ArmAPIUtil.checkOptional(identityProperties);
        definition = ArmAPIUtil.checkOptional(definition);
        id = ArmAPIUtil.checkOptional(id);

        return new LTTransactionWithMetricsDefinition(appDef, name, identityProperties, definition, id);
    }

    public ArmMetricCounter32 newArmMetricCounter32(ArmMetricCounter32Definition definition)
    {
        return new LTMetricCounter32(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricCounter64 newArmMetricCounter64(ArmMetricCounter64Definition definition)
    {
        return new LTMetricCounter64(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricCounterFloat32 newArmMetricCounterFloat32(ArmMetricCounterFloat32Definition definition)
    {
        return new LTMetricCounterFloat32(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricGauge32 newArmMetricGauge32(ArmMetricGauge32Definition definition)
    {
        return new LTMetricGauge32(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricGauge64 newArmMetricGauge64(ArmMetricGauge64Definition definition)
    {
        return new LTMetricGauge64(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricGaugeFloat32 newArmMetricGaugeFloat32(ArmMetricGaugeFloat32Definition definition)
    {
        return new LTMetricGaugeFloat32(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricNumericId32 newArmMetricNumericId32(ArmMetricNumericId32Definition definition)
    {
        return new LTMetricNumericId32(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricNumericId64 newArmMetricNumericId64(ArmMetricNumericId64Definition definition)
    {
        return new LTMetricNumericId64(ArmAPIUtil.checkRequired(definition));
    }

    public ArmMetricString32 newArmMetricString32(ArmMetricString32Definition definition)
    {
        return new LTMetricString32(ArmAPIUtil.checkRequired(definition));
    }

    public synchronized ArmMetricGroup newArmMetricGroup(ArmMetricGroupDefinition groupDefinition, ArmMetric[] metrics)
    {
        if (metrics == null) metrics = new ArmMetric[0];

        LTAbstractMetricBase[] cleanMetrics = new LTAbstractMetricBase[7];

        System.arraycopy(metrics, 0, cleanMetrics, 0, Math.min(metrics.length, 7));

        LTMetricGroup object = new LTMetricGroup(groupDefinition, cleanMetrics);

        for (int i = 0; i < 6; i++)
        {
            if ((cleanMetrics[i] != null && (cleanMetrics[i] instanceof ArmMetricString32) || ((LTObject)cleanMetrics[i]).isBad()))
            {
                StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GRP_ARRAY_INVALID);
            }
        }

        if ((cleanMetrics[6] != null && !(cleanMetrics[6] instanceof ArmMetricString32) || ((LTObject)cleanMetrics[6]).isBad()))
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GRP_ARRAY_INVALID);
        }

        return object;
    }

    public ArmTranReportWithMetrics newArmTranReportWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        app = ArmAPIUtil.checkRequired(app);
        definition = ArmAPIUtil.checkRequired(definition);
        group = ArmAPIUtil.checkRequired(group);

        return new LTTranReportWithMetrics(app, definition, group);
    }

    public ArmTransactionWithMetrics newArmTransactionWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        app = ArmAPIUtil.checkRequired(app);
        definition = ArmAPIUtil.checkRequired(definition);
        group = ArmAPIUtil.checkRequired(group);

        return new LTTransactionWithMetrics(app, definition, group);
    }
}
