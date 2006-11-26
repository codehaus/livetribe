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
package org.livetribe.arm40.impl;

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

import org.livetribe.arm40.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class LTMetricFactoryImpl extends AbstractFactoryBase implements ArmMetricFactory, MetricErrorCodes
{
    public ArmMetricCounter32Definition newArmMetricCounter32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricCounter32Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricCounter32Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricCounter32Definition(definition.getObjectId(),
                                                             APIUtil.extractOID(appDef),
                                                             name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_COUNTER32_DEF;
        }

        return definition;
    }

    public ArmMetricCounter64Definition newArmMetricCounter64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricCounter64Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricCounter64Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricCounter64Definition(definition.getObjectId(),
                                                             APIUtil.extractOID(appDef),
                                                             name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_COUNTER64_DEF;
        }
        return definition;
    }

    public ArmMetricCounterFloat32Definition newArmMetricCounterFloat32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricCounterFloat32Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricCounterFloat32Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricCounterFloat32Definition(definition.getObjectId(),
                                                                  APIUtil.extractOID(appDef),
                                                                  name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_COUNTER_FLOAT32_DEF;
        }

        return definition;
    }

    public ArmMetricGauge32Definition newArmMetricGauge32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricGauge32Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricGauge32Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricGauge32Definition(definition.getObjectId(),
                                                           APIUtil.extractOID(appDef),
                                                           name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_GAUGE32_DEF;
        }

        return definition;
    }

    public ArmMetricGauge64Definition newArmMetricGauge64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricGauge64Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricGauge64Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricGauge64Definition(definition.getObjectId(),
                                                           APIUtil.extractOID(appDef),
                                                           name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_GAUGE64_DEF;
        }

        return definition;
    }

    public ArmMetricGaugeFloat32Definition newArmMetricGaugeFloat32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricGaugeFloat32Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricGaugeFloat32Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricGaugeFloat32Definition(definition.getObjectId(),
                                                                APIUtil.extractOID(appDef),
                                                                name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_GAUGE_FLOAT32_DEF;
        }

        return definition;
    }

    public ArmMetricNumericId32Definition newArmMetricNumericId32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricNumericId32Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricNumericId32Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricNumericId32Definition(definition.getObjectId(),
                                                               APIUtil.extractOID(appDef),
                                                               name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_NUMERICID32_DEF;
        }

        return definition;
    }

    public ArmMetricNumericId64Definition newArmMetricNumericId64Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricNumericId64Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricNumericId64Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricNumericId64Definition(definition.getObjectId(),
                                                               APIUtil.extractOID(appDef),
                                                               name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_NUMERICID64_DEF;
        }

        return definition;
    }

    public ArmMetricString32Definition newArmMetricString32Definition(ArmApplicationDefinition appDef, String name, String units, short usage, ArmID id)
    {
        LTMetricString32Definition definition;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            id = APIUtil.checkOptional(id);

            definition = new LTMetricString32Definition(allocateOID(), appDef, name, units, usage, id);

            getConnection().declareMetricString32Definition(definition.getObjectId(),
                                                            APIUtil.extractOID(appDef),
                                                            name, units, usage, APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            definition = APIUtil.BAD_STRING32_DEF;
        }

        return definition;
    }

    public ArmMetricGroupDefinition newArmMetricGroupDefinition(ArmMetricDefinition[] definitions)
    {
        LTMetricGroupDefinition metricGroupDef;
        try
        {
            if (definitions == null) definitions = new ArmMetricDefinition[0];

            ArmMetricDefinition[] cleanDefinitions = new ArmMetricDefinition[7];

            System.arraycopy(definitions, 0, cleanDefinitions, 0, Math.min(definitions.length, 7));

            metricGroupDef = new LTMetricGroupDefinition(allocateOID(), cleanDefinitions);

            for (int i = 0; i < 6; i++)
            {
                if (cleanDefinitions[i] != null && (cleanDefinitions[i] instanceof ArmMetricString32Definition || ((AbstractObject) APIUtil.obtainTarget(cleanDefinitions[i])).isBad()))
                {
                    StaticArmAPIMonitor.error(MetricErrorCodes.GRP_DEF_ARRAY_INVALID);
                    break;
                }
            }

            if (cleanDefinitions[6] != null && !(cleanDefinitions[6] instanceof ArmMetricString32Definition || ((AbstractObject) APIUtil.obtainTarget(cleanDefinitions[6])).isBad()))
            {
                StaticArmAPIMonitor.error(MetricErrorCodes.GRP_DEF_ARRAY_INVALID);
            }

            getConnection().declareMetricGroupDefinition(metricGroupDef.getObjectId(),
                                                         APIUtil.extractObjectIds(cleanDefinitions));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            metricGroupDef = APIUtil.BAD_METRIC_GROUP_DEF;
        }

        return metricGroupDef;
    }

    public ArmTransactionWithMetricsDefinition newArmTransactionWithMetricsDefinition(ArmApplicationDefinition appDef, String name, ArmIdentityPropertiesTransaction identityProperties, ArmMetricGroupDefinition metricGroupDef, ArmID id)
    {
        LTTransactionWithMetricsDefinition transDef;
        try
        {
            appDef = APIUtil.checkRequired(appDef);
            name = APIUtil.checkRequiredName(name);
            identityProperties = APIUtil.checkOptional(identityProperties);
            metricGroupDef = APIUtil.checkOptional(metricGroupDef);
            id = APIUtil.checkOptional(id);

            transDef = new LTTransactionWithMetricsDefinition(allocateOID(), appDef, name, identityProperties, metricGroupDef, id);
            getConnection().declareTransactionWithMetricsDefinition(transDef.getObjectId(),
                                                                    APIUtil.extractOID(appDef),
                                                                    name,
                                                                    APIUtil.extractOID(identityProperties),
                                                                    APIUtil.extractOID(metricGroupDef),
                                                                    APIUtil.extractArmId(id));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            transDef = APIUtil.BAD_TRANS_W_METRICS_DEF;
        }
        return transDef;
    }

    public ArmMetricCounter32 newArmMetricCounter32(ArmMetricCounter32Definition definition)
    {
        return new LTMetricCounter32(APIUtil.checkRequired(definition));
    }

    public ArmMetricCounter64 newArmMetricCounter64(ArmMetricCounter64Definition definition)
    {
        return new LTMetricCounter64(APIUtil.checkRequired(definition));
    }

    public ArmMetricCounterFloat32 newArmMetricCounterFloat32(ArmMetricCounterFloat32Definition definition)
    {
        return new LTMetricCounterFloat32(APIUtil.checkRequired(definition));
    }

    public ArmMetricGauge32 newArmMetricGauge32(ArmMetricGauge32Definition definition)
    {
        return new LTMetricGauge32(APIUtil.checkRequired(definition));
    }

    public ArmMetricGauge64 newArmMetricGauge64(ArmMetricGauge64Definition definition)
    {
        return new LTMetricGauge64(APIUtil.checkRequired(definition));
    }

    public ArmMetricGaugeFloat32 newArmMetricGaugeFloat32(ArmMetricGaugeFloat32Definition definition)
    {
        return new LTMetricGaugeFloat32(APIUtil.checkRequired(definition));
    }

    public ArmMetricNumericId32 newArmMetricNumericId32(ArmMetricNumericId32Definition definition)
    {
        return new LTMetricNumericId32(APIUtil.checkRequired(definition));
    }

    public ArmMetricNumericId64 newArmMetricNumericId64(ArmMetricNumericId64Definition definition)
    {
        return new LTMetricNumericId64(APIUtil.checkRequired(definition));
    }

    public ArmMetricString32 newArmMetricString32(ArmMetricString32Definition definition)
    {
        return new LTMetricString32(APIUtil.checkRequired(definition));
    }

    public synchronized ArmMetricGroup newArmMetricGroup(ArmMetricGroupDefinition metricGroupDef, ArmMetric[] metrics)
    {
        LTMetricGroup metricGroup;
        try
        {
            metricGroupDef = APIUtil.checkRequired(metricGroupDef);

            if (metrics == null) metrics = new ArmMetric[0];

            ArmMetric[] cleanMetrics = new ArmMetric[7];

            System.arraycopy(metrics, 0, cleanMetrics, 0, Math.min(metrics.length, 7));

            metricGroup = new LTMetricGroup(allocateOID(), metricGroupDef, cleanMetrics);

            for (int i = 0; i < 6; i++)
            {
                if (cleanMetrics[i] != null && (cleanMetrics[i] instanceof ArmMetricString32 || ((AbstractObject) APIUtil.obtainTarget(cleanMetrics[i])).isBad()))
                {
                    StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GRP_ARRAY_INVALID);
                }
            }

            if (cleanMetrics[6] != null && !(cleanMetrics[6] instanceof ArmMetricString32 || ((AbstractObject) APIUtil.obtainTarget(cleanMetrics[6])).isBad()))
            {
                StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GRP_ARRAY_INVALID);
            }

            getConnection().declareMetricGroup(metricGroup.getObjectId(),
                                               APIUtil.extractOID(metricGroupDef));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            metricGroup = APIUtil.BAD_METRIC_GROUP;
        }

        return metricGroup;
    }

    public ArmTranReportWithMetrics newArmTranReportWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition tranReportMetricsDef, ArmMetricGroup metricGroup)
    {
        LTTranReportWithMetrics tranReportMetrics;
        try
        {
            app = APIUtil.checkRequired(app);
            tranReportMetricsDef = APIUtil.checkRequired(tranReportMetricsDef);
            metricGroup = APIUtil.checkRequired(metricGroup);

            tranReportMetrics = new LTTranReportWithMetrics(allocateOID(),
                                                            getConnection(), getGuidGenerator(),
                                                            app, tranReportMetricsDef, metricGroup);

            getConnection().declareTranReportWithMetrics(tranReportMetrics.getObjectId(),
                                                         APIUtil.extractOID(tranReportMetricsDef),
                                                         APIUtil.extractOID(metricGroup));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            tranReportMetrics = APIUtil.BAD_TRAN_REPORT_W_METRICS;
        }

        return tranReportMetrics;
    }

    public ArmTransactionWithMetrics newArmTransactionWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition tranMetricsDef, ArmMetricGroup metricGroup)
    {
        LTTransactionWithMetrics tranMetrics;
        try
        {
            app = APIUtil.checkRequired(app);
            tranMetricsDef = APIUtil.checkRequired(tranMetricsDef);
            metricGroup = APIUtil.checkRequired(metricGroup);

            tranMetrics = new LTTransactionWithMetrics(allocateOID(), getConnection(), getGuidGenerator(), app, tranMetricsDef, metricGroup);

            getConnection().declareTranWithMetrics(tranMetrics.getObjectId(),
                                                   APIUtil.extractOID(metricGroup),
                                                   APIUtil.extractOID(metricGroup));
        }
        catch (Throwable t)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.UNEXPECTED_ERROR);
            tranMetrics = APIUtil.BAD_TRAN_W_METRICS;
        }

        return tranMetrics;
    }
}
