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
package org.livetribe.arm40.model;

import java.util.List;


/**
 * @version $Revision$ $Date$
 */
public interface ARMDAO
{
    public List getIdentityPropertiesByName(String[] idNames, String[] idValues, String[] ctxNames);

    public IdentityProperties getIdentityPropertiesByOID(String oid);

    public void saveIdentityProperties(IdentityProperties identityProperties);

    public void deleteIdentityProperties(IdentityProperties identityProperties);

    public List getIdentityPropertiesTransactionByName(String[] idNames, String[] idValues, String[] ctxNames, String uriValue);

    public IdentityPropertiesTransaction getIdentityPropertiesTransactionByOID(String oid);

    public void saveIdentityPropertiesTransaction(IdentityPropertiesTransaction identityPropertiesTransaction);

    public void deleteIdentityPropertiesTransaction(IdentityPropertiesTransaction identityPropertiesTransaction);

    public ApplicationDefinition getApplicationDefinitionByName(String name, IdentityProperties identityProperties);

    public ApplicationDefinition getApplicationDefinitionByOID(String oid);

    public ApplicationDefinition getApplicationDefinitionById(byte[] id);

    public void saveApplicaitonDefinition(ApplicationDefinition applicationDefinition);

    public void deleteApplicaitonDefinition(ApplicationDefinition applicationDefinition);
}
