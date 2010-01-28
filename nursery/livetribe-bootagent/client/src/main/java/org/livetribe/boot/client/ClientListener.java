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

import org.livetribe.boot.protocol.ProvisionDirective;


/**
 * A listener interface used by users of the <code>Client</code> object to
 * monitor its state and to listen for warning and errors.
 * <p/>
 * Errors are in indication that the client is irrevocably broken.  When this
 * occurs the client is broken in some way and there is no way to recover other
 * than restarting the server.
 *
 * @version $Revision$ $Date$
 * @see Listener
 */
public interface ClientListener extends Listener
{
    /**
     * The state of the client has changed.
     *
     * @param oldState the old state of the client
     * @param newState the new state of the client
     */
    public void stateChange(State oldState, State newState);

    /**
     * A provision check is about to be performed
     *
     * @param uuid    the UUID of the client that is going to be sent for the check
     * @param version the provisioning verison that is going to be sent for the check
     */
    public void provisionCheck(String uuid, long version);

    /**
     * A provisioning directive was returned as a result of a check
     *
     * @param directive the directive that was returned
     */
    public void provisionDirective(ProvisionDirective directive);
}
