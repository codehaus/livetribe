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
package org.livetribe.boot.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.livetribe.boot.protocol.ProvisionEntry;


/**
 * @version $Revision$ $Date$
 */
public class DefaultProvisionStore implements ProvisionStore
{
    private final static String DEFAULT_UUID = "org.livetribe.boot.uuid.unset";
    private final static String UUID_FILE = "uuid";
    private final static String CURRENT_FILE = "current.properties";
    private final static String NEXT_FILE = "next.properties";
    private final static String VERSION_KEY = "version";
    private final static String BOOT_CLASS_KEY = "boot.class";
    private final static String DIRECTIVE_BASE_KEY = "directive.";
    private final File root;
    private final File resources;
    private String uuid = "org.livetribe.boot.uuid.unset";
    private ProvisionDirective currentProvisionDirective;
    private ProvisionDirective nextProvisionDirective;

    public DefaultProvisionStore(File root)
    {
        if (root == null) throw new IllegalArgumentException("File root must not be null");

        this.root = root;
        this.resources = new File(root, "resources");
        this.uuid = loadUuid();
        this.currentProvisionDirective = loadCurrentProvisionDirective();
        this.nextProvisionDirective = loadNextProvisionDirective();

        if (!resources.exists()) resources.mkdirs();
        else if (!resources.isDirectory()) throw new IllegalArgumentException("Resources in root is not a directory");
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid) throws ProvisionStoreException
    {
        this.uuid = uuid;
        saveUuid(uuid);
    }

    public ProvisionDirective getCurrentProvisionDirective()
    {
        return currentProvisionDirective;
    }

    public ProvisionDirective getNextProvisionDirective()
    {
        return nextProvisionDirective;
    }

    public void setNextProvisionDirective(ProvisionDirective provisionDirective) throws ProvisionStoreException
    {
        nextProvisionDirective = provisionDirective;
        saveNextProvisionDirective(nextProvisionDirective);
    }

    public void store(ProvisionEntry provisionEntry, InputStream inputStream) throws ProvisionStoreException
    {
        File directory = new File(resources, provisionEntry.getName());

        if (!directory.exists()) directory.mkdirs();
        else if (!directory.isDirectory()) throw new ProvisionStoreException("Resource directory " + directory + " is not a directory");

        try
        {
            File resource = new File(directory, Long.toString(provisionEntry.getVersion()));
            FileOutputStream outputStream = new FileOutputStream(resource);
            byte[] buffer = new byte[16384];
            int len;

            while ((len = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, len);
        }
        catch (IOException ioe)
        {
            throw new ProvisionStoreException("Unable to save UUID", ioe);
        }
    }

    public void prepareNext() throws ProvisionStoreException
    {
        if (nextProvisionDirective.getVersion() < 1) throw new ProvisionStoreException("Next provision directive has bad version " + nextProvisionDirective.getVersion());
        if (nextProvisionDirective.getBootClass().trim().length() == 0) throw new ProvisionStoreException("Next provision directive has empty boot class");

        for (ProvisionEntry entry : nextProvisionDirective.getEntries())
        {
            File directory = new File(resources, entry.getName());
            if (!directory.exists()) throw new ProvisionStoreException("Resource directory" + directory + " does not exist");
            else if (!directory.isDirectory()) throw new ProvisionStoreException("Resource directory " + directory + " is not a directory");

            File resource = new File(directory, Long.toString(entry.getVersion()));
            if (!resource.exists()) throw new ProvisionStoreException("Resource verison " + entry.getVersion() + " does not exist");
            else if (!resource.isFile()) throw new ProvisionStoreException("Resource verison " + entry.getVersion() + " is not a file");
        }

        Set<ProvisionEntry> removeSet = new HashSet<ProvisionEntry>(currentProvisionDirective.getEntries());
        removeSet.removeAll(nextProvisionDirective.getEntries());

        currentProvisionDirective = nextProvisionDirective;
        saveCurrentProvisionDirective(currentProvisionDirective);

        for (ProvisionEntry entry : removeSet)
        {
            File directory = new File(resources, entry.getName());
            File resource = new File(directory, Long.toString(entry.getVersion()));

            resource.delete();
        }
    }

    public List<URL> getClasspath() throws ProvisionStoreException
    {
        List<URL> classpath = new ArrayList<URL>(currentProvisionDirective.getEntries().size());

        for (ProvisionEntry entry : currentProvisionDirective.getEntries())
        {
            File directory = new File(resources, entry.getName());
            File resource = new File(directory, Long.toString(entry.getVersion()));

            try
            {
                classpath.add(resource.getCanonicalFile().toURL());
            }
            catch (IOException ioe)
            {
                throw new ProvisionStoreException("Unable to obtail URL for " + resource, ioe);
            }
        }

        return classpath;
    }

    private String loadUuid()
    {
        File file = new File(root, UUID_FILE);
        if (file.exists())
        {
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                return reader.readLine();
            }
            catch (IOException ioe)
            {
                return DEFAULT_UUID;
            }
        }
        return DEFAULT_UUID;
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    private void saveUuid(String uuid) throws ProvisionStoreException
    {
        File file = new File(root, UUID_FILE);
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(uuid);
        }
        catch (IOException ioe)
        {
            throw new ProvisionStoreException("Unable to save UUID", ioe);
        }
        finally
        {
            if (writer != null) try { writer.close(); } catch (IOException ignored) {}
        }
    }

