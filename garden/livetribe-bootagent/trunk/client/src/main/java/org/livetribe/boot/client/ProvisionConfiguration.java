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
package org.livetribe.boot.client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.livetribe.boot.protocol.ProvisionEntry;


/**
 * Represents a provision configuration for a particular version.
 *
 * @version $Revision$ $Date$
 */
public class ProvisionConfiguration
{
    private final long version;
    private final String bootClass;
    private final Set<ProvisionEntry> entries;

    /**
     * Construct an "empty" instance whose version is zero, has no boot class
     * specified, and has an empty set of provisioning entries.
     */
    public ProvisionConfiguration()
    {
        this(0, "", Collections.<ProvisionEntry>emptySet());
    }

    /**
     * Construct an instance with a particular version, boot class, and entries.
     *
     * @param version   the version of this provision configuration
     * @param bootClass the boot class of this provision configuration
     * @param entries   tne provision entries of this provision configuration
     */
    public ProvisionConfiguration(long version, String bootClass, Set<ProvisionEntry> entries)
    {
        if (bootClass == null) throw new IllegalArgumentException("Boot class cannot be null");
        if (entries == null) throw new IllegalArgumentException("Provision entries cannot be null");

        this.version = version;
        this.bootClass = bootClass;
        this.entries = Collections.unmodifiableSet(new HashSet<ProvisionEntry>(entries));
    }

    /**
     * Obtain the version of this provision configuration
     *
     * @return the version of this provision configuration
     */
    public long getVersion()
    {
        return version;
    }

    /**
     * Obtain the boot class of this provision configuration
     *
     * @return the boot class of this provision configuration
     */
    public String getBootClass()
    {
        return bootClass;
    }

    /**
     * Obtain tne provision entries of this provision configuration
     *
     * @return tne provision entries of this provision configuration
     */
    public Set<ProvisionEntry> getEntries()
    {
        return entries;
    }
}
