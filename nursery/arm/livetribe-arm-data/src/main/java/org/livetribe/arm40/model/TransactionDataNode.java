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
package org.livetribe.arm40.model;

import java.util.List;


/**
 * @version $Revision$ $Date$
 */
public class TransactionDataNode extends ModelLongIdBase
{
    private Transaction transaction;
    private TransactionDataNode parent;
    private List childeren;
    private Correlator correlator;
    private String user;
    private String[] contextValues;
    private String contextURI;
    private int status;
    private String message;
    private long start;
    private long end;
    private boolean reset;
    private List blocks;
    private List updates;

    public Transaction getTransaction()
    {
        return transaction;
    }

    public void setTransaction(Transaction transaction)
    {
        this.transaction = transaction;
    }

    public TransactionDataNode getParent()
    {
        return parent;
    }

    public void setParent(TransactionDataNode parent)
    {
        this.parent = parent;
    }

    public List getChilderen()
    {
        return childeren;
    }

    public void setChilderen(List childeren)
    {
        this.childeren = childeren;
    }

    public Correlator getCorrelator()
    {
        return correlator;
    }

    public void setCorrelator(Correlator correlator)
    {
        this.correlator = correlator;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String[] getContextValues()
    {
        return contextValues;
    }

    public void setContextValues(String[] contextValues)
    {
        this.contextValues = contextValues;
    }

    public String getContextURI()
    {
        return contextURI;
    }

    public void setContextURI(String contextURI)
    {
        this.contextURI = contextURI;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public long getStart()
    {
        return start;
    }

    public void setStart(long start)
    {
        this.start = start;
    }

    public long getEnd()
    {
        return end;
    }

    public void setEnd(long end)
    {
        this.end = end;
    }

    public boolean isReset()
    {
        return reset;
    }

    public void setReset(boolean reset)
    {
        this.reset = reset;
    }

    public List getBlocks()
    {
        return blocks;
    }

    public void setBlocks(List blocks)
    {
        this.blocks = blocks;
    }

    public List getUpdates()
    {
        return updates;
    }

    public void setUpdates(List updates)
    {
        this.updates = updates;
    }
}
