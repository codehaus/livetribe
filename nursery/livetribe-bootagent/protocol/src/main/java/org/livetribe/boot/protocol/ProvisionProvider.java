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
package org.livetribe.boot.protocol;

/**
 * Provision provider interface.
 * <p/>
 * An inteface is used to decouple the higher level protocol from the lower
 * level wire protocol.  For example wire protocol implementations could
 * include HTTP, BEEP, ASN1, etc.
 * <p/>
 * This interface is purposefully kept simple to enforce a narrow scope of
 * application.  This being the initial provisioning of a boot environment.
 * It is not meant to be used as a fully functional provisioning system.  Such
 * a system would be implemented "on top" of this provision provider.
 *
 * @version $Revision$ $Date$
 */
public interface ProvisionProvider
{
    /**
     * The client introduces itself to the server, identifying itself by using
     * a UUID.  The client also provides the version of its current
     *
     * @param uuid    the UUID that should uniquely identify the client
     * @param version the last version that the client was provisioned with
     * @return a provisioning directive
     * @throws BootException if there is a problem obtaining the provisioning directive
     */
    ProvisionDirective hello(String uuid, long version) throws BootException;
}
