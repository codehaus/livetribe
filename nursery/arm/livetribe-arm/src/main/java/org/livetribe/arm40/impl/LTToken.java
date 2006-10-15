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
package org.livetribe.arm40.impl;

import java.util.Arrays;

import org.opengroup.arm40.transaction.ArmToken;


/**
 * @version $Revision: $ $Date: $
 */
class LTToken extends AbstractObject implements ArmToken
{
    private final byte[] id;
    private transient int cachedHash = 0;
    protected byte one;
    protected byte two;

    LTToken(byte[] id)
    {
        assert id != null;

        this.id = id;
    }

    public boolean copyBytes(byte[] dest)
    {
        return copyBytes(dest, 0);
    }

    public boolean copyBytes(byte[] dest, int offset)
    {
        if (dest == null) return false;

        System.arraycopy(id, 0, dest, offset, Math.min(id.length, dest.length - offset));

        return (id.length <= dest.length - offset);
    }

    public byte[] getBytes()
    {
        byte[] result = new byte[getLength()];

        copyBytes(result);

        return result;
    }

    public int getLength()
    {
        return id.length;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LTToken ltToken = (LTToken) o;

        return Arrays.equals(id, ltToken.id);
    }

    public int hashCode()
    {
        int hash = cachedHash;
        if (hash == 0)
        {
            for (int i = 0; i < id.length; i++) hash = 29 * hash + id[i];
            cachedHash = hash;
        }
        return hash;
    }
}
