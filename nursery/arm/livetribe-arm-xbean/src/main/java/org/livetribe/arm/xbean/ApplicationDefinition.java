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
package org.livetribe.arm.xbean;

import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionFactory;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class ApplicationDefinition extends Base
{
    private ArmTransactionFactory factory;
    private String name;
    private IdentityProperties identityProperties;
    private byte[] armId;

    protected ArmInterface generate()
    {
        return factory.newArmApplicationDefinition(name, (ArmIdentityProperties) identityProperties.unWrap(), factory.newArmID(armId));
    }

    public ArmTransactionFactory getFactory()
    {
        return factory;
    }

    public void setFactory(ArmTransactionFactory factory)
    {
        this.factory = factory;
    }

    /**
     * @org.apache.xbean.Property alias="armName"
     */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public IdentityProperties getIdentityProperties()
    {
        return identityProperties;
    }

    public void setIdentityProperties(IdentityProperties identityProperties)
    {
        this.identityProperties = identityProperties;
    }

    /**
     * TODO AT org.apache.xbean.Property alias="armId"
     */
    public byte[] getArmId()
    {
        return armId;
    }

    public void setArmId(byte[] armId)
    {
        this.armId = armId;
    }
}
