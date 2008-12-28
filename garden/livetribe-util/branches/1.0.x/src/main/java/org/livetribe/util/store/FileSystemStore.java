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
package org.livetribe.util.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * $Rev$
 */
public class FileSystemStore implements Store
{
    private File directory;
    private long capacity;
    private final Map items = new HashMap();

    public void setDirectory(File directory)
    {
        this.directory = directory;
    }

    public void setMaxCapacity(long maxCapacityBytes)
    {
        this.capacity = maxCapacityBytes;
    }

    public StoreItem write(InputStream stream) throws IOException
    {
        // TODO: implements check on max capacity

        File destination = File.createTempFile("smx_", ".zip", directory);
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] buffer = new byte[256];
        int read = -1;
        while ((read = stream.read(buffer)) >= 0) fos.write(buffer, 0, read);
        fos.close();

        FileStoreItem item = new FileStoreItem(destination);

        synchronized (items)
        {
            items.put(destination.getName(), item);
        }

        return item;
    }

    public StoreItem read(String name) throws IOException
    {
        synchronized (items)
        {
            return (StoreItem) items.get(name);
        }
    }
}
