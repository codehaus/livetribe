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

import java.util.Arrays;

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricGroup;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.metric.ArmTransactionWithMetricsDefinition;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.LTObject;
import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
class ArmAPIUtil implements GeneralErrorCodes
{
    public static final LTApplicationDefinition BAD_APP_DEF;
    public static final LTAbstractMetricDefinition BAD_METRIC_DEF;
    public static final LTApplication BAD_APP;
    public static final LTMetricGroupDefinition BAD_METRIC_GRP_DEF;
    public static final LTTransactionWithMetricsDefinition BAD_TRANS_W_METRICS_DEF;
    public static final LTAbstractMetricBase BAD_METRIC;
    public static final LTAbstractMetricBase[] BAD_METRICS;
    public static final LTMetricGroup BAD_METRIC_GROUP;
    public static final LTIdentityPropertiesTransaction BAD_ID_PROPS_TRANS;
    public static final LTTransactionDefinition BAD_TRANS_DEF;

    static
    {
        BAD_APP_DEF = new LTApplicationDefinition("", null, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_METRIC_DEF = new LTAbstractMetricDefinition(BAD_APP_DEF, "", null, (short) 0, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_APP = new LTApplication(BAD_APP_DEF, "", null, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_METRIC_GRP_DEF = new LTMetricGroupDefinition(new ArmMetricDefinition[7])
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_TRANS_W_METRICS_DEF = new LTTransactionWithMetricsDefinition(BAD_APP_DEF, "", null, BAD_METRIC_GRP_DEF, null)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_METRIC = new LTAbstractMetricBase(BAD_METRIC_DEF)
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

        BAD_METRICS = new LTAbstractMetricBase[7];
        Arrays.fill(BAD_METRICS, BAD_METRIC);

        BAD_METRIC_GROUP = new LTMetricGroup(BAD_METRIC_GRP_DEF, BAD_METRICS)
        {
            public boolean isBad()
            {
                return true;
            }
        };

        BAD_ID_PROPS_TRANS = new LTIdentityPropertiesTransaction(null, null, null, null)
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

        BAD_TRANS_DEF = new LTTransactionDefinition(BAD_APP_DEF, "", BAD_ID_PROPS_TRANS, null)
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

    static ArmApplicationDefinition checkRequired(ArmApplicationDefinition appDef)
    {
        LTObject base = (LTObject) appDef;
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_DEFINIITON_NULL);
            base = BAD_APP_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return (ArmApplicationDefinition) base;
    }

    static ArmMetricDefinition checkRequired(ArmMetricDefinition metricDef)
    {
        LTObject base = (LTObject) metricDef;
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_DEFINIITON_NULL);
            base = BAD_METRIC_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return (ArmMetricDefinition) base;
    }

    static ArmApplication checkRequired(ArmApplication app)
    {
        LTObject base = (LTObject) app;
        if (app == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_NULL);
            base = BAD_APP;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return (ArmApplication) base;
    }

    static ArmTransactionDefinition checkRequired(ArmTransactionDefinition definition)
    {
        LTObject base = (LTObject) definition;
        if (base == null)
        {
            StaticArmAPIMonitor.error(TransactionErrorCodes.TRANS_DEF_NULL);
            base = BAD_TRANS_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return (ArmTransactionDefinition) base;
    }

    static ArmTransactionWithMetricsDefinition checkRequired(ArmTransactionWithMetricsDefinition definition)
    {
        LTObject base = (LTObject) definition;
        if (definition == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.TRN_W_METRICS_DEFINITION_NULL);
            base = BAD_TRANS_W_METRICS_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return (ArmTransactionWithMetricsDefinition) base;
    }

    static ArmMetricGroup checkRequired(ArmMetricGroup group)
    {
        LTObject base = (LTObject) group;
        if (group == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GROUP_NULL);
            base = BAD_METRIC_GROUP;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return (ArmMetricGroup) base;
    }

    static ArmID checkOptional(ArmID id)
    {
        LTObject base = (LTObject) id;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return id;
    }

    static ArmIdentityProperties checkOptional(ArmIdentityProperties id)
    {
        LTObject base = (LTObject) id;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return id;
    }

    static ArmIdentityPropertiesTransaction checkOptional(ArmIdentityPropertiesTransaction id)
    {
        LTObject base = (LTObject) id;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return id;
    }

    static ArmMetricGroupDefinition checkOptional(ArmMetricGroupDefinition metricGroupDef)
    {
        LTObject base = (LTObject) metricGroupDef;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
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
        LTObject base = (LTObject) systemAddress;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return systemAddress;
    }

    static String[] cleanIdProps(String[] idProps)
    {
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
}
