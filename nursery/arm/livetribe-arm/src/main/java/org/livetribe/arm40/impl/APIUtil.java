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
class APIUtil implements GeneralErrorCodes
{
    private static final LTApplicationDefinition BAD_APP_DEF;
    private static final AbstractMetricDefinition BAD_METRIC_DEF;
    private static final LTApplication BAD_APP;
    private static final LTMetricGroupDefinition BAD_METRIC_GRP_DEF;
    private static final LTTransactionWithMetricsDefinition BAD_TRANS_W_METRICS_DEF;
    private static final ArmMetric[] BAD_METRICS;
    private static final LTMetricGroup BAD_METRIC_GROUP;
    private static final LTIdentityPropertiesTransaction BAD_ID_PROPS_TRANS;
    private static final LTTransactionDefinition BAD_TRANS_DEF;

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

        BAD_ID_PROPS_TRANS = new LTIdentityPropertiesTransaction("org.livetribe.arm40.OID.BadIdentityPropertiesTransaction", cleanIdProps(null), cleanIdProps(null), cleanIdProps(null), null)
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

        BAD_TRANS_DEF = new LTTransactionDefinition("org.livetribe.arm40.OID.BadTransactionDefinition", BAD_APP_DEF, "", BAD_ID_PROPS_TRANS, null)
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
    }

    private APIUtil()
    {
    }

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

    static byte[] extractArmSystemAddress(ArmSystemAddress addr)
    {
        return (addr == null ? null : addr.getBytes());
    }

    static String extractURI(ArmIdentityPropertiesTransaction props)
    {
        return (props == null ? null : props.getURIValue());
    }

    static String extractOID(Identifiable object)
    {
        return (object == null ? null : object.getObjectId());
    }

    static String[] extractObjectIds(ArmMetricDefinition[] definitions)
    {
        String[] result = new String[7];

        for (int i = 0; i < 7; i++)
        {
            AbstractMetricDefinition def = (AbstractMetricDefinition) obtainTarget(definitions[i]);
            Identifiable appDef = (def != null ? (Identifiable) def.getAppDef() : null);
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
