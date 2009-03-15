/**
 *
 * Copyright 2007-2009 (C) The original author or authors
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
package org.livetribe.boot.protocol;

import net.jcip.annotations.Immutable;


/**
 * @version $Revision$ $Date$
 */
@Immutable
public class PleaseProvide
{
    private String name;
    private long version;

    public PleaseProvide(String name, long version)
    {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");

        this.name = name;
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getVersion()
    {
        return version;
    }

    public void setVersion(long version)
    {
        this.version = version;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PleaseProvide that = (PleaseProvide) o;

        return version == that.version && name.equals(that.name);
    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "PROVIDE " + name + ":" + version;
    }
}
