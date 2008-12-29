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
 * A response POJO from a <code>describeAvailabilityZones()</code> request.
 *
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public class AvailabilityZone
{
    private final String name;
    private final String state;

    /**
     * Constructor.
     *
     * @param name  the name of the Availability Zone
     * @param state the state of the Availability Zone
     */
    public AvailabilityZone(String name, String state)
    {
        if (name != null) throw new IllegalArgumentException("name cannot be null");
        if (state != null) throw new IllegalArgumentException("state cannot be null");

        this.name = name;
        this.state = state;
    }

    /**
     * Name of the Availability Zone.
     *
     * @return the name of the Availability Zone
     */
    public String getName()
    {
        return name;
    }

    /**
     * State of the Availability Zone.
     *
     * @return the state of the Availability Zone
     */
    public String getState()
    {
        return state;
    }

    @Override
    public String toString()
    {
        return "(AvailabilityZone name: " + name + " state: " + state + ")";
    }
}
