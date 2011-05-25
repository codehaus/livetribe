/**
 *
 * Copyright 2008-2011 (C) The original author or authors
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

import java.util.List;

/**
 * @version $Revision$ $Date$
 */
public class BucketListing
{
    List<ObjectInfo> objects;
    List<String> commonPrefixes;

    public List<ObjectInfo> getObjects()
    {
        return objects;
    }

    public void setObjects(List<ObjectInfo> objects)
    {
        this.objects = objects;
    }

    public List<String> getCommonPrefixes()
    {
        return commonPrefixes;
    }

    public void setCommonPrefixes(List<String> commonPrefixes)
    {
        this.commonPrefixes = commonPrefixes;
    }
}
