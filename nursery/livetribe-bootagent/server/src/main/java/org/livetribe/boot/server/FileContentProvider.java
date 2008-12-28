/**
 *
 * Copyright 2008 (C) The original author or authors
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

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ContentProvider;


/**
 * @version $Revision$ $Date$
 */
public class FileContentProvider implements ContentProvider
{
    private final File resources;

    public FileContentProvider(File directory) throws IOException
    {
        if (directory == null) throw new IllegalArgumentException("Directory cannot be null");
        if (!directory.exists() || !directory.isDirectory()) throw new IllegalArgumentException("Directory does not exist or is a file");

        this.resources = directory;
    }

    public InputStream pleaseProvide(String name, long version) throws BootException
    {
        File directory = new File(resources, name);

        if (!directory.exists() || !directory.isDirectory()) throw new BootException("Directory for resource does not exist");

        File resource = new File(directory, Long.toString(version) + ".jar");

        if (!resource.exists() || !resource.isFile()) throw new BootException("Version for resource does not exist");

        try
        {
            return resource.toURL().openStream();
        }
        catch (IOException ioe)
        {
            throw new BootException("Unable to open stream for resource", ioe);
        }
    }
}
