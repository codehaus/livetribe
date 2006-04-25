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

import org.opengroup.arm40.metric.ArmMetricGroup;
import org.opengroup.arm40.metric.ArmTransactionWithMetrics;
import org.opengroup.arm40.metric.ArmTransactionWithMetricsDefinition;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.AbstractIdentifiableObject;


/**
 * @version $Revision: $ $Date: $
 */
class LTTransactionWithMetrics extends AbstractIdentifiableObject implements ArmTransactionWithMetrics
{
    private final ArmApplication app;
    private final ArmTransactionWithMetricsDefinition definition;
    private final ArmMetricGroup group;

    public LTTransactionWithMetrics(ArmApplication app, ArmTransactionWithMetricsDefinition definition, ArmMetricGroup group)
    {
        this.app = app;
        this.definition = definition;
        this.group = group;
    }

    public ArmTransactionWithMetricsDefinition getTransactionWithMetricsDefinition()
    {
        return definition;
    }

    public ArmMetricGroup getMetricGroup()
    {
        return group;
    }

    public int bindThread()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public long blocked()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmApplication getApplication()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public String getContextURIValue()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public String getContextValue(int index)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmCorrelator getCorrelator()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmCorrelator getParentCorrelator()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int getStatus()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmTransactionDefinition getDefinition()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmUser getUser()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isTraceRequested()
    {
        return false;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int reset()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setArrivalTime()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setContextURIValue(String value)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setContextValue(int index, String value)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setTraceRequested(boolean traceState)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setUser(ArmUser user)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int start()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int start(byte[] parentCorr)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int start(byte[] parentCorr, int offset)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int start(ArmCorrelator parentCorr)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int stop(int status)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int stop(int status, String diagnosticDetail)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int unbindThread()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int unblocked(long blockHandle)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int update()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }
}
