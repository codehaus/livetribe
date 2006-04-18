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

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.Factory;
import org.livetribe.arm.GeneralErrorCodes;
import org.livetribe.arm.LTAbstractObject;
import org.livetribe.arm.connection.Connection;


/**
 * @version $Revision: $ $Date: $
 */
class LTTransaction extends LTAbstractObject implements ArmTransaction
{
    private final ArmApplication application;
    private final ArmTransactionDefinition definition;
    private final Connection connection = Factory.getConnection();
    private ArmCorrelator parentCorrelator = ArmAPIUtil.newArmCorrelator(null);
    private ArmCorrelator correlator = ArmAPIUtil.newArmCorrelator(null);
    private long start = 0;
    private long blocked = 0;
    private String contextURI;
    private String[] contextValue = new String[20];

    LTTransaction(ArmApplication application, ArmTransactionDefinition definition)
    {
        this.application = application;
        this.definition = definition;
    }

    public int bindThread()
    {
        return GeneralErrorCodes.SUCCESS;
    }

    public long blocked()
    {
        long handle;

        synchronized (this)
        {
            handle = blocked++;
        }

        connection.block(correlator.getBytes(), handle);

        return handle;
    }

    public ArmApplication getApplication()
    {
        return application;
    }

    public String getContextURIValue()
    {
        return contextURI;
    }

    public String getContextValue(int index)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmCorrelator getCorrelator()
    {
        if (correlator == null) correlator = ArmAPIUtil.newArmCorrelator();
        return correlator;
    }

    public ArmCorrelator getParentCorrelator()
    {
        return parentCorrelator;
    }

    public int getStatus()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmTransactionDefinition getDefinition()
    {
        return definition;
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
        connection.reset(correlator.getBytes());

        clear();

        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setArrivalTime()
    {
        start = System.currentTimeMillis();

        return GeneralErrorCodes.SUCCESS;
    }

    public int setContextURIValue(String value)
    {
        contextURI = value;

        return GeneralErrorCodes.SUCCESS;
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
        return start((byte[]) null);
    }

    public int start(byte[] parentCorr)
    {
        return start(parentCorr, 0);
    }

    public int start(byte[] parentCorr, int offset)
    {
        return start(ArmAPIUtil.newArmCorrelator(parentCorr, offset));
    }

    public int start(ArmCorrelator parent)
    {
        correlator = ArmAPIUtil.newArmCorrelator();
        if (start == 0) start = System.currentTimeMillis();
        parentCorrelator = parent;

        connection.start(correlator.getBytes(), start, parent.getBytes());

        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int stop(int status)
    {
        return stop(status, null);
    }

    public int stop(int status, String diagnosticDetail)
    {
        long end = System.currentTimeMillis();

        connection.stop(correlator.getBytes(), end, status, diagnosticDetail);

        clear();

        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int unbindThread()
    {
        return GeneralErrorCodes.SUCCESS;
    }

    public int unblocked(long handle)
    {
        connection.block(correlator.getBytes(), handle);

        return GeneralErrorCodes.SUCCESS;
    }

    public int update()
    {
        connection.update(correlator.getBytes());

        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    protected void clear()
    {
        start = 0;
        Arrays.fill(contextValue, null);
    }
}
