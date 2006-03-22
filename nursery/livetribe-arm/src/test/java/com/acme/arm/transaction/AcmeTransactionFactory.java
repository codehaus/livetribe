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
package com.acme.arm.transaction;

import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmUser;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransaction;


/**
 * @version $Revision: $ $Date: $
 */
public class AcmeTransactionFactory implements ArmTransactionFactory
{
    public ArmApplication newArmApplication(ArmApplicationDefinition armApplicationDefinition, String string, String string1, String[] strings)
    {
        return null;
    }

    public ArmApplicationDefinition newArmApplicationDefinition(String string, ArmIdentityProperties armIdentityProperties, ArmID armID)
    {
        return null;
    }

    public ArmCorrelator newArmCorrelator(byte[] bytes)
    {
        return null;
    }

    public ArmCorrelator newArmCorrelator(byte[] bytes, int i)
    {
        return null;
    }

    public ArmID newArmID(byte[] bytes)
    {
        return null;
    }

    public ArmID newArmID(byte[] bytes, int i)
    {
        return null;
    }

    public ArmIdentityProperties newArmIdentityProperties(String[] strings, String[] strings1, String[] strings2)
    {
        return null;
    }

    public ArmIdentityPropertiesTransaction newArmIdentityPropertiesTransaction(String[] strings, String[] strings1, String[] strings2, String string)
    {
        return null;
    }

    public ArmTransaction newArmTransaction(ArmApplication armApplication, ArmTransactionDefinition armTransactionDefinition)
    {
        return null;
    }

    public ArmTransactionDefinition newArmTransactionDefinition(ArmApplicationDefinition armApplicationDefinition, String string, ArmIdentityPropertiesTransaction armIdentityPropertiesTransaction, ArmID armID)
    {
        return null;
    }

    public ArmUser newArmUser(String string, ArmID armID)
    {
        return null;
    }

    public boolean setErrorCallback(ArmErrorCallback armErrorCallback)
    {
        return false;
    }

    public int getErrorCode()
    {
        return 0;
    }

    public int setErrorCode(int i)
    {
        return 0;
    }

    public String getErrorMessage(int i)
    {
        return null;
    }
}
