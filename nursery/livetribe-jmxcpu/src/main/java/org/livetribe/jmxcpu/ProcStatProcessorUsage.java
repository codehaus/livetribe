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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;


class ProcStatProcessorUsage extends ProcessorUsage
{
    private static final String CPU = "cpu";
    
    private static final String PROC_STAT = "/proc/stat";
    
    private static final int USER_INDEX = 0;
    
    private static final int SYSTEM_INDEX = 1;
    
    private static final int IDLE_INDEX = 2;
    
    private int samplingTime = 1000;
    
    public int getSamplingTime()
    {
        return samplingTime;
    }
    
    public void setSamplingTime(int samplingTime)
    {
        this.samplingTime = samplingTime;
    }
    
    public boolean supportsMultiprocessorUsageQuery()
    {
        return true;
    }
    
    public int getProcessorAverageKernelUsage() throws Exception
    {
        return getProcessorAverageUsage(SYSTEM_INDEX);
    }
    
    public int getProcessorAverageUserUsage() throws Exception
    {
        return getProcessorAverageUsage(USER_INDEX);
    }
    
    public int getProcessorAverageIdleUsage() throws Exception
    {
        return getProcessorAverageUsage(IDLE_INDEX);
    }
    
    public int[] getProcessorsKernelUsage() throws Exception
    {
        return getProcessorsUsage(SYSTEM_INDEX);
    }
    
    public int[] getProcessorsUserUsage() throws Exception
    {
        return getProcessorsUsage(USER_INDEX);
    }
    
    public int[] getProcessorsIdleUsage() throws Exception
    {
        return getProcessorsUsage(IDLE_INDEX);
    }
    
    public ProcessorUsageInfo getProcessorAverageUsageInfo() throws Exception
    {
        long[] snapshot1 = this.getProcessorAverageSnapshot();
        Thread.sleep(samplingTime);
        long[] snapshot2 = this.getProcessorAverageSnapshot();
        
        long userModeElapse = snapshot2[USER_INDEX] - snapshot1[USER_INDEX];
        long kernelModeElapse = snapshot2[SYSTEM_INDEX] - snapshot1[SYSTEM_INDEX];
        long idleModeElapse = snapshot2[IDLE_INDEX] - snapshot1[IDLE_INDEX];
        
        long denominator = (snapshot2[USER_INDEX] + snapshot2[SYSTEM_INDEX] + snapshot2[IDLE_INDEX]) - 
        (snapshot1[USER_INDEX] + snapshot1[SYSTEM_INDEX] + snapshot1[IDLE_INDEX]);
        
        ProcessorUsageInfo info = new ProcessorUsageInfo((int)((kernelModeElapse / denominator) * 100), 
                                                         (int)((userModeElapse / denominator) * 100),
                                                         (int)((idleModeElapse / denominator) * 100));
        
        return info;
    }
    
    public ProcessorUsageInfo[] getProcessorsUsageInfo() throws Exception
    {
        long[] result1Temp, result2Temp;
        long userModeElapse, kernelModeElapse, idleModeElapse, denominator;
        
        List snapshot1 = this.getProcessorsSnapshot();
        Thread.sleep(samplingTime);
        List snapshot2 = this.getProcessorsSnapshot();
        
        int numberOfCPUs = snapshot1.size() - 1;        
        ProcessorUsageInfo[] usage = new ProcessorUsageInfo[numberOfCPUs];
        
        for (int i = 1; i <= numberOfCPUs; i++)
        {
            result1Temp = (long[]) snapshot1.get(i);
            result2Temp = (long[]) snapshot2.get(i);
            
            userModeElapse = result2Temp[USER_INDEX] - result1Temp[USER_INDEX];
            kernelModeElapse = result2Temp[SYSTEM_INDEX] - result1Temp[SYSTEM_INDEX];
            idleModeElapse = result2Temp[IDLE_INDEX] - result1Temp[IDLE_INDEX];
            
            denominator = (result2Temp[USER_INDEX] + result2Temp[SYSTEM_INDEX] + result2Temp[IDLE_INDEX]) - 
            (result1Temp[USER_INDEX] + result1Temp[SYSTEM_INDEX] + result1Temp[IDLE_INDEX]);
            
            usage[i - 1] = new ProcessorUsageInfo((int)((kernelModeElapse / denominator) * 100), 
                                                  (int)((userModeElapse / denominator) * 100),
                                                  (int)((idleModeElapse / denominator) * 100));
        }
        
        return usage;
    }
    
