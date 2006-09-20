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
public class MetricGroupDefinition extends Model
{
    private ApplicationDefinition[] applicationDefinitions;
    private String[] name;
    private String[] units;
    private short[] usage;
    private byte[][] id;

    public ApplicationDefinition[] getApplicationDefinitions()
    {
        return applicationDefinitions;
    }

    public void setApplicationDefinitions(ApplicationDefinition[] applicationDefinitions)
    {
        this.applicationDefinitions = applicationDefinitions;
    }

    public String[] getName()
    {
        return name;
    }

    public void setName(String[] name)
    {
        this.name = name;
    }

    public String[] getUnits()
    {
        return units;
    }

    public void setUnits(String[] units)
    {
        this.units = units;
    }

    public short[] getUsage()
    {
        return usage;
    }

    public void setUsage(short[] usage)
    {
        this.usage = usage;
    }

    public byte[][] getId()
    {
        return id;
    }

    public void setId(byte[][] id)
    {
        this.id = id;
    }
}
