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
package org.livetribe.arm40.impl;

import java.util.Arrays;

import org.livetribe.arm40.connection.DoNothingConnection;
import org.livetribe.arm40.util.StaticArmAPIMonitor;
import org.opengroup.arm40.metric.ArmMetric;
import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricGroup;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.metric.ArmTransactionWithMetricsDefinition;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;
import org.springframework.aop.framework.Advised;


/**
 * @version $Revision: $ $Date: $
 */
final class APIUtil implements GeneralErrorCodes
{
    static final LTApplicationDefinition BAD_APP_DEF;
    static final AbstractMetricDefinition BAD_METRIC_DEF;
    static final LTApplication BAD_APP;
    static final LTApplicationRemote BAD_APP_REMOTE;
    static final LTMetricGroupDefinition BAD_METRIC_GRP_DEF;
    static final LTTransactionWithMetricsDefinition BAD_TRANS_W_METRICS_DEF;
    static final ArmMetric[] BAD_METRICS;
    static final LTMetricGroup BAD_METRIC_GROUP;
    static final LTIdentityProperties BAD_ID_PROPS;
    static final LTIdentityPropertiesTransaction BAD_ID_PROPS_TRANS;
    static final LTTransactionDefinition BAD_TRANS_DEF;
    static final LTTransaction BAD_TRANS;
    static final LTTranReport BAD_TRAN_REPORT;
    static final LTMetricCounter32Definition BAD_COUNTER32_DEF;
    static final LTMetricCounter64Definition BAD_COUNTER64_DEF;
    static final LTMetricCounterFloat32Definition BAD_COUNTER_FLOAT32_DEF;
    static final LTMetricGauge32Definition BAD_GAUGE32_DEF;
    static final LTMetricGauge64Definition BAD_GAUGE64_DEF;
    static final LTMetricGaugeFloat32Definition BAD_GAUGE_FLOAT32_DEF;
    static final LTMetricNumericId32Definition BAD_NUMERICID32_DEF;
    static final LTMetricNumericId64Definition BAD_NUMERICID64_DEF;
    static final LTMetricString32Definition BAD_STRING32_DEF;
    static final LTMetricGroupDefinition BAD_METRIC_GROUP_DEF;
    static final LTTranReportWithMetrics BAD_TRAN_REPORT_W_METRICS;
    static final LTTransactionWithMetrics BAD_TRAN_W_METRICS;

