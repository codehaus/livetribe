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

import java.util.HashSet;
import java.util.Set;

import org.livetribe.totem.Message;


/**
 * @version $Revision: $ $Date: $
 */
public class ConfigurationChangeMessage implements Message
{
    private RingId ringId;
    private long sequenceId;
    private int configId;
    private final Set members = new HashSet();

    public RingId getRingId()
    {
        return ringId;
    }

    public void setRingId(RingId ringId)
    {
        this.ringId = ringId;
    }

    public long getSequenceId()
    {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId)
    {
        this.sequenceId = sequenceId;
    }

    public int getConfigId()
    {
        return configId;
    }

    public void setConfigId(int configId)
    {
        this.configId = configId;
    }

    public Set getMembers()
    {
        return members;
    }
}
