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
package org.livetribe.ec2.api.v20080201;

/**
 * A response POJO from a <code>confirmProductInstance()</code> request.
 *
 * @version $Revision$ $Date$
 * @see org.livetribe.ec2.api.v20080201.EC2API#confirmProductInstance(String, String)
 */
public class ProductInstanceConfirmation
{
    private final boolean attached;
    private final String ownerId;

    /**
     * Constructor used if the product code is not attached to the instance.
     */
    public ProductInstanceConfirmation()
    {
        this.attached = false;
        this.ownerId = null;
    }

    /**
     * Constructor used if the product code is not attached to the instance.
     *
     * @param ownerId the instance owner's account ID
     */
    public ProductInstanceConfirmation(String ownerId)
    {
        if (ownerId == null) throw new IllegalArgumentException("ownerId cannot be null");

        this.attached = true;
        this.ownerId = ownerId;
    }

    /**
     * Check if the product code is attached to the instance,
     *
     * @return true if the product code is attached to the instance, false if it is not
     */
    public boolean isAttached()
    {
        return attached;
    }

    /**
     * The instance owner's account ID. Only present if the product code is attached to the instance.
     *
     * @return the instance owner's account ID
     */
    public String getOwnerId()
    {
        return ownerId;
    }

    @Override
    public String toString()
    {
        return "(ProductInstanceConfirmation attached: " + attached + " ownerId: " + ownerId + ")";
    }
}
