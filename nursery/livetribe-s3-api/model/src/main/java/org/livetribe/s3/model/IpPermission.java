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
package org.livetribe.s3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @version $Revision$ $Date: 2008-06-15 16:24:01 -0700 (Sun, 15 Jun 2008) $
 */
public class IpPermission
{
    private final IpProtocol ipProtocol;
    private final int fromPort;
    private final int toPort;
    private final List<UserIdGroupPair> groups;
    private final List<String> ipRanges;

    public IpPermission(IpProtocol ipProtocol, int fromPort, int toPort, List<UserIdGroupPair> groups, List<String> ipRanges)
    {
        if (ipProtocol == null) throw new IllegalArgumentException("ipProtocol cannot be null");
        if (groups == null) throw new IllegalArgumentException("groups cannot be null");
        if (ipRanges == null) throw new IllegalArgumentException("ipRanges cannot be null");

        this.ipProtocol = ipProtocol;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.groups = Collections.unmodifiableList(new ArrayList<UserIdGroupPair>(groups));
        this.ipRanges = Collections.unmodifiableList(new ArrayList<String>(ipRanges));
    }

    public IpProtocol getIpProtocol()
    {
        return ipProtocol;
    }

    public int getFromPort()
    {
        return fromPort;
    }

    public int getToPort()
    {
        return toPort;
    }

    public List<UserIdGroupPair> getGroups()
    {
        return groups;
    }

    public List<String> getIpRanges()
    {
        return ipRanges;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("(IpPermission ipProtocol: ");
        builder.append(ipProtocol);
        builder.append(" fromPort: ");
        builder.append(fromPort);
        builder.append(" toPort: ");
        builder.append(toPort);
        builder.append(" groups: [");
        boolean first = true;
        for (UserIdGroupPair pair : groups)
        {
            builder.append(pair);
            if (!first) builder.append(", ");
            else first = false;
        }
        builder.append("] ipRanges: [");
        first = true;
        for (String range : ipRanges)
        {
            builder.append(range);
            if (!first) builder.append(", ");
            else first = false;
        }
        builder.append("])");

        return builder.toString();
    }
}
