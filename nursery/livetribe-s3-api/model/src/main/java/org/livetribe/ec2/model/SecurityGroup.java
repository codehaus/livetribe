/**
 *
 * Copyright 2008 (C) The original author or authors
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
package org.livetribe.ec2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @version $Revision$ $Date: 2008-06-15 16:24:01 -0700 (Sun, 15 Jun 2008) $
 */
public class SecurityGroup
{
    private final String ownerId;
    private final String name;
    private final String description;
    private final List<IpPermission> ipPermissions;

    public SecurityGroup(String ownerId, String name, String description, List<IpPermission> ipPermissions)
    {
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.ipPermissions = Collections.unmodifiableList(new ArrayList<IpPermission>(ipPermissions));
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public List<IpPermission> getIpPermissions()
    {
        return ipPermissions;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("(SecurityGroup ownerId: ");
        builder.append(ownerId);
        builder.append(" name: ");
        builder.append(name);
        builder.append(" description: ");
        builder.append(description);
        builder.append(" ipPermissions: [");
        boolean first = true;
        for (IpPermission permission : ipPermissions)
        {
            builder.append(permission);
            if (!first) builder.append(", ");
            else first = false;
        }
        builder.append("])");

        return builder.toString();
    }
}
