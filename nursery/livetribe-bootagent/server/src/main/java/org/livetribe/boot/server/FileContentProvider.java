/**
 *
 * Copyright 2008-2009 (C) The original author or authors
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
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jcip.annotations.ThreadSafe;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ContentProvider;


/**
 * The <code>FileContentProvider</code> is an implementation of
 * <code>ContentProvider</code> backed by a file system.
 * <p/>
 * The first level of directories correspond to the names of the content and so
 * the directory names match the content names that they represent.  Within
 * those directies are the collection of versions of that content.  They are of
 * the format <code><i>version</i>.jar</code>.  For example, content with the
 * name bar has three versions and the content named foo has two:
 * <p/>
 * <blockquote><pre>
 *     bar/
 *       1.jar
 *       2.jar
 *       3.jar
 *     foo/
 *       1.jar
 *       2.jar
 * </pre></blockquote>
 *
 * @version $Revision$ $Date$
 */
@ThreadSafe
public class FileContentProvider implements ContentProvider
{
    private final static String CLASS_NAME = FileContentProvider.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final File resources;

    /**
     * @param resources the root directory of the content that this provider
     *                  delivers.  This directory must already exist and its
     *                  contents must be in the format described in this class'
     *                  description.
     */
    public FileContentProvider(File resources)
    {
        if (resources == null) throw new IllegalArgumentException("Directory cannot be null");
        if (!resources.exists() || !resources.isDirectory()) throw new IllegalArgumentException("Directory does not exist or is a file");

        if (LOGGER.isLoggable(Level.CONFIG)) LOGGER.config("resources: " + resources);

        this.resources = resources;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream pleaseProvide(String name, long version) throws BootException
    {
        LOGGER.entering(CLASS_NAME, "pleaseProvide", new Object[]{name, version});

        if (name == null) throw new BootException("Content name cannot be null");
        if (version < 0) throw new BootException("Content version cannot be negative");

        File directory = new File(resources, name);

        if (!directory.exists() || !directory.isDirectory()) throw new BootException("Directory for resource does not exist");

        File resource = new File(directory, Long.toString(version) + ".jar");

        if (!resource.exists() || !resource.isFile()) throw new BootException("Version for resource does not exist");

        try
        {
            LOGGER.exiting(CLASS_NAME, "pleaseProvide", resource.toURL());

            return resource.toURL().openStream();
        }
        catch (IOException ioe)
        {
            throw new BootException("Unable to open stream for resource", ioe);
        }
    }
}
