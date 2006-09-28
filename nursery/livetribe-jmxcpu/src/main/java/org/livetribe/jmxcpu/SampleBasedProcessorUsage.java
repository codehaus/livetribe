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

import java.util.logging.Level;

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
    
    /*
     * This is where the actual calculation for the collected CPU usage snapshots 
     * based on the sampling time takes place.
     */
    public int[] getProcessorUsage() throws Exception
    {
        long[] snapshot1 = this.getProcessorSnapshot();
        Thread.sleep(getSamplingTime());
        long[] snapshot2 = this.getProcessorSnapshot();
        
        long userModeElapse = snapshot2[USER_INDEX] - snapshot1[USER_INDEX];
        long kernelModeElapse = snapshot2[SYSTEM_INDEX] - snapshot1[SYSTEM_INDEX];
        
        long denominator = (snapshot2[USER_INDEX] + snapshot2[SYSTEM_INDEX] + snapshot2[IDLE_INDEX]) - 
                           (snapshot1[USER_INDEX] + snapshot1[SYSTEM_INDEX] + snapshot1[IDLE_INDEX]);
        
        if (logger.isLoggable(Level.FINE))
            logger.fine("User Process Usage : [" + ((float)userModeElapse / (float)denominator) * 100 + 
                        "]\tKernel Process Usage[" + ((float)kernelModeElapse / (float)denominator) * 100 + "]");
        
        int[] info = new int[3];
        info[SYSTEM_INDEX] = Math.round(((float)kernelModeElapse / (float)denominator) * 100);
        info[USER_INDEX] = Math.round(((float)userModeElapse / (float)denominator) * 100);
        info[IDLE_INDEX] = 100 - (info[SYSTEM_INDEX] + info[USER_INDEX]);
        
        return info;
    }
    
    /*
     * This is where the actual calculation for the collected CPU usage snapshots 
     * based on the sampling time takes place. This calculation is for devices
     * that can report multi processor usages.
     */
    public int[][] getProcessorsUsage() throws Exception
    {
        int[][] usages = null;
        if(supportsMultiprocessorUsageQuery())
        {
            long userModeElapse, kernelModeElapse, denominator;

            long[][] snapshot1 = this.getProcessorsSnapshot();
            Thread.sleep(getSamplingTime());
            long[][] snapshot2 = this.getProcessorsSnapshot();

            int numberOfCPUs = snapshot1.length;        
            usages = new int[numberOfCPUs][3];

            for (int i=0; i < numberOfCPUs; i++)
            {
                userModeElapse = snapshot2[i][USER_INDEX] - snapshot1[i][USER_INDEX];
                kernelModeElapse = snapshot2[i][SYSTEM_INDEX] - snapshot1[i][SYSTEM_INDEX];

                denominator = (snapshot2[i][USER_INDEX] + snapshot2[i][SYSTEM_INDEX] + snapshot2[i][IDLE_INDEX]) - 
                              (snapshot1[i][USER_INDEX] + snapshot1[i][SYSTEM_INDEX] + snapshot1[i][IDLE_INDEX]);

                usages[i][SYSTEM_INDEX] = Math.round(((float)kernelModeElapse / (float)denominator) * 100);
                usages[i][USER_INDEX] = Math.round(((float)userModeElapse / (float)denominator) * 100);
                usages[i][IDLE_INDEX] = 100 - (usages[i][SYSTEM_INDEX] + usages[i][USER_INDEX]);
            }
        }
        
        return usages;
    }
    
    protected abstract long[] getProcessorSnapshot() throws Exception;
    protected abstract long[][] getProcessorsSnapshot() throws Exception;
}
