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
package org.livetribe.s3.model;

import java.util.Date;


/**
 * @version $Revision$ $Date$
 */
public class S3Bucket
{
    private Owner owner;
    private String name;
    private Date creationDate;
    private String location;
    private AccessControlPolicy accessControlPolicy;
    private LoggingConfiguration loggingConfiguration;

    public Owner getOwner()
    {
        return owner;
    }

    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public AccessControlPolicy getAccessControlPolicy()
    {
        return accessControlPolicy;
    }

    public void setAccessControlPolicy(AccessControlPolicy accessControlPolicy)
    {
        this.accessControlPolicy = accessControlPolicy;
    }

    public LoggingConfiguration getLoggingConfiguration()
    {
        return loggingConfiguration;
    }

    public void setLoggingConfiguration(LoggingConfiguration loggingConfiguration)
    {
        this.loggingConfiguration = loggingConfiguration;
    }
}
