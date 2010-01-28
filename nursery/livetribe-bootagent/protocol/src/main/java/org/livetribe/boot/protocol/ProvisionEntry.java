/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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


/**
 * A provision entry describes the name of the content and its version.  It
 * will be subsequently used to obtain content from a
 * <code>ContentProvider</code>.
 *
 * @version $Revision$ $Date$
 * @see ContentProvider
 */
public final class ProvisionEntry
{
    private final String name;
    private final long version;

    public ProvisionEntry(String name, long version)
    {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");

        this.name = name;
        this.version = version;
    }

    /**
     * The name of the content to be obtained from a <code>ContentProvider</code>.
     *
     * @return the name of the content to be obtained
     */
    public String getName()
    {
        return name;
    }

    /**
     * The version of the content to be obtained from a <code>ContentProvider</code>.
     *
     * @return the version of the content to be obtained
     */
    public long getVersion()
    {
        return version;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProvisionEntry that = (ProvisionEntry) o;

        return name.equals(that.name) && version == that.version;
    }

    @Override
    public int hashCode()
    {
        int result;
        result = name.hashCode();
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return name + ":" + version;
    }
}
