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

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm40.connection.Connection;
import org.livetribe.arm40.connection.StaticThreadBindMonitor;
import org.livetribe.arm40.util.StaticArmAPIMonitor;
import org.livetribe.util.uuid.UUIDGen;


/**
 * @version $Revision: $ $Date: $
 */
class LTTransaction extends AbstractTransactionBase implements ArmTransaction
{
    private final Connection connection;
    private final UUIDGen guidGenerator;
    private final ArmApplication application;
    private final ArmTransactionDefinition definition;
    private ArmCorrelator parentCorrelator = null;
    private ArmCorrelator correlator;
    private int status = ArmConstants.STATUS_INVALID;
    private long start = 0;
    private long blocked = 1;
    private String contextURI;
    private final String[] contextValues = new String[20];
    private ArmUser user;
    private boolean trace = false;

    private State state;

    LTTransaction(String oid, Connection connection, UUIDGen guidGenerator, ArmApplication application, ArmTransactionDefinition definition)
    {
        super(oid);

        this.connection = connection;
        this.guidGenerator = guidGenerator;
        this.application = application;
        this.definition = definition;

        this.state = STOPPED;

        ((LTApplication) APIUtil.obtainTarget(application)).addApplicationLifecycleListener(this);
    }

    public synchronized void end()
    {
        state.reset();
        setBad(true);
    }

    public int bindThread()
    {
        StaticThreadBindMonitor.bind(correlator.getBytes());

        return GeneralErrorCodes.SUCCESS;
    }

    public synchronized long blocked()
    {
        return state.blocked();
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
        return (index < 20 ? contextValues[index] : null);
    }

    public ArmCorrelator getCorrelator()
    {
        return correlator;
    }

    public ArmCorrelator getParentCorrelator()
    {
        return parentCorrelator;
    }

    public int getStatus()
    {
        return status;
    }

    public ArmTransactionDefinition getDefinition()
    {
        return definition;
    }

    public ArmUser getUser()
    {
        return user;
    }

    public boolean isTraceRequested()
    {
        return trace;
    }

    public synchronized int reset()
    {
        state = state.reset();

        return GeneralErrorCodes.SUCCESS;
    }

    public synchronized int setArrivalTime()
    {
        state.setArrivalTime();

        return GeneralErrorCodes.SUCCESS;
    }

    public int setContextURIValue(String value)
    {
        contextURI = value;

        return GeneralErrorCodes.SUCCESS;
    }

    public int setContextValue(int index, String value)
    {
        if (index > 20) return StaticArmAPIMonitor.error(TransactionErrorCodes.INDEX_OUT_OF_RANGE);

        if (value != null)
        {
            int length = value.length();

            if (length == 0) value = null;
            if (length > 127) StaticArmAPIMonitor.warning(TransactionErrorCodes.ID_PROP_TOO_LONG);
            if (definition.getIdentityProperties().getContextName(index) != null)
            {
                contextValues[index] = value;
            }
            else
            {
                StaticArmAPIMonitor.warning(TransactionErrorCodes.ID_PROP_IGNORED);
            }
        }

        return GeneralErrorCodes.SUCCESS;
    }

    public int setTraceRequested(boolean traceState)
    {
        trace = traceState;

        return GeneralErrorCodes.SUCCESS;
    }

    public int setUser(ArmUser user)
    {
        this.user = user;

        return GeneralErrorCodes.SUCCESS;
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
        return start(APIUtil.newArmCorrelator(parentCorr, offset));
    }

    public synchronized int start(ArmCorrelator parent)
    {
        state = state.start(parent);

        return GeneralErrorCodes.SUCCESS;
    }

    public int stop(int status)
    {
        return stop(status, null);
    }

    public synchronized int stop(int code, String diagnosticDetail)
    {
        state = state.stop(code, diagnosticDetail);

        return GeneralErrorCodes.SUCCESS;
    }

    public synchronized int unbindThread()
    {
        StaticThreadBindMonitor.unbind(correlator.getBytes());

        return GeneralErrorCodes.SUCCESS;
    }

    public synchronized int unblocked(long handle)
    {
        state.unblocked(handle);

        return GeneralErrorCodes.SUCCESS;
    }

    public synchronized int update()
    {
        state.update();

        return GeneralErrorCodes.SUCCESS;
    }

    private abstract class State
    {
        abstract void setArrivalTime();

        abstract State start(ArmCorrelator parent);

        abstract State stop(int code, String diagnosticDetail);

        abstract void update();

        abstract long blocked();

        abstract void unblocked(long handle);

        abstract State reset();
    }

    private final State STOPPED = new State()
    {
        void setArrivalTime()
        {
            start = System.currentTimeMillis();
        }

        State start(ArmCorrelator parent)
        {
            correlator = APIUtil.constructArmCorrelator(guidGenerator.uuidgen(),
                                                        trace || parent.isAgentTrace() || parent.isApplicationTrace());
            if (start == 0) start = System.currentTimeMillis();
            parentCorrelator = parent;

            connection.start(getObjectId(),
                             correlator.getBytes(),
                             start,
                             (parent != null ? parent.getBytes() : null),
                             APIUtil.extractUser(user),
                             contextValues,
                             contextURI);

            return STARTED;
        }

        State stop(int code, String diagnosticDetail)
        {
            return this;
        }

        void update()
        {
        }

        long blocked()
        {
            return 0;
        }

        void unblocked(long handle)
        {
        }

        State reset()
        {
            connection.reset(getObjectId(), correlator.getBytes());

            start = 0;

            unbindThread();

            return this;
        }

        public String toString()
        {
            return "STOPPED";
        }
    };

    private final State STARTED = new State()
    {
        void setArrivalTime()
        {
        }

        State start(ArmCorrelator parent)
        {
            return this;
        }

        State stop(int code, String diagnosticDetail)
        {
            long end = System.currentTimeMillis();

            connection.stop(getObjectId(), correlator.getBytes(), end, code, diagnosticDetail);

            start = 0;

            if (blocked != 1)
            {
                StaticArmAPIMonitor.warning(TransactionErrorCodes.NOT_ALL_BLOCKS_REMOVED);
                blocked = 1;
            }

            unbindThread();

            status = code;

            return STOPPED;
        }

        void update()
        {
            connection.update(getObjectId(), correlator.getBytes(), System.currentTimeMillis());
        }

        long blocked()
        {
            long handle = blocked++;

            connection.block(getObjectId(), correlator.getBytes(), handle, System.currentTimeMillis());

            return handle;
        }

        void unblocked(long handle)
        {
            blocked--;
            connection.unblocked(getObjectId(), correlator.getBytes(), handle, System.currentTimeMillis());
        }

        State reset()
        {
            connection.reset(getObjectId(), correlator.getBytes());

            start = 0;
            blocked = 1;

            unbindThread();

            return STOPPED;
        }

        public String toString()
        {
            return "STARTED";
        }
    };
}
