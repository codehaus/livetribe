/**
 *
 * Copyright 2009 (C) The original author or authors
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
package org.livetribe.boot.server;

import java.io.InputStream;
import java.util.logging.Logger;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionProvider;


/**
 * @version $Revision$ $Date$
 */
public class OverrideProvider implements ProvisionProvider, ContentProvider
{
    private final static String CLASS_NAME = OverrideProvider.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ProvisionProvider provisionProvider;
    private final ContentProvider contentProvider;

    public OverrideProvider(ProvisionProvider provisionProvider, ContentProvider contentProvider)
    {
        if (provisionProvider == null) throw new IllegalArgumentException("Provision provider is null");
        if (contentProvider == null) throw new IllegalArgumentException("Content provider is null");

        this.provisionProvider = provisionProvider;
        this.contentProvider = contentProvider;
    }

    public ProvisionDirective hello(String uuid, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "hello", new Object[]{uuid, version});

        ProvisionDirective directive = provisionProvider.hello(uuid, version);

        LOGGER.exiting(CLASS_NAME, "hello", directive);

        return directive;
    }

    public InputStream pleaseProvide(String name, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "pleaseProvide", new Object[]{name, version});

        InputStream stream = contentProvider.pleaseProvide(name, version);

        LOGGER.exiting(CLASS_NAME, "pleaseProvide", stream);

        return stream;
    }
}