    private ProvisionDirective loadCurrentProvisionDirective()
    {
        return loadProvisionDirective(CURRENT_FILE);
    }

    private ProvisionDirective loadNextProvisionDirective()
    {
        return loadProvisionDirective(NEXT_FILE);
    }

    private void saveCurrentProvisionDirective(ProvisionDirective nextProvisionDirective) throws ProvisionStoreException
    {
        saveProvisionDirective(nextProvisionDirective, CURRENT_FILE);
    }

    private void saveNextProvisionDirective(ProvisionDirective nextProvisionDirective) throws ProvisionStoreException
    {
        saveProvisionDirective(nextProvisionDirective, NEXT_FILE);
    }

    /**
     * Load the <code>ProvisionDirective</code> from a properties file.  If
     * any part of properties file is misformatted an instance created from
     * the default constructor will be returned.
     *
     * @param name the name of the properties file
     * @return the ProvisionDirective loaded from the properties file
     */
    @SuppressWarnings({"EmptyCatchBlock"})
    private ProvisionDirective loadProvisionDirective(String name)
    {
        File file = new File(root, name);

        if (file.exists())
        {
            Properties properties = new Properties();

            try
            {
                properties.load(new FileInputStream(file));

                long version = Long.valueOf((String) properties.get(VERSION_KEY));

                if (!properties.contains(BOOT_CLASS_KEY)) return new ProvisionDirective();
                String bootClass = (String) properties.get(BOOT_CLASS_KEY);

                Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
                int count = 0;
                while (true)
                {
                    String key = DIRECTIVE_BASE_KEY + count++;

                    if (!properties.contains(key)) break;

                    String[] tokens = ((String) properties.get(key)).split(":");

                    if (tokens.length != 2) return new ProvisionDirective();

                    entries.add(new ProvisionEntry(tokens[0], Long.valueOf(tokens[1])));
                }

                return new ProvisionDirective(version, bootClass, entries);
            }
            catch (NumberFormatException fallThrough)
            {
            }
            catch (IOException fallThrough)
            {
            }

        }
        return new ProvisionDirective();
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    private void saveProvisionDirective(ProvisionDirective directive, String name) throws ProvisionStoreException
    {
        File file = new File(root, name);
        Properties properties = new Properties();

        properties.put(VERSION_KEY, Long.toString(directive.getVersion()));
        properties.put(BOOT_CLASS_KEY, directive.getBootClass());

        int count = 0;
        for (ProvisionEntry entry : directive.getEntries())
        {
            properties.put(DIRECTIVE_BASE_KEY + count++, entry.getName() + ":" + entry.getVersion());
        }

        FileOutputStream writer = null;
        try
        {
            writer = new FileOutputStream(file);

            properties.store(writer, "Stored at " + new Date());
        }
        catch (IOException ioe)
        {
            throw new ProvisionStoreException("Unable to save ProvisionDirective to " + name, ioe);
        }
        finally
        {
            if (writer != null) try { writer.close(); } catch (IOException ignored) {}
        }
    }
}
