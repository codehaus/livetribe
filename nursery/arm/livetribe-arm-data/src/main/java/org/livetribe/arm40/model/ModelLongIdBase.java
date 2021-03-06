/**
 *
 * Copyright 2007 (C) The original author or authors
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

/**
 * @version $Revision$ $Date$
 */
public class ModelLongIdBase implements Model
{
    protected long oid;

    public long getOid()
    {
        return oid;
    }

    protected void setOid(long oid)
    {
        this.oid = oid;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ModelLongIdBase model = (ModelLongIdBase) o;

        return oid == model.oid;
    }

    public int hashCode()
    {
        return (int) (oid ^ (oid >>> 32));
    }
}
