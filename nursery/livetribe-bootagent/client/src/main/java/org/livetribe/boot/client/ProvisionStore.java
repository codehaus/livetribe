/**
 *
 * Copyright 2007-2009 (C) The original author or authors
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
package org.livetribe.boot.client;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.livetribe.boot.protocol.ProvisionEntry;


/**
 * @version $Revision$ $Date$
 */
public interface ProvisionStore
{
    String getUuid() throws ProvisionStoreException;

    void setUuid(String uuid) throws ProvisionStoreException;

    ProvisionConfiguration getCurrentProvisionDirective() throws ProvisionStoreException;

    ProvisionConfiguration getNextProvisionDirective() throws ProvisionStoreException;

    void setNextProvisionDirective(ProvisionConfiguration provisionConfiguration) throws ProvisionStoreException;

    void store(ProvisionEntry provisionEntry, InputStream inputStream) throws ProvisionStoreException;

    void prepareNext() throws ProvisionStoreException;

    void commitNext() throws ProvisionStoreException;

    void rollbackNext() throws ProvisionStoreException;

    List<URL> getClasspath() throws ProvisionStoreException;
}
