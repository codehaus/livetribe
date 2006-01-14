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
public class RingId implements Serializable
{
    private int incarnation;
    private Identifier representative;

    public RingId(int incarnation, Identifier representative)
    {
        if (representative == null) throw new IllegalArgumentException("representative cannot be null");
        if (incarnation < 0) throw new IllegalArgumentException("incarnation cannot be negative");

        this.incarnation = incarnation;
        this.representative = representative;
    }

    public int getIncarnation()
    {
        return incarnation;
    }

    public void setIncarnation(short incarnation)
    {
        this.incarnation = incarnation;
    }

    public Identifier getRepresentative()
    {
        return representative;
    }

    public void setRepresentative(Identifier representative)
    {
        if (representative == null) throw new IllegalArgumentException("representative cannot be null");

        this.representative = representative;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RingId ringId = (RingId) o;

        if (incarnation != ringId.incarnation) return false;
        if (!representative.equals(ringId.representative)) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (int) incarnation;
        result = 29 * result + representative.hashCode();
        return result;
    }
}
