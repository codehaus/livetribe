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

import org.activeio.Packet;

import org.livetribe.totem.Identifier;
import org.livetribe.totem.Message;


/**
 * @version $Revision: $ $Date: $
 */
public class RegularMessage implements Message, Comparable
{
    private Identifier senderId;
    private RingId ringId;
    private long sequenceId;
    private int configId;
    private Packet packet;

    public Identifier getSenderId()
    {
        return senderId;
    }

    public void setSenderId(Identifier senderId)
    {
        this.senderId = senderId;
    }

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

    public Packet getPacket()
    {
        return packet;
    }

    public void setPacket(Packet packet)
    {
        this.packet = packet;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RegularMessage that = (RegularMessage) o;

        if (configId != that.configId) return false;
        if (sequenceId != that.sequenceId) return false;
        if (!ringId.equals(that.ringId)) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = ringId.hashCode();
        result = 29 * result + (int) (sequenceId ^ (sequenceId >>> 32));
        result = 29 * result + configId;
        return result;
    }

    public int compareTo(Object o)
    {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return 1;

        final RegularMessage that = (RegularMessage) o;

        if (sequenceId < that.sequenceId) return -1;
        if (sequenceId > that.sequenceId) return 1;
        return 0;
    }
}
