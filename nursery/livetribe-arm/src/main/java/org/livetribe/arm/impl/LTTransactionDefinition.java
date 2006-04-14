/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm.impl;

import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;

import org.livetribe.arm.LTAbstractObject;


/**
 * @version $Revision: $ $Date: $
 */
class LTTransactionDefinition extends LTAbstractObject implements ArmTransactionDefinition
{
    private final ArmApplicationDefinition appDef;
    private final String name;
    private final ArmIdentityPropertiesTransaction idPropsTransaction;
    private final ArmID id;

    LTTransactionDefinition(ArmApplicationDefinition appDef, String name, ArmIdentityPropertiesTransaction idPropsTransaction, ArmID id)
    {
        this.appDef = appDef;
        this.name = name;
        this.idPropsTransaction = idPropsTransaction;
        this.id = id;
    }

    public ArmApplicationDefinition getApplicationDefinition()
    {
        return appDef;
    }

    public ArmID getID()
    {
        return id;
    }

    public ArmIdentityPropertiesTransaction getIdentityProperties()
    {
        return idPropsTransaction;
    }

    public String getName()
    {
        return name;
    }
}
