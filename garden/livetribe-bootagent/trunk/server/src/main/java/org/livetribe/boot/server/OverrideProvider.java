/**
 *
 * Copyright 2010 (C) The original author or authors
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
import org.livetribe.boot.protocol.DoNothing;
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
    private final ProvisionProvider overrideProvisionProvider;
    private final ContentProvider overrideContentProvider;
    private volatile boolean override = false;

    public OverrideProvider(ProvisionProvider provisionProvider, ContentProvider contentProvider, ProvisionProvider overrideProvisionProvider, ContentProvider overrideContentProvider)
    {
        if (provisionProvider == null) throw new IllegalArgumentException("Provision provider is null");
        if (contentProvider == null) throw new IllegalArgumentException("Content provider is null");
        if (overrideProvisionProvider == null) throw new IllegalArgumentException("Override provision provider is null");
        if (overrideContentProvider == null) throw new IllegalArgumentException("Override content provider is null");

        this.provisionProvider = provisionProvider;
        this.contentProvider = contentProvider;
        this.overrideProvisionProvider = overrideProvisionProvider;
        this.overrideContentProvider = overrideContentProvider;
    }

    public ProvisionDirective hello(String uuid, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "hello", new Object[]{uuid, version});

        ProvisionDirective directive = overrideProvisionProvider.hello(uuid, version);
        if (!(directive instanceof DoNothing))
        {
            if (!override) LOGGER.info("Provision provider has been overridden");
            override = true;
            LOGGER.finer("Provision provider has been overridden");
        }
        else
        {
            override = false;
            directive = provisionProvider.hello(uuid, version);
            LOGGER.finer("Provision provider has not been overridden");
        }

        LOGGER.exiting(CLASS_NAME, "hello", directive);

        return directive;
    }

    public InputStream pleaseProvide(String name, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "pleaseProvide", new Object[]{name, version});

        InputStream stream;
        if (override)
        {
            stream = overrideContentProvider.pleaseProvide(name, version);
            LOGGER.finer("Content provider has been overridden");
        }
        else
        {
            stream = contentProvider.pleaseProvide(name, version);
            LOGGER.finer("Content provider has not been overridden");
        }

        LOGGER.exiting(CLASS_NAME, "pleaseProvide", stream);

        return stream;
    }
}
