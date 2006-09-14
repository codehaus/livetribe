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
import java.util.ArrayList;
import java.util.logging.Level;

public class ProcStatProcessorUsage extends SampleBasedProcessorUsage
{
    private static final String CPU = "cpu";
    
    private static final String PROC_STAT = "/proc/stat";
    
    public boolean supportsMultiprocessorUsageQuery()
    {
        return true;
    }
    
    public int[] getProcessorUsage() throws Exception
    {
        long[] snapshot1 = this.getProcessorAverageSnapshot();
        Thread.sleep(getSamplingTime());
        long[] snapshot2 = this.getProcessorAverageSnapshot();
        
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
    
    public int[][] getProcessorsUsage() throws Exception
    {
        long[] result1Temp, result2Temp;
        long userModeElapse, kernelModeElapse, denominator;
        
        List snapshot1 = this.getProcessorsSnapshot();
        Thread.sleep(getSamplingTime());
        List snapshot2 = this.getProcessorsSnapshot();
        
        int numberOfCPUs = snapshot1.size() - 1;        
        int[][] usages = new int[numberOfCPUs][3];
        
        for (int i = 1; i <= numberOfCPUs; i++)
        {
            result1Temp = (long[]) snapshot1.get(i);
            result2Temp = (long[]) snapshot2.get(i);
            
            userModeElapse = result2Temp[USER_INDEX] - result1Temp[USER_INDEX];
            kernelModeElapse = result2Temp[SYSTEM_INDEX] - result1Temp[SYSTEM_INDEX];
            
            denominator = (result2Temp[USER_INDEX] + result2Temp[SYSTEM_INDEX] + result2Temp[IDLE_INDEX]) - 
                          (result1Temp[USER_INDEX] + result1Temp[SYSTEM_INDEX] + result1Temp[IDLE_INDEX]);
            
            usages[i-1][SYSTEM_INDEX] = Math.round(((float)kernelModeElapse / (float)denominator) * 100);
            usages[i-1][USER_INDEX] = Math.round(((float)userModeElapse / (float)denominator) * 100);
            usages[i-1][IDLE_INDEX] = 100 - (usages[i-1][SYSTEM_INDEX] + usages[i-1][USER_INDEX]);
        }
        
        return usages;
    }
    
    /**
     * Returns the first element of the List returned by 
     * retrieveCPUUsageSnapshot().
     */
    private long[] getProcessorAverageSnapshot() throws Exception
    {
        List usage = retrieveCPUUsageSnapshot();
        return (long[]) usage.get(0);
    }
    
    /**
     * Returns a slice of the List returned by retrieveCPUUsageSnapshot().
     * The slice starts from 1 to the last index
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
        List resultList = new ArrayList();
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
        usage[USER_INDEX] = Integer.parseInt(tokenizer.nextToken());
        /* Get niced processes executing in user mode */
        usage[USER_INDEX] += Integer.parseInt(tokenizer.nextToken());
        /* Get processes executing in kernel mode */
        usage[SYSTEM_INDEX] = Integer.parseInt(tokenizer.nextToken());
        /* Get processes that are twiddling thumbs (idle processes) */
        usage[IDLE_INDEX] = Integer.parseInt(tokenizer.nextToken());
        
        if (logger.isLoggable(Level.FINE))
            logger.fine("CPU Usage(" + processor + ") == " + usage[0] + " " + usage[1] + " " + usage[2]+"]");
        
        return usage;
    }
}
