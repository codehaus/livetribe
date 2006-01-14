/**
 *
 * Copyright 2005 (C) The original author or authors.
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
package org.livetribe.totem.single;

import java.io.IOException;

import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledFuture;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import org.activeio.AsyncChannel;


/**
 * @version $Revision: $ $Date: $
 */
abstract class SingleRingChannelBase implements AsyncChannel
{
    private final ScheduledExecutorService clockDaemon;
    private ScheduledFuture tokenLossTaskID;
    private ScheduledFuture tokenRetransTaskID;
    private long tokenLossTimeout;
    private long tokenRetransTimeout;
    private ScheduledFuture joinTaskID;
    private ScheduledFuture consensusTaskID;
    private long joinTimeout;
    private long consensusTimeout;

    protected State state;
    protected final Object STATE_SEMAPHORE = new Object();

    SingleRingChannelBase(ScheduledExecutorService clockDaemon)
    {
        this.clockDaemon = clockDaemon;
    }

    public long getTokenLossTimeout()
    {
        return tokenLossTimeout;
    }

    public void setTokenLossTimeout(long tokenLossTimeout)
    {
        this.tokenLossTimeout = tokenLossTimeout;
    }

    public long getTokenRetransTimeout()
    {
        return tokenRetransTimeout;
    }

    public void setTokenRetransTimeout(long tokenRetransTimeout)
    {
        this.tokenRetransTimeout = tokenRetransTimeout;
    }

    public long getJoinTimeout()
    {
        return joinTimeout;
    }

    public void setJoinTimeout(long joinTimeout)
    {
        this.joinTimeout = joinTimeout;
    }

    public long getConsensusTimeout()
    {
        return consensusTimeout;
    }

    public void setConsensusTimeout(long consensusTimeout)
    {
        this.consensusTimeout = consensusTimeout;
    }

    protected void startTokenLossTimeout()
    {
        tokenLossTaskID.cancel(false);
        tokenLossTaskID = clockDaemon.schedule(tokenLossRunnable, tokenLossTimeout, TimeUnit.MILLISECONDS);
    }

    protected void resetTokenLossTimeout()
    {
        cancelTokenLossTimeout();
        tokenLossTaskID = clockDaemon.schedule(tokenLossRunnable, tokenLossTimeout, TimeUnit.MILLISECONDS);
    }

    protected void cancelTokenLossTimeout()
    {
        if (tokenLossTaskID != null) tokenLossTaskID.cancel(false);
    }

    protected void startTokenRetransTimeout()
    {
        tokenRetransTaskID = clockDaemon.schedule(tokenRetransRunnable, tokenRetransTimeout, TimeUnit.MILLISECONDS);
    }

    protected void resetTokenRetransTimeout()
    {
        cancelTokenRetransTimeout();
        tokenRetransTaskID = clockDaemon.schedule(tokenRetransRunnable, tokenRetransTimeout, TimeUnit.MILLISECONDS);
    }

    protected void cancelTokenRetransTimeout()
    {
        if (tokenRetransTaskID != null) tokenRetransTaskID.cancel(false);
    }

    protected void startJoinTimeout()
    {
        joinTaskID = clockDaemon.schedule(joinRunnable, joinTimeout, TimeUnit.MILLISECONDS);
    }

    protected void resetJoinTimeout()
    {
        cancelJoinTimeout();
        joinTaskID = clockDaemon.schedule(joinRunnable, joinTimeout, TimeUnit.MILLISECONDS);
    }

    protected void cancelJoinTimeout()
    {
        if (joinTaskID != null) joinTaskID.cancel(false);
    }

    protected void startConsensusTimeout()
    {
        consensusTaskID = clockDaemon.schedule(consensusRunnable, consensusTimeout, TimeUnit.MILLISECONDS);
    }

    protected void resetConsensusTimeout()
    {
        cancelJoinTimeout();
        consensusTaskID = clockDaemon.schedule(consensusRunnable, consensusTimeout, TimeUnit.MILLISECONDS);
    }

    protected void cancelConsensusTimeout()
    {
        if (consensusTaskID != null) consensusTaskID.cancel(false);
    }

    private Runnable tokenLossRunnable = new Runnable()
    {
        public void run()
        {
            try
            {
                synchronized (STATE_SEMAPHORE)
                {
                    state = state.tokenLossTimeout();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
            }
        }
    };

    private Runnable tokenRetransRunnable = new Runnable()
    {
        public void run()
        {
            try
            {
                synchronized (STATE_SEMAPHORE)
                {
                    state = state.tokenRetransmissionTimeout();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
            }
        }
    };

    private Runnable joinRunnable = new Runnable()
    {
        public void run()
        {
            try
            {
                synchronized (STATE_SEMAPHORE)
                {
                    state = state.joinTimeout();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
            }
        }
    };

    private Runnable consensusRunnable = new Runnable()
    {
        public void run()
        {
            try
            {
                synchronized (STATE_SEMAPHORE)
                {
                    state = state.consensusTimeout();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
            }
        }
    };

    abstract class State
    {
        abstract State process(RegularToken token) throws IOException;

        abstract State process(RegularMessage message) throws IOException;

        abstract State tokenLossTimeout() throws IOException;

        abstract State tokenRetransmissionTimeout() throws IOException;

        abstract State processForeign(RegularMessage message) throws IOException;

        abstract State process(JoinMessage message) throws IOException;

        abstract State process(CommitToken token) throws IOException;

        abstract State joinTimeout() throws IOException;

        abstract State consensusTimeout() throws IOException;

        final protected State myState()
        {
            return this;
        }
    }
}