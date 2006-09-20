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
package org.livetribe.arm.connection.model;

/**
 * @version $Revision$ $Date$
 */
public class TransactionDefinition extends Model
{
    private String name;
    private String[] idNames;
    private String[] idValues;
    private String[] ctxNames;
    private String uri;
    private byte[] id;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String[] getIdNames()
    {
        return idNames;
    }

    public void setIdNames(String[] idNames)
    {
        this.idNames = idNames;
    }

    public String[] getIdValues()
    {
        return idValues;
    }

    public void setIdValues(String[] idValues)
    {
        this.idValues = idValues;
    }

    public String[] getCtxNames()
    {
        return ctxNames;
    }

    public void setCtxNames(String[] ctxNames)
    {
        this.ctxNames = ctxNames;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
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
