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

public abstract class SampleBasedProcessorUsage extends ProcessorUsage
{   
    private int samplingTime = 1000;
    
    public int getSamplingTime()
    {
        return samplingTime;
    }
    
    public void setSamplingTime(int samplingTime)
    {
        this.samplingTime = samplingTime;
    }

    public ProcessorUsageInfo getProcessorAverageUsageInfo() throws Exception
    {
        int[] usage =  getProcessorUsage();
        ProcessorUsageInfo info = null;
        if( usage != null && usage.length == 3 )
        {
            info = new ProcessorUsageInfo(usage[SYSTEM_INDEX], usage[USER_INDEX], usage[IDLE_INDEX]);
        }
        
        return info;
    }

    public ProcessorUsageInfo[] getProcessorsUsageInfo() throws Exception
    {
        int[][] infos =  getProcessorsUsage();
        ProcessorUsageInfo[] processorInfo = null;
        if((infos != null) && (infos.length > 0))
        {
            processorInfo = new ProcessorUsageInfo[infos.length];
            for(int i = 0; i < infos.length; i++)
                processorInfo[i] = new ProcessorUsageInfo(infos[i][SYSTEM_INDEX], infos[i][USER_INDEX],infos[i][IDLE_INDEX]);
        }
        
        return processorInfo;
    }
}