    static
    {
        BAD_APP_DEF = new LTApplicationDefinition("org.livetribe.arm40.OID.BadApplicationDefinition", "", null, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_METRIC_DEF = new AbstractMetricDefinition("org.livetribe.arm40.OID.BadMetricDefinition", BAD_APP_DEF, "", null, (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_APP = new LTApplication("org.livetribe.arm40.OID.BadApplication", BAD_APP_DEF, "", null, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_APP_REMOTE = new LTApplicationRemote("org.livetribe.arm40.OID.BadApplicationRemote", BAD_APP_DEF, "", null, null, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_METRIC_GRP_DEF = new LTMetricGroupDefinition("org.livetribe.arm40.OID.BadMetricGroupDefinition", new ArmMetricDefinition[7])
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_TRANS_W_METRICS_DEF = new LTTransactionWithMetricsDefinition("org.livetribe.arm40.OID.BadTransactionWithMetricsDefinition", BAD_APP_DEF, "", null, BAD_METRIC_GRP_DEF, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        AbstractMetricBase BAD_METRIC = new AbstractMetricBase(BAD_METRIC_DEF)
        {
            public boolean isBad()
            {
                return true;
            }

            public boolean isValid()
            {
                return false;
            }

            public Object snapshot()
            {
                return null;
            }
        };

        BAD_METRICS = new ArmMetric[7];
        Arrays.fill(BAD_METRICS, BAD_METRIC);

        BAD_METRIC_GROUP = new LTMetricGroup("org.livetribe.arm40.OID.BadMetricGroup", BAD_METRIC_GRP_DEF, BAD_METRICS)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_ID_PROPS = new LTIdentityProperties("org.livetribe.arm40.OID.BadIdentityProperties", cleanIdProps(null), cleanIdProps(null), cleanIdProps(null))
        {
            public boolean isBad()
            {
                return true;
            }

            public boolean isValid()
            {
                return false;
            }
        };

        BAD_ID_PROPS_TRANS = new LTIdentityPropertiesTransaction("org.livetribe.arm40.OID.BadIdentityPropertiesTransaction", cleanIdProps(null), cleanIdProps(null), cleanIdProps(null), null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_TRANS_DEF = new LTTransactionDefinition("org.livetribe.arm40.OID.BadTransactionDefinition", BAD_APP_DEF, "", BAD_ID_PROPS_TRANS, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_TRANS = new LTTransaction("org.livetribe.arm40.OID.BadTransaction", new DoNothingConnection(), null, BAD_APP, BAD_TRANS_DEF)
        {
            public boolean isBad()
            {
                return true;
            }

            public synchronized void end()
            {
            }

            public int bindThread()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized long blocked()
            {
                return 0;
            }

            public String getContextURIValue()
            {
                return null;
            }

            public String getContextValue(int index)
            {
                return null;
            }

            public ArmCorrelator getCorrelator()
            {
                return null;
            }

            public ArmCorrelator getParentCorrelator()
            {
                return null;
            }

            public int getStatus()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public ArmUser getUser()
            {
                return null;
            }

            public boolean isTraceRequested()
            {
                return false;
            }

            public synchronized int reset()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int setArrivalTime()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextURIValue(String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextValue(int index, String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setTraceRequested(boolean traceState)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setUser(ArmUser user)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int start()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int start(byte[] parentCorr)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int start(byte[] parentCorr, int offset)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int start(ArmCorrelator parent)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int stop(int status)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int stop(int code, String diagnosticDetail)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int unbindThread()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int unblocked(long handle)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int update()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }
        };

        BAD_TRAN_REPORT = new LTTranReport("org.livetribe.arm40.OID.BadTransaction", new DoNothingConnection(), null, BAD_APP, BAD_TRANS_DEF)
        {
            public boolean isBad()
            {
                return true;
            }

            public ArmCorrelator generateCorrelator()
            {
                return null;
            }

            public String getContextURIValue()
            {
                return null;
            }

            public String getContextValue(int index)
            {
                return null;
            }

            public ArmCorrelator getCorrelator()
            {
                return null;
            }

            public ArmCorrelator getParentCorrelator()
            {
                return null;
            }

            public long getResponseTime()
            {
                return 0;
            }

            public int getStatus()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public ArmUser getUser()
            {
                return null;
            }

            public int report(int status, long respTime)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int report(int status, long respTime, long stopTime)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int report(int status, long respTime, String diagnosticDetail)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int report(int status, long respTime, long stopTime, String diagnosticDetail)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextURIValue(String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextValue(int index, String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setParentCorrelator(ArmCorrelator parent)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setUser(ArmUser user)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }
        };

        BAD_COUNTER32_DEF = new LTMetricCounter32Definition("org.livetribe.arm40.OID.BadMetricCounter32Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_COUNTER64_DEF = new LTMetricCounter64Definition("org.livetribe.arm40.OID.BadMetricCounter64Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_COUNTER_FLOAT32_DEF = new LTMetricCounterFloat32Definition("org.livetribe.arm40.OID.BadMetricCounterFloat32Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_GAUGE32_DEF = new LTMetricGauge32Definition("org.livetribe.arm40.OID.BadMetricGauge32Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_GAUGE64_DEF = new LTMetricGauge64Definition("org.livetribe.arm40.OID.BadMetricGauge64Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_GAUGE_FLOAT32_DEF = new LTMetricGaugeFloat32Definition("org.livetribe.arm40.OID.BadMetricGaugeFloat32Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_NUMERICID32_DEF = new LTMetricNumericId32Definition("org.livetribe.arm40.OID.BadMetricNumericId32Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_NUMERICID64_DEF = new LTMetricNumericId64Definition("org.livetribe.arm40.OID.BadMetricNumericId64Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_STRING32_DEF = new LTMetricString32Definition("org.livetribe.arm40.OID.BadMetricString32Definition", BAD_APP_DEF, "", "", (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_METRIC_GROUP_DEF = new LTMetricGroupDefinition("org.livetribe.arm40.OID.BadMetricGroupDefinition", null)
        {
            public boolean isBad()
            {
                return true;
            }

            public ArmMetricDefinition getMetricDefinition(int index)
            {
                return null;
            }
        };

        BAD_TRAN_REPORT_W_METRICS = new LTTranReportWithMetrics("org.livetribe.arm40.OID.BadTranReportWithMetrics", new DoNothingConnection(), null, BAD_APP, BAD_TRANS_W_METRICS_DEF, BAD_METRIC_GROUP)
        {
            public boolean isBad()
            {
                return true;
            }

            public ArmCorrelator generateCorrelator()
            {
                return null;
            }

            public String getContextURIValue()
            {
                return null;
            }

            public String getContextValue(int index)
            {
                return null;
            }

            public ArmCorrelator getCorrelator()
            {
                return null;
            }

            public ArmCorrelator getParentCorrelator()
            {
                return null;
            }

            public long getResponseTime()
            {
                return 0;
            }

            public int getStatus()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public ArmUser getUser()
            {
                return null;
            }

            public int report(int status, long respTime)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int report(int status, long respTime, long stopTime)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int report(int status, long respTime, String diagnosticDetail)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int report(int status, long respTime, long stopTime, String diagnosticDetail)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextURIValue(String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextValue(int index, String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setParentCorrelator(ArmCorrelator parent)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setUser(ArmUser user)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }
        };

        BAD_TRAN_W_METRICS = new LTTransactionWithMetrics("org.livetribe.arm40.OID.BadTransaction", new DoNothingConnection(), null, BAD_APP, BAD_TRANS_W_METRICS_DEF, BAD_METRIC_GROUP)
        {
            public boolean isBad()
            {
                return true;
            }

            public synchronized void end()
            {
            }

            public int bindThread()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized long blocked()
            {
                return 0;
            }

            public String getContextURIValue()
            {
                return null;
            }

            public String getContextValue(int index)
            {
                return null;
            }

            public ArmCorrelator getCorrelator()
            {
                return null;
            }

            public ArmCorrelator getParentCorrelator()
            {
                return null;
            }

            public int getStatus()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public ArmUser getUser()
            {
                return null;
            }

            public boolean isTraceRequested()
            {
                return false;
            }

            public synchronized int reset()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int setArrivalTime()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextURIValue(String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setContextValue(int index, String value)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setTraceRequested(boolean traceState)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int setUser(ArmUser user)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int start()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int start(byte[] parentCorr)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int start(byte[] parentCorr, int offset)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int start(ArmCorrelator parent)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public int stop(int status)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int stop(int code, String diagnosticDetail)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int unbindThread()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int unblocked(long handle)
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }

            public synchronized int update()
            {
                return GeneralErrorCodes.USING_INVALID_OBJECT;
            }
        };

    }

    private APIUtil()
    {
    }

    /**
     * If the object is an instance of an AOP proxy then obtain its target.
     *
     * @param object the possible AOP proxy
     * @return the target of the AOP proxy or the object itself
     */
    static Object obtainTarget(Object object)
    {
        try
        {
            return (object instanceof Advised ? ((Advised) object).getTargetSource().getTarget() : object);
        }
        catch (Exception e)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.ERROR_OBTAINING_TARGET);
            return object;
        }
    }

    static String checkRequiredName(String name)
    {
        if (name == null || name.length() == 0)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.NAME_NULL_OR_EMPTY);
            return "";
        }

        if (name.length() > 127)
        {
            StaticArmAPIMonitor.warning(GeneralErrorCodes.NAME_LENGTH_LARGE);
        }

        return name;
    }

    static ArmMetricGroupDefinition checkRequired(ArmMetricGroupDefinition metricGroupDef)
    {
        AbstractObject base = (AbstractObject) obtainTarget(metricGroupDef);
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_DEFINIITON_NULL);
            metricGroupDef = BAD_METRIC_GRP_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return metricGroupDef;
    }

    static ArmApplicationDefinition checkRequired(ArmApplicationDefinition appDef)
    {
        AbstractObject base = (AbstractObject) obtainTarget(appDef);
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_DEFINIITON_NULL);
            appDef = BAD_APP_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return appDef;
    }

    static ArmMetricDefinition checkRequired(ArmMetricDefinition metricDef)
    {
        AbstractObject base = (AbstractObject) obtainTarget(metricDef);
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_DEFINIITON_NULL);
            metricDef = BAD_METRIC_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return metricDef;
    }

    static ArmApplication checkRequired(ArmApplication app)
    {
        AbstractObject base = (AbstractObject) obtainTarget(app);
        if (app == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_NULL);
            app = BAD_APP;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return app;
    }

    static ArmTransactionDefinition checkRequired(ArmTransactionDefinition definition)
    {
        AbstractObject base = (AbstractObject) obtainTarget(definition);
        if (base == null)
        {
            StaticArmAPIMonitor.error(TransactionErrorCodes.TRANS_DEF_NULL);
            definition = BAD_TRANS_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return definition;
    }

    static ArmTransactionWithMetricsDefinition checkRequired(ArmTransactionWithMetricsDefinition definition)
    {
        AbstractObject base = (AbstractObject) obtainTarget(definition);
        if (definition == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.TRN_W_METRICS_DEFINITION_NULL);
            definition = BAD_TRANS_W_METRICS_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return definition;
    }

    static ArmMetricGroup checkRequired(ArmMetricGroup group)
    {
        AbstractObject base = (AbstractObject) obtainTarget(group);
        if (group == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GROUP_NULL);
            group = BAD_METRIC_GROUP;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return group;
    }

    static void checkBadStatus(Object object)
    {
        AbstractObject base = (AbstractObject) obtainTarget(object);
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
    }

    static ArmID checkOptional(ArmID id)
    {
        checkBadStatus(id);
        return id;
    }

    static ArmIdentityProperties checkOptional(ArmIdentityProperties id)
    {
        checkBadStatus(id);
        return id;
    }

    static ArmIdentityPropertiesTransaction checkOptional(ArmIdentityPropertiesTransaction id)
    {
        checkBadStatus(id);
        return id;
    }

    static ArmMetricGroupDefinition checkOptional(ArmMetricGroupDefinition metricGroupDef)
    {
        checkBadStatus(metricGroupDef);
        return metricGroupDef;
    }

    static String checkOptional255(String name)
    {
        if (name != null && name.length() > 255)
        {
            StaticArmAPIMonitor.warning(GeneralErrorCodes.GROUP_LENGTH_LARGE);
        }
        return name;
    }

    static String[] checkOptional(ArmApplicationDefinition appDef, String[] contextValues)
    {
        if (contextValues == null) contextValues = new String[0];

        if (contextValues.length > 20) StaticArmAPIMonitor.warning(GeneralErrorCodes.CTX_VAL_ARRAY_LONG);

        String[] cleanValues = new String[20];

        ArmIdentityProperties props = appDef.getIdentityProperties();
        if (props != null)
        {
            System.arraycopy(contextValues, 0, cleanValues, 0, Math.min(contextValues.length, 20));

            for (int i = 0; i < 20; i++)
            {
                if (props.getContextName(i) == null) cleanValues[i] = null;
            }
        }

        return cleanValues;
    }

    static ArmSystemAddress checkOptional(ArmSystemAddress systemAddress)
    {
        checkBadStatus(systemAddress);
        return systemAddress;
    }

    static String[] cleanIdProps(String[] idProps)
    {
        if (idProps == null) idProps = new String[0];

        String[] cleanProps = new String[20];
        System.arraycopy(idProps, 0, cleanProps, 0, Math.min(20, idProps.length));

        for (int i = 0; i < 20; i++)
        {
            if (cleanProps[i] != null && cleanProps[i].length() > 127)
            {
                StaticArmAPIMonitor.warning(TransactionErrorCodes.ID_PROP_TOO_LONG);
            }
        }

        return cleanProps;
    }

    static ArmCorrelator constructArmCorrelator(byte[] uuid, boolean trace)
    {
        int length = uuid.length + 4;
        byte[] cleanBytes = new byte[length];

        cleanBytes[0] = (byte) (length >> 8);
        cleanBytes[1] = (byte) (length);
        if (trace) cleanBytes[3] |= 0xC0;

        System.arraycopy(uuid, 0, cleanBytes, 4, uuid.length);

        return new LTCorrelator(cleanBytes);
    }

    static ArmCorrelator newArmCorrelator(byte[] corrBytes)
    {
        return newArmCorrelator(corrBytes, 0);
    }

    static ArmCorrelator newArmCorrelator(byte[] corrBytes, int offset)
    {
        if (corrBytes == null || corrBytes.length < offset + 4)
        {
            if (corrBytes != null) StaticArmAPIMonitor.error(TransactionErrorCodes.TOKEN_TOO_SHORT);

            offset = 0;
            corrBytes = new byte[4];
            corrBytes[1] = 4;
        }

        int length = corrBytes[offset] << 8 | corrBytes[offset + 1];

        if (corrBytes.length < offset + length || length > ArmConstants.CORR_MAX_LENGTH)
        {
            length = 0;
            StaticArmAPIMonitor.error(TransactionErrorCodes.TOKEN_TOO_LONG);
        }

        byte[] cleanBytes = new byte[length];
        System.arraycopy(corrBytes, offset, cleanBytes, 0, length);

        return new LTCorrelator(cleanBytes);
    }

    static String[] extractIdNames(ArmIdentityProperties props)
    {
        if (props == null) return null;

        String[] result = new String[20];

        for (int i = 0; i < 20; i++) result[i] = props.getIdentityName(i);

        return result;
    }

    static String[] extractIdValues(ArmIdentityProperties props)
    {
        if (props == null) return null;

        String[] result = new String[20];

        for (int i = 0; i < 20; i++) result[i] = props.getIdentityValue(i);

        return result;
    }

    static String[] extractCtxNames(ArmIdentityProperties props)
    {
        if (props == null) return null;

        String[] result = new String[20];

        for (int i = 0; i < 20; i++) result[i] = props.getContextName(i);

        return result;
    }

    static byte[] extractArmId(ArmID id)
    {
        return (id == null ? null : id.getBytes());
    }

    /**
     * A simple convenience function which obtains the bytes of the system
     * address or null, if a null is passed.
     *
     * @param addr the <code>ArmSystemAddress</code> whose bytes are to be
     *             obtained.
     * @return the bytes of the system address.
     */
    static byte[] extractArmSystemAddress(ArmSystemAddress addr)
    {
        return (addr == null ? null : addr.getBytes());
    }

    static String extractURI(ArmIdentityPropertiesTransaction props)
    {
        return (props == null ? null : props.getURIValue());
    }

    /**
     * Extract the object id from the object by first obtaining the target
     * object since the <code>getObjectId()</code> method is not member of an
     * interface that is exposed by the AOP proxy.
     *
     * @param object the object whose OID is to be obtained
     * @return the object id of the object
     */
    static String extractOID(Object object)
    {
        return (object == null ? null : ((AbstractIdentifiableObject) APIUtil.obtainTarget(object)).getObjectId());
    }

    static String[] extractObjectIds(ArmMetricDefinition[] definitions)
    {
        String[] result = new String[7];

        for (int i = 0; i < 7; i++)
        {
            AbstractMetricDefinition def = (AbstractMetricDefinition) obtainTarget(definitions[i]);
            AbstractIdentifiableObject appDef = (def != null ? (AbstractIdentifiableObject) obtainTarget(def.getAppDef()) : null);
            result[i] = (appDef != null ? appDef.getObjectId() : null);
        }

        return result;
    }

    static String[] extractNames(ArmMetricDefinition[] definitions)
    {
        String[] result = new String[7];

        for (int i = 0; i < 7; i++)
        {
            ArmMetricDefinition def = definitions[i];
            result[i] = (def != null ? def.getName() : null);
        }

        return result;
    }

    static String[] extractUnits(ArmMetricDefinition[] definitions)
    {
        String[] result = new String[7];

        for (int i = 0; i < 7; i++)
        {
            ArmMetricDefinition def = definitions[i];
            result[i] = (def != null ? def.getUnits() : null);
        }

        return result;
    }

    static short[] extractUsage(ArmMetricDefinition[] definitions)
    {
        short[] result = new short[7];

        for (int i = 0; i < 7; i++)
        {
            ArmMetricDefinition def = definitions[i];
            result[i] = (def != null ? def.getUsage() : 0);
        }

        return result;
    }

    static byte[][] extractIds(ArmMetricDefinition[] definitions)
    {
        byte[][] result = new byte[7][];

        for (int i = 0; i < 7; i++)
        {
            ArmMetricDefinition def = definitions[i];
            ArmID id = (def != null ? def.getID() : null);
            result[i] = (id != null ? id.getBytes() : null);
        }

        return result;
    }

    static String extractUser(ArmUser user)
    {
        return (user != null ? user.toString() : null);
    }
}

