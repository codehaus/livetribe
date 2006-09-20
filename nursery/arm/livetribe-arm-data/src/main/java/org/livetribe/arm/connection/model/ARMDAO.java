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
package org.livetribe.arm.connection.model;

import java.util.List;


/**
 * @version $Revision$ $Date$
 */
public interface ARMDAO
{
    public ApplicationDefinition getApplicationDefinitionByName(String name, IdentityProperties identityProperties) throws ARMDAOException;

    public ApplicationDefinition getApplicationDefinitionById(byte[] id) throws ARMDAOException;

    public void saveApplicaitonDefinition(ApplicationDefinition applicationDefinition) throws ARMDAOException;

    public void deleteApplicaitonDefinition(ApplicationDefinition applicationDefinition) throws ARMDAOException;

    public List getIdentityPropertiesByName(String[] idNames, String[] idValues, String[] ctxNames) throws ARMDAOException;

    public IdentityProperties getIdentityPropertiesByOID(Long oid) throws ARMDAOException;

    public void saveIdentityProperties(IdentityProperties identityProperties) throws ARMDAOException;

    public void deleteIdentityProperties(IdentityProperties identityProperties) throws ARMDAOException;
}
