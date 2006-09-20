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
package org.livetribe.arm.connection.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class BlockMessage implements Message
{
    private final String transOID;
    private final byte[] correlator;
    private final long handle;
    private final long ts;

    public BlockMessage(String transOID, byte[] correlator, long handle, long ts)
    {
        this.transOID = transOID;
        this.correlator = correlator;
        this.handle = handle;
        this.ts = ts;
    }

    public String getTransOID()
    {
        return transOID;
    }

    public byte[] getCorrelator()
    {
        return correlator;
    }

    public long getHandle()
    {
        return handle;
    }

    public long getTs()
    {
        return ts;
    }
}
