/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.forma.boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @version $Rev$ $Date$
 */
public class PluginInfo implements IPluginInfo
{
    private static final String CLASS_FILE_EXTENSION = ".class";

    private File classesDirectory;
    private List<JarFile> libraryJars = new ArrayList<JarFile>();
    private File servicesFile;

    public void setClasses(File classesDir)
    {
        this.classesDirectory = classesDir;
    }

    public void addLibrary(JarFile library)
    {
        libraryJars.add(library);
    }

    public void addServices(File servicesFile)
    {
        this.servicesFile = servicesFile;
    }

    public byte[] getClassBytes(String className) throws ClassNotFoundException
    {
        String classPath = nameToPath(className);

        if (classesDirectory != null)
        {
            File classFile = new File(classesDirectory, classPath);
            if (classFile.exists()) return getClassBytes(classFile);
        }

        if (!libraryJars.isEmpty())
        {
            for (JarFile jar : libraryJars)
            {
                JarEntry entry = jar.getJarEntry(classPath);
                if (entry != null) return getClassBytes(jar, entry);
            }
        }

        return null;
    }

    private String nameToPath(String className)
    {
        return className.replace('.', '/').concat(CLASS_FILE_EXTENSION);
    }

    private byte[] getClassBytes(File classFile) throws ClassNotFoundException
    {
        try
        {
            int size = Long.valueOf(classFile.length()).intValue();
            FileInputStream stream = new FileInputStream(classFile);
            return getClassBytes(stream, size);
        }
        catch (IOException x)
        {
            throw (ClassNotFoundException)new ClassNotFoundException().initCause(x);
        }
    }

    private byte[] getClassBytes(JarFile jarFile, JarEntry jarEntry) throws ClassNotFoundException
    {
        try
        {
            int size = Long.valueOf(jarEntry.getSize()).intValue();
            InputStream stream = jarFile.getInputStream(jarEntry);
            return getClassBytes(stream, size);
        }
        catch (IOException x)
        {
            throw (ClassNotFoundException)new ClassNotFoundException().initCause(x);
        }
    }

    private byte[] getClassBytes(InputStream stream, int size) throws IOException
    {
        byte[] bytes = new byte[size];
        int offset = 0;
        while (offset < size)
        {
            offset += stream.read(bytes, offset, bytes.length - offset);
        }
        return bytes;
    }

    public URL getResource(String resourceName)
    {
        if (classesDirectory != null)
        {
            File resourceFile = new File(classesDirectory, resourceName);
            if (resourceFile.exists()) return getURL(resourceFile);
        }

        if (!libraryJars.isEmpty())
        {
            for (JarFile jarFile : libraryJars)
            {
                JarEntry jarEntry = jarFile.getJarEntry(resourceName);
                if (jarEntry != null) return getURL(jarFile, jarEntry);
            }
        }

        return null;
    }

    private URL getURL(File file)
    {
        try
        {
            return file.toURL();
        }
        catch (MalformedURLException x)
        {
            return null;
        }
    }

    private URL getURL(JarFile jarFile, JarEntry jarEntry)
    {
        File file = new File(jarFile.getName());
        StringBuilder builder = new StringBuilder("jar:");
        builder.append(getURL(file)).append("!/").append(jarEntry.getName());
        try
        {
            return new URL(builder.toString());
        }
        catch (MalformedURLException x)
        {
            return null;
        }
    }
}
