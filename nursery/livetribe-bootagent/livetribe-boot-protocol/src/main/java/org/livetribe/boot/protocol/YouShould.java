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
package org.livetribe.boot.protocol;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


/**
 * @version $Revision$ $Date$
 */
public class YouShould
{
    private final long version;
    private final String bootClass;
    private final Set<ProvisionEntry> entries;

    public YouShould(long version, String bootClass, Set<ProvisionEntry> entries)
    {
        this.version = version;
        this.bootClass = bootClass;
        this.entries = Collections.unmodifiableSet(new HashSet<ProvisionEntry>(entries));
    }

    public long getVersion()
    {
        return version;
    }

    public String getBootClass()
    {
        return bootClass;
    }

    public Set<ProvisionEntry> getEntries()
    {
        return entries;
    }
}
