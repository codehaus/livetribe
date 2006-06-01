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
package org.livetribe.arm.xbean;

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
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;

import org.livetribe.arm.ARMException;
import org.livetribe.arm.util.ARMUtil;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class MetricFactory implements ArmMetricFactory
{
    private final ArmMetricFactory base;

    public MetricFactory() throws ARMException
    {
        this.base = ARMUtil.createArmMetricFactory();
    }

    public ArmMetricCounter32Definition newArmMetricCounter32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricCounter32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricCounter64Definition newArmMetricCounter64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricCounter64Definition(appDef, name, units, usage, id);
    }

    public ArmMetricCounterFloat32Definition newArmMetricCounterFloat32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricCounterFloat32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGauge32Definition newArmMetricGauge32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricGauge32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGauge64Definition newArmMetricGauge64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricGauge64Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGaugeFloat32Definition newArmMetricGaugeFloat32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricGaugeFloat32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricNumericId32Definition newArmMetricNumericId32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricNumericId32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricNumericId64Definition newArmMetricNumericId64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricNumericId64Definition(appDef, name, units, usage, id);
    }

    public ArmMetricString32Definition newArmMetricString32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        return base.newArmMetricString32Definition(appDef, name, units, usage, id);
    }

    public ArmMetricGroupDefinition newArmMetricGroupDefinition(ArmMetricDefinition[] definitions)
    {
        return base.newArmMetricGroupDefinition(definitions);
    }

    public ArmTransactionWithMetricsDefinition newArmTransactionWithMetricsDefinition(ArmApplicationDefinition app, String name, ArmIdentityPropertiesTransaction identityProperties, ArmMetricGroupDefinition definition, ArmID id)
    {
        return base.newArmTransactionWithMetricsDefinition(app, name, identityProperties, definition, id);
    }

    public ArmMetricCounter32 newArmMetricCounter32(ArmMetricCounter32Definition definition)
    {
        return base.newArmMetricCounter32(definition);
    }

    public ArmMetricCounter64 newArmMetricCounter64(ArmMetricCounter64Definition definition)
    {
        return base.newArmMetricCounter64(definition);
    }

    public ArmMetricCounterFloat32 newArmMetricCounterFloat32(ArmMetricCounterFloat32Definition definition)
    {
        return base.newArmMetricCounterFloat32(definition);
    }

    public ArmMetricGauge32 newArmMetricGauge32(ArmMetricGauge32Definition definition)
    {
        return base.newArmMetricGauge32(definition);
    }

    public ArmMetricGauge64 newArmMetricGauge64(ArmMetricGauge64Definition definition)
    {
        return base.newArmMetricGauge64(definition);
    }

    public ArmMetricGaugeFloat32 newArmMetricGaugeFloat32(ArmMetricGaugeFloat32Definition definition)
    {
        return base.newArmMetricGaugeFloat32(definition);
    }

    public ArmMetricNumericId32 newArmMetricNumericId32(ArmMetricNumericId32Definition definition)
    {
        return base.newArmMetricNumericId32(definition);
    }

    public ArmMetricNumericId64 newArmMetricNumericId64(ArmMetricNumericId64Definition definition)
    {
        return base.newArmMetricNumericId64(definition);
    }

    public ArmMetricString32 newArmMetricString32(ArmMetricString32Definition definition)
    {
        return base.newArmMetricString32(definition);
    }

    public ArmMetricGroup newArmMetricGroup(ArmMetricGroupDefinition groupDefinition, ArmMetric[] metrics)
    {
        return base.newArmMetricGroup(groupDefinition, metrics);
    }

    public ArmTranReportWithMetrics newArmTranReportWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        return base.newArmTranReportWithMetrics(app, definition, group);
    }

    public ArmTransactionWithMetrics newArmTransactionWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        return base.newArmTransactionWithMetrics(app, definition, group);
    }

    public boolean setErrorCallback(ArmErrorCallback armErrorCallback)
    {
        return base.setErrorCallback(armErrorCallback);
    }

    public void setErrorListener(ArmErrorCallback armErrorCallback)
    {
        base.setErrorCallback(armErrorCallback);
    }

    public int getErrorCode()
    {
        return base.getErrorCode();
    }

    public int setErrorCode(int errorCode)
    {
        return base.setErrorCode(errorCode);
    }


    public String getErrorMessage(int i)
    {
        return base.getErrorMessage(i);
    }
}
