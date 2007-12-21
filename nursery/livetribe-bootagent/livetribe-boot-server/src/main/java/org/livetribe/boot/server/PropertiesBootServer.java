/**
 *
 * Copyright 2007 (C) The original author or authors
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
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.livetribe.boot.protocol.BootServer;
import org.livetribe.boot.protocol.BootServerException;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision$ $Date$
 */
public class PropertiesBootServer implements BootServer
{
    private final static String REQUIRED_KEY = "required";
    private final static String RESTART_KEY = "restart";
    private final static String VERSION_KEY = "version";
    private final static String BOOT_CLASS_KEY = "boot.class";
    private final static String DIRECTIVE_BASE_KEY = "directive.";
    private final static String PROPERTIES_NAME = "directives.properties";
    private final static String RESOURCES_NAME = "resources";
    private final File resources;
    private final Properties properties = new Properties();


    public PropertiesBootServer(File directory) throws IOException
    {
        if (directory == null) throw new IllegalArgumentException("File cannot be null");
        if (!directory.exists() || !directory.isDirectory()) throw new IllegalArgumentException("Directory does not exist or is a file");

        this.resources = new File(directory, RESOURCES_NAME);
        if (!resources.exists() || !resources.isDirectory()) throw new IllegalArgumentException("Resources directory does not exist");

        properties.load(new FileInputStream(new File(directory, PROPERTIES_NAME)));
    }

    public YouShould hello(String uuid, long version) throws BootServerException
    {
        if (properties.containsKey(uuid + "." + VERSION_KEY)) return load(uuid + ".");

        if (properties.containsKey(VERSION_KEY)) return load("");

        throw new BootServerException("No directives found for " + uuid);
    }

    protected YouShould load(String prefix) throws BootServerException
    {
        long v = Long.valueOf((String) properties.get(prefix + VERSION_KEY));
        String bootClass = (String) properties.get(prefix + BOOT_CLASS_KEY);
        Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
        int count = 0;
        while (true)
        {
            String key = prefix + DIRECTIVE_BASE_KEY + count++;

            if (!properties.containsKey(key)) break;

            String[] tokens = ((String) properties.get(key)).split(":");

            if (tokens.length != 2) throw new BootServerException("Malformed entry for " + key);

            entries.add(new ProvisionEntry(tokens[0], Long.valueOf(tokens[1])));
        }

        if (properties.containsKey(prefix + REQUIRED_KEY)) return new YouMust(v, bootClass, entries, properties.containsKey(prefix + RESTART_KEY));
        else return new YouShould(v, bootClass, entries);
    }

    public InputStream pleaseProvide(String name, long version) throws BootServerException
    {
        File directory = new File(resources, name);

        if (!directory.exists() || !directory.isDirectory()) throw new BootServerException("Directory for resource does not exist");

        File resource = new File(directory, Long.toString(version));

        if (!resource.exists() || !resource.isFile()) throw new BootServerException("Version for resource does not exist");

        try
        {
            return resource.toURL().openStream();
        }
        catch (IOException ioe)
        {
            throw new BootServerException("Unable to open stream for resource", ioe);
        }
    }
}
