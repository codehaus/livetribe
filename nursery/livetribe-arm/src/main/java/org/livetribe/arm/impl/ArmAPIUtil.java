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
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmIdentityProperties;

import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.LTAbstractObject;
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
    }

    static String checkRequired(String name)
    {
        if (name == null || name.length() == 0)
        {
            StaticArmAPIMonitor.error(GeneralErrorCodes.NAME_NULL_OR_EMPTY);
            return "";
        }

        if (name.length() > 127)
        {
            StaticArmAPIMonitor.warning(GeneralErrorCodes.NAME_LENGTH_LARGE);
            name = name.substring(0, 126);
        }

        return name;
    }

    static ArmApplicationDefinition checkRequired(ArmApplicationDefinition appDef)
    {
        LTApplicationDefinition base = (LTApplicationDefinition) appDef;
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_DEFINIITON_NULL);
            base = BAD_APP_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return base;
    }

    static ArmMetricDefinition checkRequired(ArmMetricDefinition metricDef)
    {
        LTAbstractMetricDefinition base = (LTAbstractMetricDefinition) metricDef;
        if (base == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_DEFINIITON_NULL);
            base = BAD_METRIC_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return base;
    }

    static ArmApplication checkRequired(ArmApplication app)
    {
        LTApplication base = (LTApplication) app;
        if (app == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.APPLICATION_NULL);
            base = BAD_APP;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return base;
    }

    static ArmTransactionWithMetricsDefinition checkRequired(ArmTransactionWithMetricsDefinition definition)
    {
        LTTransactionWithMetricsDefinition base = (LTTransactionWithMetricsDefinition) definition;
        if (definition == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.TRN_W_METRICS_DEFINITION_NULL);
            base = BAD_TRANS_W_METRICS_DEF;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return base;
    }

    static ArmMetricGroup checkRequired(ArmMetricGroup group)
    {
        LTMetricGroup base = (LTMetricGroup) group;
        if (group == null)
        {
            StaticArmAPIMonitor.error(MetricErrorCodes.METRIC_GROUP_NULL);
            base = BAD_METRIC_GROUP;
        }
        else
        {
            if (base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        }
        return base;
    }

    static ArmID checkOptional(ArmID id)
    {
        LTAbstractObject base = (LTAbstractObject) id;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return id;
    }

    static ArmIdentityProperties checkOptional(ArmIdentityProperties id)
    {
        LTAbstractObject base = (LTAbstractObject) id;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return id;
    }

    static ArmIdentityPropertiesTransaction checkOptional(ArmIdentityPropertiesTransaction id)
    {
        LTAbstractObject base = (LTAbstractObject) id;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return id;
    }

    static ArmMetricGroupDefinition checkOptional(ArmMetricGroupDefinition metricGroupDef)
    {
        LTAbstractObject base = (LTAbstractObject) metricGroupDef;
        if (base != null && base.isBad()) StaticArmAPIMonitor.error(GeneralErrorCodes.USING_INVALID_OBJECT);
        return metricGroupDef;
    }
}
