/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm40.model;

import java.util.Set;


/**
 * @version $Revision$ $Date$
 */
public class ApplicationDefinition extends ModelGuidIdBase
{
    private String name;
    private IdentityProperties identityProperties;
    private Set applications;
    private byte[] id;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public IdentityProperties getIdentityProperties()
    {
        return identityProperties;
    }

    public void setIdentityProperties(IdentityProperties identityProperties)
    {
        this.identityProperties = identityProperties;
    }

    public Set getApplications()
    {
        return applications;
    }

    public void setApplications(Set applications)
    {
        this.applications = applications;
    }

    public byte[] getId()
    {
        return id;
    }

    public void setId(byte[] id)
    {
        this.id = id;
    }
}
