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

import java.util.logging.Logger;

public abstract class ProcessorUsage
{
    protected final Logger logger = Logger.getLogger(getClass().getName());
    
    private static final String OS_LINUX = "Linux";
    private static final String OS_WINXP = "Windows XP";
    
    /**
     * Returns the correct instance of ProcessorUsage class based on the
     * operating system its running in. Can return null if a ProcessorUsage
     * class hasn't been implemented for that particular platform.
     * @return 
     */
    public static ProcessorUsage getInstance()
    {
        /* if platform is Linux-based */
        if(System.getProperty("os.name").equals(OS_LINUX))
            return new ProcStatProcessorUsage();
        // TODO : return the correct subclass for other OS's
        return null;
    }
    
    /**
     * This method will return a boolean indicating whether this 
     * particular ProcessorUsage class can return information regarding
     * its multiple processors if it has one.
     */
    public abstract boolean supportsMultiprocessorUsageQuery(); 
    
    public abstract int getProcessorAverageKernelUsage() throws Exception;
    
    public abstract int getProcessorAverageUserUsage() throws Exception;
    
    public abstract int getProcessorAverageIdleUsage() throws Exception;
    
    public abstract int[] getProcessorsKernelUsage() throws Exception;
    
    public abstract int[] getProcessorsUserUsage() throws Exception;
    
    public abstract int[] getProcessorsIdleUsage() throws Exception;
    
    public abstract ProcessorUsageInfo getProcessorAverageUsageInfo() throws Exception;
    
    public abstract ProcessorUsageInfo[] getProcessorsUsageInfo() throws Exception;
}