    private int getProcessorAverageUsage(int index) throws Exception
    {
        long[] snapshot1 = this.getProcessorAverageSnapshot();
        Thread.sleep(samplingTime);
        long[] snapshot2 = this.getProcessorAverageSnapshot();
        
        long elapse = snapshot2[index] - snapshot1[index];
        
        long denominator = (snapshot2[USER_INDEX] + snapshot2[SYSTEM_INDEX] + snapshot2[IDLE_INDEX]) - 
        (snapshot1[USER_INDEX] + snapshot1[SYSTEM_INDEX] + snapshot1[IDLE_INDEX]);
        
        return (int)((elapse / denominator) * 100);
    }
    
    private int[] getProcessorsUsage(int index) throws Exception
    {
        long[] result1Temp = null, result2Temp = null;
        long elapse = 0, denominator = 0;
        
        List snapshot1 = this.getProcessorsSnapshot();
        Thread.sleep(samplingTime);
        List snapshot2 = this.getProcessorsSnapshot();
        
        int numberOfCPUs = snapshot1.size() - 1;        
        int[] usage = new int[numberOfCPUs];
        
        for (int i = 1; i <= numberOfCPUs; i++)
        {
            result1Temp = (long[]) snapshot1.get(i);
            result2Temp = (long[]) snapshot2.get(i);
            
            elapse = result2Temp[index] - result1Temp[index];
            
            denominator = (result2Temp[USER_INDEX] + result2Temp[SYSTEM_INDEX] + result2Temp[IDLE_INDEX]) - 
                          (result1Temp[USER_INDEX] + result1Temp[SYSTEM_INDEX] + result1Temp[IDLE_INDEX]);
            
            usage[i - 1] = (int)((elapse / denominator) * 100);
        }
        
        return usage;
    }
    
    /**
     * Returns an array of length 3 with the processor values.
     * Use the index stand-in USER_INDEX, SYSTEM_INDEX, IDLE_INDEX
     * to get the appropriate values.
     *     
     */
    private long[] getProcessorAverageSnapshot() throws Exception
    {
        List usage = retrieveCPUUsageSnapshot();
        return (long[]) usage.get(0);
    }
    
    /**
     * Returns a List of long array of length 3 with the ff structure 
     *           Usermode  SystemMode IdleMode 
     * CPU 0        [ ]       [ ]       [ ] 
     * CPU 1        [ ]       [ ]       [ ]
     * ...          ...       ...       ... 
     * CPU N        [ ]       [ ]       [ ]
     */
    private List getProcessorsSnapshot() throws Exception
    {
        List usage = retrieveCPUUsageSnapshot();
        // return the second up to the last element
        return usage.subList(1, usage.size());
    }
    
    /**
     * Returns a List of long array of length 3 with the ff structure 
     *           Usermode  SystemMode IdleMode 
     * CPU_TOTAL    [ ]       [ ]       [ ] 
     * CPU 0        [ ]       [ ]       [ ] 
     * CPU 1        [ ]       [ ]       [ ]
     * ...          ...       ...       ... 
     * CPU N        [ ]       [ ]       [ ]
     */
    private List retrieveCPUUsageSnapshot() throws Exception
    {
        /* Open /proc/stat */
        File file = new File(PROC_STAT);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
        
        /*
         * Parse each line, retrieve data and put into List. Because of the
         * structure of /proc/stat, we can be sure that resultList[0] will
         * have the total CPU usage information.
         */
        List resultList = new Vector();
        String line = br.readLine();
        while (line != null)
        {
            if (line.startsWith(CPU))
                resultList.add(getCPUUsageFromString(line));
            line = br.readLine();
        }
        
        br.close();
        
        return resultList;
    }
    
    private long[] getCPUUsageFromString(String procStatLine)
    {
        StringTokenizer tokenizer = new StringTokenizer(procStatLine);
        /* Skip the label, i.e. cpu | cpu0 | cpu1 | cpu2 */
        String processor = tokenizer.nextToken();
        
        long[] usage = new long[3];
        /* Get normal processes executing in user mode */
        usage[0] = Integer.parseInt(tokenizer.nextToken());
        /* Get niced processes executing in user mode */
        usage[0] += Integer.parseInt(tokenizer.nextToken());
        /* Get processes executing in kernel mode */
        usage[1] = Integer.parseInt(tokenizer.nextToken());
        /* Get processes that are twiddling thumbs (idle processes) */
        usage[2] = Integer.parseInt(tokenizer.nextToken());
        
        if (logger.isLoggable(Level.FINE))
            logger.fine("CPU Usage(" + processor + ") == " + usage[0] + " " + usage[1] + " " + usage[2]);
        
        return usage;
    }
}
