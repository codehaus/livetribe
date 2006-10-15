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
package org.livetribe.arm40.impl;

import org.livetribe.arm40.util.StaticArmAPIMonitor;
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


/**
 * @version $Revision: $ $Date: $
 */
public class LTTransactionFactoryImpl extends AbstractFactoryBase implements ArmTransactionFactory
{
    public ArmApplication newArmApplication(ArmApplicationDefinition appDef, String group, String instance, String[] contextValues)
    {
        appDef = APIUtil.checkRequired(appDef);
        group = APIUtil.checkOptional255(group);
        instance = APIUtil.checkOptional255(instance);
        contextValues = APIUtil.checkOptional(appDef, contextValues);

        LTApplication app = new LTApplication(allocateOID(), appDef, group, instance, contextValues);

        getConnection().declareApplication(app.getObjectId(),
                                           APIUtil.extractOID(appDef),
                                           group,
                                           instance,
                                           contextValues);

        return app;
    }

    public ArmApplicationDefinition newArmApplicationDefinition(String name, ArmIdentityProperties identityProperties, ArmID id)
    {
        name = APIUtil.checkRequiredName(name);
        identityProperties = APIUtil.checkOptional(identityProperties);
        id = APIUtil.checkOptional(id);

        LTApplicationDefinition appDef = new LTApplicationDefinition(allocateOID(), name, identityProperties, id);

        getConnection().declareApplicationDefinition(appDef.getObjectId(),
                                                     name,
                                                     APIUtil.extractOID(identityProperties),
                                                     APIUtil.extractArmId(id));

        return appDef;
    }

    public ArmCorrelator newArmCorrelator(byte[] corrBytes)
    {
        return newArmCorrelator(corrBytes, 0);
    }

    public ArmCorrelator newArmCorrelator(byte[] corrBytes, int offset)
    {
        return APIUtil.newArmCorrelator(corrBytes, offset);
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

            StaticArmAPIMonitor.error(TransactionErrorCodes.ID_NULL);
        }

        if (idBytes.length < offset + 16) StaticArmAPIMonitor.warning(TransactionErrorCodes.ID_TOO_SHORT);

        int length = Math.min(idBytes.length - offset, 16);

        byte[] cleanBytes = new byte[length];
        System.arraycopy(idBytes, offset, cleanBytes, 0, length);

        return new LTID(cleanBytes);
    }

    public ArmIdentityProperties newArmIdentityProperties(String[] identityNames, String[] identityValues, String[] contextNames)
    {
        String[] cleanIdNames = APIUtil.cleanIdProps(identityNames);
        String[] cleanIdValues = APIUtil.cleanIdProps(identityValues);
        String[] cleanCtxNames = APIUtil.cleanIdProps(contextNames);

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

        LTIdentityProperties idProps = new LTIdentityProperties(allocateOID(), cleanIdNames, cleanIdValues, cleanCtxNames);

        getConnection().declareIdentityProperties(idProps.getObjectId(),
                                                  cleanIdNames,
                                                  cleanIdValues,
                                                  cleanCtxNames);

        return idProps;
    }

    public ArmIdentityPropertiesTransaction newArmIdentityPropertiesTransaction(String[] identityNames, String[] identityValues, String[] contextNames, String uriValue)
    {
        String[] cleanIdNames = APIUtil.cleanIdProps(identityNames);
        String[] cleanIdValues = APIUtil.cleanIdProps(identityValues);
        String[] cleanCtxNames = APIUtil.cleanIdProps(contextNames);

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

        if (uriValue != null && uriValue.length() > 4096)
            StaticArmAPIMonitor.warning(TransactionErrorCodes.URI_TOO_LONG);

        LTIdentityPropertiesTransaction idPropsTrans = new LTIdentityPropertiesTransaction(allocateOID(), cleanIdNames, cleanIdValues, cleanCtxNames, uriValue);

        getConnection().declareIdentityPropertiesTransaction(idPropsTrans.getObjectId(),
                                                             cleanIdNames,
                                                             cleanIdValues,
                                                             cleanCtxNames,
                                                             uriValue);

        return idPropsTrans;
    }

    public ArmTransaction newArmTransaction(ArmApplication app, ArmTransactionDefinition definition)
    {
        app = APIUtil.checkRequired(app);
        definition = APIUtil.checkRequired(definition);

        LTTransaction transaction = new LTTransaction(allocateOID(), getConnection(), getGuidGenerator(), app, definition);

        getConnection().associateTransaction(transaction.getObjectId(),
                                             APIUtil.extractOID(definition),
                                             APIUtil.extractOID(definition));

        return transaction;
    }

    public ArmTransactionDefinition newArmTransactionDefinition(ArmApplicationDefinition app, String name, ArmIdentityPropertiesTransaction identityProperties, ArmID id)
    {
        app = APIUtil.checkRequired(app);
        name = APIUtil.checkRequiredName(name);
        identityProperties = APIUtil.checkOptional(identityProperties);
        id = APIUtil.checkOptional(id);

        LTTransactionDefinition transDef = new LTTransactionDefinition(allocateOID(), app, name, identityProperties, id);

        getConnection().declareTransactionDefinition(transDef.getObjectId(),
                                                     name,
                                                     APIUtil.extractOID(identityProperties),
                                                     APIUtil.extractArmId(id));

        return transDef;
    }

    public ArmUser newArmUser(String name, ArmID id)
    {
        name = APIUtil.checkRequiredName(name);
        id = APIUtil.checkOptional(id);

        return new LTUser(name, id);
    }
}
