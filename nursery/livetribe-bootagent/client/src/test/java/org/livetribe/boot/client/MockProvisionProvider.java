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

import java.util.HashSet;
import java.util.Set;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.ProvisionProvider;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision$ $Date$
 */
public class MockProvisionProvider implements ProvisionProvider
{
    public YouShould hello(String uuid, long version) throws BootException
    {
        Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
        return new YouShould(1, "org.livetribe.boot.Server", entries);
    }
}
