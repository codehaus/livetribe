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

import java.util.Set;


/**
 * @version $Revision$ $Date$
 */
public class YouMust extends YouShould
{
    private final boolean restart;
    private transient String string;

    public YouMust(long version, String bootClass, Set<ProvisionEntry> entries, boolean restart)
    {
        super(version, bootClass, entries);
        this.restart = restart;
    }

    public boolean isRestart()
    {
        return restart;
    }

    @Override
    public String toString()
    {
        if (string == null)
        {
            StringBuilder builder = new StringBuilder();
            builder.append("MUST ");
            builder.append(getVersion());
            builder.append(" ");
            builder.append(getBootClass());
            builder.append(" ");
            builder.append((restart ? " RESTART" : "NO-RESTART"));

            builder.append(" [");
            boolean first = true;
            for (ProvisionEntry entry : getEntries())
            {
                if (first) first = false;
                else builder.append(", ");

                builder.append(entry);
            }
            builder.append("]");

            string = builder.toString();
        }
        return string;
    }
}