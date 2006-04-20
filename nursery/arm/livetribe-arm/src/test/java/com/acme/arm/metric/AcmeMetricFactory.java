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
package com.acme.arm.metric;

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


/**
 * @version $Revision: $ $Date: $
 */
public class AcmeMetricFactory implements ArmMetricFactory
{
    public ArmMetricCounter32Definition newArmMetricCounter32Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricCounter64Definition newArmMetricCounter64Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricCounterFloat32Definition newArmMetricCounterFloat32Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricGauge32Definition newArmMetricGauge32Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricGauge64Definition newArmMetricGauge64Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricGaugeFloat32Definition newArmMetricGaugeFloat32Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricNumericId32Definition newArmMetricNumericId32Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricNumericId64Definition newArmMetricNumericId64Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricString32Definition newArmMetricString32Definition(ArmApplicationDefinition armApplicationDefinition, String string, String string1, short i, ArmID armID)
    {
        return null;
    }

    public ArmMetricGroupDefinition newArmMetricGroupDefinition(ArmMetricDefinition[] armMetricDefinitions)
    {
        return null;
    }

    public ArmTransactionWithMetricsDefinition newArmTransactionWithMetricsDefinition(ArmApplicationDefinition armApplicationDefinition, String string, ArmIdentityPropertiesTransaction armIdentityPropertiesTransaction, ArmMetricGroupDefinition armMetricGroupDefinition, ArmID armID)
    {
        return null;
    }

    public ArmMetricCounter32 newArmMetricCounter32(ArmMetricCounter32Definition armMetricCounter32Definition)
    {
        return null;
    }

    public ArmMetricCounter64 newArmMetricCounter64(ArmMetricCounter64Definition armMetricCounter64Definition)
    {
        return null;
    }

    public ArmMetricCounterFloat32 newArmMetricCounterFloat32(ArmMetricCounterFloat32Definition armMetricCounterFloat32Definition)
    {
        return null;
    }

    public ArmMetricGauge32 newArmMetricGauge32(ArmMetricGauge32Definition armMetricGauge32Definition)
    {
        return null;
    }

    public ArmMetricGauge64 newArmMetricGauge64(ArmMetricGauge64Definition armMetricGauge64Definition)
    {
        return null;
    }

    public ArmMetricGaugeFloat32 newArmMetricGaugeFloat32(ArmMetricGaugeFloat32Definition armMetricGaugeFloat32Definition)
    {
        return null;
    }

    public ArmMetricNumericId32 newArmMetricNumericId32(ArmMetricNumericId32Definition armMetricNumericId32Definition)
    {
        return null;
    }

    public ArmMetricNumericId64 newArmMetricNumericId64(ArmMetricNumericId64Definition armMetricNumericId64Definition)
    {
        return null;
    }

    public ArmMetricString32 newArmMetricString32(ArmMetricString32Definition armMetricString32Definition)
    {
        return null;
    }

    public ArmMetricGroup newArmMetricGroup(ArmMetricGroupDefinition armMetricGroupDefinition, ArmMetric[] armMetrics)
    {
        return null;
    }

    public ArmTranReportWithMetrics newArmTranReportWithMetrics(ArmApplication armApplication, ArmTransactionWithMetricsDefinition armTransactionWithMetricsDefinition, ArmMetricGroup armMetricGroup)
    {
        return null;
    }

    public ArmTransactionWithMetrics newArmTransactionWithMetrics(ArmApplication armApplication, ArmTransactionWithMetricsDefinition armTransactionWithMetricsDefinition, ArmMetricGroup armMetricGroup)
    {
        return null;
    }

    public boolean setErrorCallback(ArmErrorCallback armErrorCallback)
    {
        return false;
    }

    public int getErrorCode()
    {
        return 0;
    }

    public int setErrorCode(int i)
    {
        return 0;
    }

    public String getErrorMessage(int i)
    {
        return null;
    }
}
