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

/**
 * @version $Revision$ $Date: 2008-06-15 16:24:01 -0700 (Sun, 15 Jun 2008) $
 */
public class UserIdGroupPair
{
    private final String userId;
    private final String groupName;

    public UserIdGroupPair(String userId, String groupName)
    {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (groupName == null) throw new IllegalArgumentException("groupName cannot be null");

        this.userId = userId;
        this.groupName = groupName;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    @Override
    public String toString()
    {
        return "(UserIdGroupPair userId: " + userId + " groupName: " + groupName + ")";
    }
}
