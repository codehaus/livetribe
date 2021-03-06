/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm40.impl;

import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;


/**
 * @version $Revision: $ $Date: $
 */
class LTApplicationDefinition extends AbstractIdentifiableObject implements ArmApplicationDefinition
{
    private final String name;
    private final ArmIdentityProperties identityProperties;
    private final ArmID id;

    LTApplicationDefinition(String oid, String name, ArmIdentityProperties identityProperties, ArmID id)
    {
        super(oid);

        this.name = name;
        this.identityProperties = identityProperties;
        this.id = id;
    }

    /**
     * For this particular implementation, we do not need to delete anything
     */
    public void destroy()
    {
    }

    public String getName()
    {
        return name;
    }

    public ArmIdentityProperties getIdentityProperties()
    {
        return identityProperties;
    }

    public ArmID getID()
    {
        return id;
    }
}
