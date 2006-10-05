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

public abstract class ProcessorUsage implements ProcessorUsageMBean
{
    protected final Logger logger = Logger.getLogger(getClass().getName());
    
    private static final String OS_LINUX = "Linux";
    private static final String OS_WINXP = "Windows XP";
    
    public static final int USER_INDEX = 0;
    public static final int SYSTEM_INDEX = 1;
    public static final int IDLE_INDEX = 2;
    
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
        if(System.getProperty("os.name").equals(OS_WINXP))
            return new NativeProcessorUsage();
        // TODO : return the correct subclass for other OS's
        return null;
    }
    
    public abstract boolean supportsMultiprocessorUsageQuery(); 
    
    public abstract int[] getProcessorUsage() throws Exception;
    
    public abstract int[][] getProcessorsUsage() throws Exception; 
    
    public abstract ProcessorUsageInfo getProcessorAverageUsageInfo() throws Exception;
    
    public abstract ProcessorUsageInfo[] getProcessorsUsageInfo() throws Exception;
}
