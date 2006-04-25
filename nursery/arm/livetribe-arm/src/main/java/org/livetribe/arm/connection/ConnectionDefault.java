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
package org.livetribe.arm.connection;

import org.opengroup.arm40.transaction.ArmUser;


/**
 * @version $Revision: $ $Date: $
 */
public class ConnectionDefault implements Connection
{
    public void introduceApplicationDefinition(byte[] appDefId, String name, String[] idNames, String[] idValues, String[] ctxNames, byte[] id)
    {
    }

    public void introduceApplication(byte[] appId, byte[] appDefId, String group, String instance, String[] contextValues)
    {
    }

    public void introduceApplicationRemote(byte[] appId, byte[] systemAddress)
    {
    }

    public void introduceTransactionDefinition(byte[] transDefId, String name, String[] idNames, String[] idValues, String[] ctxNames, String uri, byte[] id)
    {
    }

    public void introduceApplicationRemote(byte[] appId, byte[] appDefId, String group, String instance, String[] contextValues, byte[] systemAddress)
    {
    }

    public void associateTransaction(byte[] transId, byte [] appId, byte [] transDefId)
    {
    }

    public void start(byte[] transId, byte[] correlator, long start, byte[] parent, ArmUser user, String[] contextValues, String contextURI)
    {
    }

    public void update(byte[] transId, byte[] correlator)
    {
    }

    public void block(byte[] transId, byte[] correlator, long handle)
    {
    }

    public void unblocked(byte[] transId, byte[] correlator, long handle)
    {
    }

    public void stop(byte[] transId, byte[] correlator, long end, int status, String message)
    {
    }

    public void reset(byte[] transId, byte[] correlator)
    {
    }
}
