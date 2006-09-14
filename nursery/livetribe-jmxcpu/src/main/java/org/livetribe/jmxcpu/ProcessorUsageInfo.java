/**
 *
 * Copyright 2005 (C) The original author or authors
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

package org.livetribe.jmxcpu;

/*
 * Setter functions are package private because only
 * implementing class within this package should know them.
 */
public class ProcessorUsageInfo
{
    private int systemUsage;
    private int userUsage;
    private int idleUsage;
    
    ProcessorUsageInfo(int systemUsage , int userUsage, int idleUsage)
    {
        this.systemUsage = systemUsage;
        this.userUsage = userUsage;
        this.idleUsage = idleUsage;
    }
    
    public int getIdleUsage()
    {
        return idleUsage;
    }
    
    void setIdleUsage(int idleUsage)
    {
        this.idleUsage = idleUsage;
    }
    
    public int getSystemUsage()
    {
        return systemUsage;
    }
    
    void setSystemUsage(int systemUsage)
    {
        this.systemUsage = systemUsage;
    }
    
    public int getUserUsage()
    {
        return userUsage;
    }
    
    void setUserUsage(int userUsage)
    {
        this.userUsage = userUsage;
    }
}
