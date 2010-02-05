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
 * Instances of <code>OverrideProvider</code> provide a mechanism to override
 * a base provision and content provider.  This is usually done in an emergency
 * situation, e.g. the original base providers have been compromised.
 * <p/>
 * When the override provision provider supplies a provision directive that is
 * not an instance of <code>DoNothing</code> then the override providers are
 * consulted until the override provision provider supplies an instance of
 * <code>DoNothing</code>.
 *
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

    /**
     * Create an instance of <code>OverrideProvider</code> using a set of base
     * providers and override providers.
     *
     * @param provisionProvider         the base provision provider
     * @param contentProvider           the base content provider
     * @param overrideProvisionProvider the override provision provider
     * @param overrideContentProvider   the override content provider
     */
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

    /**
     * The client introduces itself to the server, identifying itself by using
     * a UUID.  The client also provides the version of its currently accepted
     * provision directive.
     * <p/>
     * When the override provision provider supplies a provision directive that is
     * not an instance of <code>DoNothing</code> then the override providers are
     * consulted until the override provision provider supplies an instance of
     * <code>DoNothing</code>.
     * <p/>
     * Note: this class assumes that <code> #hello(String, long)</code> will be
     * called first to place this provider into an override state;  It also
     * assumes that ta call to <code> #hello(String, long)</code> will take
     * this instance out of an override state.
     *
     * @param uuid    the UUID that should uniquely identify the client
     * @param version the last version that the client was provisioned with
     * @return a provisioning directive
     * @throws BootException if there is a problem obtaining the provisioning directive
     */
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

    /**
     * Obtain a specific version of content with a given name.  This name
     * and version were obtained from a provision directive.
     * <p/>
     * When the override provision provider supplies a provision directive that is
     * not an instance of <code>DoNothing</code> then the override providers are
     * consulted until the override provision provider supplies an instance of
     * <code>DoNothing</code>.
     * <p/>
     * Note: this class assumes that {@link #hello(String, long)} will be
     * called first to place this provider into an override state;  It also
     * assumes that ta call to {@link #hello(String, long)} will take this
     * instance out of an override state.
     *
     * @param name    the name of the content
     * @param version the version of the content
     * @return an input stream of the content's contents
     * @throws BootException if there is a problem obtaining the content
     */
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
