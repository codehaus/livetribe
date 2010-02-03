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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionProvider;


/**
 * A <code>MultiplexingProvisionProvider</code> will interrogate a fixed set
 * of <code>ProvisionProvider</code> instances to obtain the first non
 * do-nothing instance.  If none is obtained then it will return an instance
 * of <code>DoNothing</code>.
 * <p/>
 * This class can be used to allow one provision provider to override another
 * in case of an emergency.
 *
 * @version $Revision: $ $Date: $
 */
public class MultiplexingProvisionProvider implements ProvisionProvider
{
    private final static String CLASS_NAME = MultiplexingProvisionProvider.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final static DoNothing DO_NOTHING = new DoNothing();
    private final List<ProvisionProvider> providers = new ArrayList<ProvisionProvider>();

    /**
     * Create an instance of <code>MultiplexingProvisionProvider</code> with a
     * fixed set of <code>ProvisionProvider</code> instances.
     *
     * @param providers the fixed set of <code>ProvisionProvider</code> instances
     */
    public MultiplexingProvisionProvider(ProvisionProvider... providers)
    {
        if (providers == null || providers.length == 0) throw new IllegalArgumentException("Must provide at least on provision provider");

        this.providers.addAll(Arrays.asList(providers));

        if (LOGGER.isLoggable(Level.CONFIG)) LOGGER.config("providers: " + Arrays.toString(providers));
    }

    /**
     * Interrogate a fixed set of <code>ProvisionProvider</code> instances to
     * obtain the first non do-nothing instance.  If none is obtained then it
     * will return an instance of <code>DoNothing</code>.
     *
     * @param uuid    the UUID that should uniquely identify the client
     * @param version the last version that the client was provisioned with
     * @return a provisioning directive
     * @throws BootException if there is a problem obtaining the provisioning directive
     */
    public ProvisionDirective hello(String uuid, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "hello", new Object[]{uuid, version});

        ProvisionDirective directive = DO_NOTHING;

        for (ProvisionProvider provider : providers)
        {
            directive = provider.hello(uuid, version);

            if (!(directive instanceof DoNothing)) break;
        }

        LOGGER.entering(CLASS_NAME, "hello", directive);

        return directive;
    }
}
