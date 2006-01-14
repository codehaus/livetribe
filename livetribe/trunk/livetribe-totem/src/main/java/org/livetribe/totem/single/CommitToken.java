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

import java.util.ArrayList;
import java.util.List;

import org.livetribe.totem.Identifier;


/**
 * @version $Revision: $ $Date: $
 */
public class CommitToken implements Token
{
    private RingId ringId;
    private long tokenSequence;
    private long sequenceId;
    private long aru;
    private Identifier aruId;
    private final List members = new ArrayList();
    private short memberIndex;

    public RingId getRingId()
    {
        return ringId;
    }

    public void setRingId(RingId ringId)
    {
        this.ringId = ringId;
    }

    public long getTokenSequence()
    {
        return tokenSequence;
    }

    public void setTokenSequence(long tokenSequence)
    {
        this.tokenSequence = tokenSequence;
    }

    public long getSequenceId()
    {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId)
    {
        this.sequenceId = sequenceId;
    }

    public long getAru()
    {
        return aru;
    }

    public void setAru(long aru)
    {
        this.aru = aru;
    }

    public Identifier getAruId()
    {
        return aruId;
    }

    public void setAruId(Identifier aruId)
    {
        this.aruId = aruId;
    }

    public List getMembers()
    {
        return members;
    }

    public short getMemberIndex()
    {
        return memberIndex;
    }

    public void setMemberIndex(short memberIndex)
    {
        this.memberIndex = memberIndex;
    }
}
