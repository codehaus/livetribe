/**
 *
 * Copyright 2008-2011(C) The original author or authors
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
package org.livetribe.s3.model;

import java.util.Date;

/**
 * @version $Revision$ $Date$
 */
public class ObjectInfo
{
    private String key;
    private Date lastModified;
    private String eTag;
    private long size;
    private Owner owner;
    private StorageClass storageClass;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public String getETag()
    {
        return eTag;
    }

    public void setETag(String eTag)
    {
        this.eTag = eTag;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public Owner getOwner()
    {
        return owner;
    }

    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }

    public StorageClass getStorageClass()
    {
        return storageClass;
    }

    public void setStorageClass(StorageClass storageClass)
    {
        this.storageClass = storageClass;
    }
}
