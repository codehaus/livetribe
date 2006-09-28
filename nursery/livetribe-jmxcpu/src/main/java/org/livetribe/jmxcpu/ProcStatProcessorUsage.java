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
    
    /**
     * Returns the first element of the List returned by 
     * retrieveCPUUsageSnapshot().
     */
    protected long[] getProcessorSnapshot() throws Exception
    {
        List usage = retrieveCPUUsageSnapshot();
        return (long[]) usage.get(0);
    }
    
    /**
     * Returns a slice of the List returned by retrieveCPUUsageSnapshot().
     * The slice starts from 1 to the last index
     */
    protected long[][] getProcessorsSnapshot() throws Exception
    {
        List usage = retrieveCPUUsageSnapshot();
        long [][] cpusInfo = null;
        int size = usage.size();
        if((usage != null) && (size > 1))
        {
            // The slice starts from 1 to the last element,  
            // so our array needs to be less 1
            cpusInfo = new long[size-1][3];
            for(int i=1; i < usage.size(); i++)
            {
                cpusInfo[i-1] = (long []) usage.get(i); 
            }
        }
        
        return cpusInfo;
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
            logger.fine("CPU Usage(" + processor + ") == [" + usage[0] + "] [" + usage[1] + "] [" + usage[2]+"]");
        
        return usage;
    }
}
