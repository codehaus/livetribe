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

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.util.ARMException;
import org.livetribe.arm.util.ARMUtil;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class TransactionFactory implements ArmTransactionFactory
{
    private final ArmTransactionFactory base;

    public TransactionFactory() throws ARMException
    {
        this.base = ARMUtil.createArmTransactionFactory();
    }

    public ArmApplication newArmApplication(ArmApplicationDefinition definition, String group, String instance, String[] contextValues)
    {
        return base.newArmApplication(definition, group, instance, contextValues);
    }

    public ArmApplicationDefinition newArmApplicationDefinition(String name, ArmIdentityProperties identityProperties, ArmID id)
    {
        return base.newArmApplicationDefinition(name, identityProperties, id);
    }

    public ArmCorrelator newArmCorrelator(byte[] corrBytes)
    {
        return base.newArmCorrelator(corrBytes);
    }

    public ArmCorrelator newArmCorrelator(byte[] corrBytes, int offset)
    {
        return base.newArmCorrelator(corrBytes, offset);
    }

    public ArmID newArmID(byte[] idBytes)
    {
        return base.newArmID(idBytes);
    }

    public ArmID newArmID(byte[] idBytes, int offset)
    {
        return base.newArmID(idBytes, offset);
    }

    public ArmIdentityProperties newArmIdentityProperties(String[] identityNames, String[] identityValues, String[] contextNames)
    {
        return base.newArmIdentityProperties(identityNames, identityValues, contextNames);
    }

    public ArmIdentityPropertiesTransaction newArmIdentityPropertiesTransaction(String[] identityNames, String[] identityValues, String[] contextNames, String uriValue)
    {
        return base.newArmIdentityPropertiesTransaction(identityNames, identityValues, contextNames, uriValue);
    }

    public ArmTransaction newArmTransaction(ArmApplication app, ArmTransactionDefinition definition)
    {
        return base.newArmTransaction(app, definition);
    }

    public ArmTransactionDefinition newArmTransactionDefinition(ArmApplicationDefinition app, String name, ArmIdentityPropertiesTransaction identityProperties, ArmID id)
    {
        return base.newArmTransactionDefinition(app, name, identityProperties, id);
    }

    public ArmUser newArmUser(String name, ArmID id)
    {
        return base.newArmUser(name, id);
    }

    public boolean setErrorCallback(ArmErrorCallback armErrorCallback)
    {
        return base.setErrorCallback(armErrorCallback);
    }

    public void setErrorListener(ArmErrorCallback armErrorCallback)
    {
        base.setErrorCallback(armErrorCallback);
    }

    public int getErrorCode()
    {
        return base.getErrorCode();
    }

    public int setErrorCode(int i)
    {
        return base.setErrorCode(i);
    }

    public String getErrorMessage(int i)
    {
        return base.getErrorMessage(i);
    }
}
