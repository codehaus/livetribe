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

/**
 * @version $Revision$ $Date: 2008-05-30 09:40:48 -0700 (Fri, 30 May 2008) $
 */
public class ReservationInfo
{
    private final String id;
    private final String ownerId;
    private final String[] groups;
    private final Instance[] instances;

    public ReservationInfo(String id, String ownerId, String[] groups, Instance[] instances)
    {
        this.id = id;
        this.ownerId = ownerId;

        this.groups = new String[groups.length];
        System.arraycopy(groups, 0, this.groups, 0, this.groups.length);

        this.instances = new Instance[instances.length];
        System.arraycopy(instances, 0, this.instances, 0, this.instances.length);
    }

    public String getId()
    {
        return id;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String[] getGroups()
    {
        String[] result = new String[groups.length];
        System.arraycopy(groups, 0, result, 0, result.length);
        return result;
    }

    public Instance[] getInstances()
    {
        Instance[] result = new Instance[instances.length];
        System.arraycopy(instances, 0, result, 0, result.length);
        return result;
    }
}
