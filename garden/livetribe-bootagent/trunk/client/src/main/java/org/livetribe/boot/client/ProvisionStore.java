/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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

import org.livetribe.boot.protocol.ProvisionEntry;


/**
 * An instance of a <code>ProvisionStore</code> keeps, in stable storage, a
 * boot agent's UUID and provision configuration.
 * <p/>
 * The provision store provides transaction like semantics so that a tentative
 * inconsistent configuration can be rolled back.  The reason for the
 * multi-phase mechanism for setting the provision configuration is that the
 * configuration must be tested before it can be committed.
 *
 * @version $Revision$ $Date$
 */
public interface ProvisionStore
{
    /**
     * Obtain the UUID of the boot agent that this store is serving.
     *
     * @return the UUID of the boot agent that this store is serving
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    String getUuid() throws ProvisionStoreException;

    /**
     * Set the UUID of the boot agent that this store is serving.
     *
     * @param uuid the UUID of the boot agent that this store is serving
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    void setUuid(String uuid) throws ProvisionStoreException;

    /**
     * Obtain the current provision configuration saved for the boot agent.
     *
     * @return the current provision configuration saved for the boot agent
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    ProvisionConfiguration getCurrentProvisionDirective() throws ProvisionStoreException;

    /**
     * Obtain the next tentative provision configuration saved for the boot agent.
     *
     * @return the next tentative provision configuration saved for the boot agent
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    ProvisionConfiguration getNextProvisionDirective() throws ProvisionStoreException;

    /**
     * Set the next tentative provision configuration saved for the boot agent.
     * <p/>
     * One must call {@link #prepareNext()} and  {@link #commitNext()} to make
     * this tentative provision configuration the permanent current provision
     * configuration.
     *
     * @param provisionConfiguration the next tentative provision configuration saved for the boot agent
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     * @see #prepareNext()
     * @see #commitNext()
     * @see #rollbackNext()
     */
    void setNextProvisionDirective(ProvisionConfiguration provisionConfiguration) throws ProvisionStoreException;

    /**
     * Store the contents of a provision entry in the provision store.
     *
     * @param entry       the provision entry's coordinates
     * @param inputStream the contents of the provision entry
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    void store(ProvisionEntry entry, InputStream inputStream) throws ProvisionStoreException;

    /**
     * Prepare the provision store to use the next tentative provision configuration.
     * <p/>
     * Changes made by this method are not permanent so that the boot agent can
     * perform consistency checks, e.g. boot class is loadable and can be
     * instantiated.  If any error occurs during the check a rollback should be
     * performed.
     *
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    void prepareNext() throws ProvisionStoreException;

    /**
     * Permanently commit the changes of the next tentative provision configuration.
     * <p/>
     * The boot agent should have performed consistency checks before this
     * method is called so that an inconsistent provision configuration is not
     * permanently stored.
     *
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    void commitNext() throws ProvisionStoreException;

    /**
     * Roll back the changes of the next tentative provision configuration.
     * <p/>
     * This method is called if the boot agent finds inconsistencies in the
     * provision configuration.
     *
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    void rollbackNext() throws ProvisionStoreException;

    /**
     * Obtain an array of URLs that point to the provision entries of the
     * current or next tentative provision configuration.
     *
     * @return an array of URLs that point to the provision entries of the current or next tentative provision configuration
     * @throws ProvisionStoreException if there is an error interacting with the provision store
     */
    URL[] getClasspath() throws ProvisionStoreException;
}
