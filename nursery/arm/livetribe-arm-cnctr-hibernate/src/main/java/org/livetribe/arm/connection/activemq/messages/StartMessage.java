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
package org.livetribe.arm.connection.activemq.messages;

import org.opengroup.arm40.transaction.ArmUser;

/**
 * @version $Revision$ $Date$
 */
public class StartMessage implements Message
{
    private final byte[] transId;
    private final byte[] correlator;
    private final long start;
    private final byte[] parent;
    private final ArmUser user;
    private final String[] contextValues;
    private final String contextURI;

    public StartMessage(byte[] transId, byte[] correlator, long start, byte[] parent, ArmUser user, String[] contextValues, String contextURI)
    {
        this.transId = transId;
        this.correlator = correlator;
        this.start = start;
        this.parent = parent;
        this.user = user;
        this.contextValues = contextValues;
        this.contextURI = contextURI;
    }

    public byte[] getTransId()
    {
        return transId;
    }

    public byte[] getCorrelator()
    {
        return correlator;
    }

    public long getStart()
    {
        return start;
    }

    public byte[] getParent()
    {
        return parent;
    }

    public ArmUser getUser()
    {
        return user;
    }

    public String[] getContextValues()
    {
        return contextValues;
    }

    public String getContextURI()
    {
        return contextURI;
    }
}
