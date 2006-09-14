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

public class Win32ProcessorUsage extends SampleBasedProcessorUsage
{
    static 
    {
        System.loadLibrary("Win32ProcessorUsage");
    }
    
    public boolean supportsMultiprocessorUsageQuery()
    {
        return false;
    }

    public int[] getProcessorUsage() throws Exception
    {
        return getProcessorAverageUsage(getSamplingTime());
    }
    
    public int[][] getProcessorsUsage() throws Exception
    {
        return null;
    }
    
    private native int[] getProcessorAverageUsage(int samplingTime);
}
