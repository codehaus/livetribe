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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * This provision directive directs what version the client should be at.  The
 * client may update at its own discretion.
 *
 * @version $Revision$ $Date$
 */
public class YouShould extends ProvisionDirective
{
    private final long version;
    private final String bootClass;
    private final Set<ProvisionEntry> entries;

    public YouShould(long version, String bootClass, Set<ProvisionEntry> entries)
    {
        if (bootClass == null) throw new IllegalArgumentException("Boot class cannot be null");
        if (entries == null) throw new IllegalArgumentException("Provision entries cannot be null");

        this.version = version;
        this.bootClass = bootClass;
        this.entries = Collections.unmodifiableSet(new HashSet<ProvisionEntry>(entries));
    }

    /**
     * The version of this provisioning directive.
     *
     * @return the version of this provisioning directive
     */
    public long getVersion()
    {
        return version;
    }

    /**
     * The class to instantiate and execute.  Instances of this class must
     * implement <code>org.livetribe.boot.LifeCycle</code>.
     *
     * @return the name of the class to instantiate
     */
    public String getBootClass()
    {
        return bootClass;
    }

    /**
     * The set provisioning entries associated with this directive.
     *
     * @return the set provisioning entries associated with this directive
     */
    public Set<ProvisionEntry> getEntries()
    {
        return entries;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YouShould youShould = (YouShould)o;

        return version == youShould.version && bootClass.equals(youShould.bootClass);
    }

    @Override
    public int hashCode()
    {
        int result;
        result = (int)(version ^ (version >>> 32));
        result = 31 * result + bootClass.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("SHOULD ");
        builder.append(version);
        builder.append(" ");
        builder.append(bootClass);

        builder.append(" [");
        boolean first = true;
        for (ProvisionEntry entry : entries)
        {
            if (first) first = false;
            else builder.append(", ");

            builder.append(entry);
        }
        builder.append("]");

        return builder.toString();
    }
}
