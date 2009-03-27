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
package org.livetribe.boot.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.ProvisionProvider;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * A properties file based provisioning provider.
 * <p/>
 * <p/>
 * <dl>
 * <dt>version</dt>
 * <dd>the version of this provisioning directive</dd>
 * <dt>boot.class</dt>
 * <dd>the boot class to instantiate and make lifecycle calls to</dd>
 * <dt>directive.x</dt>
 * <dd>a provisioning entry in the format of &lt;name&gt;:&lt;version&gt;.  The order of
 * these entries, specified by the conscutive numbers ".x" indicate the order
 * to be used when constructing the classpath when the boot class is
 * instantiated and started.</dd>
 * <dt>required</dt>
 * <dd>indicate whether or not a forced restart should occur.  The default is
 * <code>false</code></dd>
 * </dl>
 * <p/>
 * Entries that are directed to specific UUIDs are prefixed with that UUID +
 * ".", e.g. <code>1d02-d3c9.version</code> would be used to specify the
 * version of the provisioning directive for UUID <code>1d02-d3c9</code>.
 * <p/>
 * In the following example the version of the default provisioning directive
 * is 10, ts boot class is <code>com.acme.boot.BootAgent</code>, and its
 * provisioning entries are:
 * <ol>
 * <li>acme-a, version 1</li>
 * <li>acme-b, version 1</li>
 * <li>acme-b, version 1</li>
 * </ol>
 * <p/>
 * For the UUID <code>1d02-d3c9</code> the version of the default provisioning directive
 * is 5, ts boot class is <code>com.foo.boot.BootAgent</code>, its
 * provisioning entries are:
 * <ol>
 * <li>foo-a, version 4</li>
 * <li>acme-a, version 1</li>
 * <li>acme-b, version 2</li>
 * <li>acme-b, version 1</li>
 * </ol>
 * and it is directed to restart once the entries have been loaded.
 * <p/>
 * <blockquote><pre>
 * version=10
 * boot.class=com.acme.boot.BootAgent
 * directive.0=acme-a:1
 * directive.1=acme-b:1
 * directive.2=acme-c:1
 * 1d02-d3c9.version=5
 * 1d02-d3c9.boot.class=com.foo.boot.BootAgent
 * 1d02-d3c9.directive.0=foo-a:4
 * 1d02-d3c9.directive.1=acme-a:1
 * 1d02-d3c9.directive.2=acme-b:2
 * 1d02-d3c9.directive.3=acme-c:1
 * 1d02-d3c9.required=true
 * </pre></blockquote>
 *
 * @version $Revision$ $Date$
 */
public class PropertiesProvisionProvider implements ProvisionProvider
{
    private final static String CLASS_NAME = PropertiesProvisionProvider.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final static String REQUIRED_KEY = "required";
    private final static String RESTART_KEY = "restart";
    private final static String VERSION_KEY = "version";
    private final static String BOOT_CLASS_KEY = "boot.class";
    private final static String DIRECTIVE_BASE_KEY = "directive.";
    private final Properties properties = new Properties();
    private final File file;


    public PropertiesProvisionProvider(File file) throws IOException
    {
        if (file == null) throw new IllegalArgumentException("File cannot be null");
        if (!file.exists()) throw new IllegalArgumentException("File does not exist");

        this.file = file;

        if (LOGGER.isLoggable(Level.CONFIG)) LOGGER.config("file: " + file);

        reload();
    }

    /**
     * Reloads the configuration of this provisioning provider.
     *
     * @throws IOException if there is an error loading the properties file
     */
    public synchronized void reload() throws IOException
    {
        LOGGER.entering(CLASS_NAME, "reload");

        properties.clear();
        properties.load(new FileInputStream(file));

        if (LOGGER.isLoggable(Level.FINEST))
        {
            @SuppressWarnings({"unchecked"}) Enumeration<String> keys = (Enumeration<String>) properties.propertyNames();
            while (keys.hasMoreElements())
            {
                String key = keys.nextElement();
                LOGGER.finest(key + " = " + properties.getProperty(key));
            }
        }

        LOGGER.exiting(CLASS_NAME, "reload");
    }

    /**
     * {@inheritDoc}
     */
    public synchronized ProvisionDirective hello(String uuid, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "hello", new Object[]{uuid, version});

        ProvisionDirective directive;

        if (properties.containsKey(uuid + "." + VERSION_KEY)) directive = load(uuid + ".", version);
        else if (properties.containsKey(VERSION_KEY)) directive = load("", version);
        else throw new BootException("No directives found for " + uuid);

        LOGGER.exiting(CLASS_NAME, "hello", directive);

        return directive;
    }

    protected ProvisionDirective load(String prefix, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "load", new Object[]{prefix, version});

        assert Thread.holdsLock(this);

        final long v = Long.valueOf((String) properties.get(prefix + VERSION_KEY));

        if (v == version) return new DoNothing();

        String bootClass = (String) properties.get(prefix + BOOT_CLASS_KEY);
        Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
        int count = 0;
        while (true)
        {
            String key = prefix + DIRECTIVE_BASE_KEY + count++;

            if (!properties.containsKey(key)) break;

            String[] tokens = ((String) properties.get(key)).split(":");

            if (tokens.length != 2) throw new BootException("Malformed entry for " + key);

            entries.add(new ProvisionEntry(tokens[0], Long.valueOf(tokens[1])));
        }

        ProvisionDirective directive;
        if (properties.containsKey(prefix + REQUIRED_KEY)) directive = new YouMust(v, bootClass, entries, properties.containsKey(prefix + RESTART_KEY));
        else directive = new YouShould(v, bootClass, entries);

        LOGGER.exiting(CLASS_NAME, "load", directive);

        return directive;
    }
}
