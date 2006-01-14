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

import java.io.Serializable;

import org.livetribe.totem.Identifier;


/**
 * @version $Revision: $ $Date: $
 */
class Member implements Serializable
{
    public Identifier myId;
    public RingId oldRingId;
    public long oldMyARU;
    public long highDelivered;
    public boolean received;

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Member member = (Member) o;

        if (!myId.equals(member.myId)) return false;

        return true;
    }

    public int hashCode()
    {
        return myId.hashCode();
    }
}
