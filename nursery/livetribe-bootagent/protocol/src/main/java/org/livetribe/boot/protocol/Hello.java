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
public class Hello
{
    private final String uuid;
    private final long version;

    public Hello(String uuid, long version)
    {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");

        this.uuid = uuid;
        this.version = version;
    }

    public String getUuid()
    {
        return uuid;
    }

    public long getVersion()
    {
        return version;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hello hello = (Hello) o;

        return version == hello.version && uuid.equals(hello.uuid);
    }

    @Override
    public int hashCode()
    {
        int result = uuid.hashCode();
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "HELLO " + uuid + ":" + version;
    }
}
