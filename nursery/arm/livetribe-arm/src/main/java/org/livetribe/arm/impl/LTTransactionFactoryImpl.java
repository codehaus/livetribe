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

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.AbstractFactoryBase;
import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class LTTransactionFactoryImpl extends AbstractFactoryBase implements ArmTransactionFactory, TransactionErrorCodes
{
    public ArmApplication newArmApplication(ArmApplicationDefinition appDef, String group, String instance, String[] contextValues)
    {
        appDef = ArmAPIUtil.checkRequired(appDef);
        group = ArmAPIUtil.checkOptional255(group);
        instance = ArmAPIUtil.checkOptional255(instance);
        contextValues = ArmAPIUtil.checkOptional(appDef, contextValues);

        return new LTApplication(appDef, group, instance, contextValues);
    }

    public ArmApplicationDefinition newArmApplicationDefinition(String name, ArmIdentityProperties identityProperties, ArmID id)
    {
        name = ArmAPIUtil.checkRequiredName(name);
        identityProperties = ArmAPIUtil.checkOptional(identityProperties);
        id = ArmAPIUtil.checkOptional(id);

        return new LTApplicationDefinition(name, identityProperties, id);
    }

    public ArmCorrelator newArmCorrelator(byte[] corrBytes)
    {
        return newArmCorrelator(corrBytes, 0);
    }

    public ArmCorrelator newArmCorrelator(byte[] corrBytes, int offset)
    {
        return ArmAPIUtil.newArmCorrelator(corrBytes, offset);
    }

    public ArmID newArmID(byte[] idBytes)
    {
        return newArmID(idBytes, 0);
    }

    public ArmID newArmID(byte[] idBytes, int offset)
    {
        if (idBytes == null)
        {
            offset = 0;
            idBytes = new byte[0];

            StaticArmAPIMonitor.error(ID_NULL);
        }

        if (idBytes.length < offset + 16) StaticArmAPIMonitor.warning(ID_TOO_SHORT);

        int length = Math.min(idBytes.length - offset, 16);

        byte[] cleanBytes = new byte[length];
        System.arraycopy(idBytes, offset, cleanBytes, 0, length);

        return new LTID(cleanBytes);
    }

    public ArmIdentityProperties newArmIdentityProperties(String[] identityNames, String[] identityValues, String[] contextNames)
    {
        String[] cleanIdNames = ArmAPIUtil.cleanIdProps(identityNames);
        String[] cleanIdValues = ArmAPIUtil.cleanIdProps(identityValues);
        String[] cleanCtxNames = ArmAPIUtil.cleanIdProps(contextNames);

        for (int i = 0; i < 20; i++)
        {
            if (cleanIdNames[i] == null || cleanIdNames[i].length() == 0)
            {
                cleanIdNames[i] = null;
                cleanIdValues[i] = null;
            }
            else if (cleanIdValues[i] == null || cleanIdValues[i].length() == 0)
            {
                cleanIdNames[i] = null;
                cleanIdValues[i] = null;
            }
        }

        return new LTIdentityProperties(cleanIdNames, cleanIdValues, cleanCtxNames);
    }

    public ArmIdentityPropertiesTransaction newArmIdentityPropertiesTransaction(String[] identityNames, String[] identityValues, String[] contextNames, String uriValue)
    {
        String[] cleanIdNames = ArmAPIUtil.cleanIdProps(identityNames);
        String[] cleanIdValues = ArmAPIUtil.cleanIdProps(identityValues);
        String[] cleanCtxNames = ArmAPIUtil.cleanIdProps(contextNames);

        for (int i = 0; i < 20; i++)
        {
            if (cleanIdNames[i] == null || cleanIdNames[i].length() == 0)
            {
                cleanIdNames[i] = null;
                cleanIdValues[i] = null;
            }
            else if (cleanIdValues[i] == null || cleanIdValues[i].length() == 0)
            {
                cleanIdNames[i] = null;
                cleanIdValues[i] = null;
            }
        }

        if (uriValue != null && uriValue.length() > 4096) StaticArmAPIMonitor.warning(URI_TOO_LONG);

        return new LTIdentityPropertiesTransaction(cleanIdNames, cleanIdValues, cleanCtxNames, uriValue);
    }

    public ArmTransaction newArmTransaction(ArmApplication app, ArmTransactionDefinition definition)
    {
        app = ArmAPIUtil.checkRequired(app);
        definition = ArmAPIUtil.checkRequired(definition);

        return new LTTransaction(app, definition);
    }

    public ArmTransactionDefinition newArmTransactionDefinition(ArmApplicationDefinition app, String name, ArmIdentityPropertiesTransaction identityProperties, ArmID id)
    {
        app = ArmAPIUtil.checkRequired(app);
        name = ArmAPIUtil.checkRequiredName(name);
        identityProperties = ArmAPIUtil.checkOptional(identityProperties);
        id = ArmAPIUtil.checkOptional(id);

        return new LTTransactionDefinition(app, name, identityProperties, id);
    }

    public ArmUser newArmUser(String name, ArmID id)
    {
        name = ArmAPIUtil.checkRequiredName(name);
        id = ArmAPIUtil.checkOptional(id);

        return new LTUser(name, id);
    }
}
