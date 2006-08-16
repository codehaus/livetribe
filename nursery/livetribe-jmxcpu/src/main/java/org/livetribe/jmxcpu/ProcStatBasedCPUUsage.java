package org.livetribe.jmxcpu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

class ProcStatBasedCPUUsage extends CPUUsage
{
    private static final String CPU = "cpu";
    private static final String PROC_STAT = "/proc/stat";
    private static final int    USER_INDEX = 0;
    private static final int    SYSTEM_INDEX = 1;
    private static final int    IDLE_INDEX = 2;
   
    public void retrieveCPUUsage() throws Exception
    {
	try
        {
	    /* Get first sample */
            Vector result1 = retrieveCPUUsageSnapshot();

	    /* Sleep "samplingTime" amount of time, this will determine the 
	     * granularity of our sample */
	    Thread.sleep(getSamplingTime());

	    /* Get second sample */
            Vector result2 = retrieveCPUUsageSnapshot();

	    /* Retrieve total user, system, idle CPU usage based on sample2 & sample1 */
            long[] result1Temp = (long[]) result1.get(0); 
	    long[] result2Temp = (long[]) result2.get(0);

	    long userModeElapse = result2Temp[USER_INDEX] - result1Temp[USER_INDEX];
	    long systemModeElapse = result2Temp[SYSTEM_INDEX] - result1Temp[SYSTEM_INDEX];
	    long idleModeElapse = result2Temp[IDLE_INDEX] - result1Temp[IDLE_INDEX];
        
	    long denominator = (result2Temp[USER_INDEX] + result2Temp[SYSTEM_INDEX] + result2Temp[IDLE_INDEX]) -
		               (result1Temp[USER_INDEX] + result1Temp[SYSTEM_INDEX] + result1Temp[IDLE_INDEX]);
        
	    setTotalUserProcessCPUUsage((float) (((float)userModeElapse/(float)denominator) * 100));
	    setTotalSystemProcessCPUUsage((float) (((float)systemModeElapse/(float)denominator) * 100));
	    setTotalIdleProcessCPUUsage((float) (((float)idleModeElapse/(float)denominator) * 100));

	    /* Retrieve per CPU user, system, idle CPU usage based on sample2 & sample1.
	     * Each CPU sample is in the result vector of retrieveCPUUsageSnapshot()
	     * Refer to comments on retrieveCPUUsageSnapshot() for the structure of vector.
	     */
	    int numberOfCPUs = result1.size() - 1;
	    setNumberOfProcessors(numberOfCPUs);
	    float [] perCPUUserUsage = new float[numberOfCPUs];
	    float [] perCPUSystemUsage = new float[numberOfCPUs];
	    float [] perCPUIdleUsage = new float[numberOfCPUs];
        
	    for(int i = 1; i <= numberOfCPUs; i++)
	    {
		result1Temp = (long[]) result1.get(i);
		result2Temp = (long[]) result2.get(i);

		userModeElapse = result2Temp[USER_INDEX] - result1Temp[USER_INDEX];
		systemModeElapse = result2Temp[SYSTEM_INDEX] - result1Temp[SYSTEM_INDEX];
		idleModeElapse = result2Temp[IDLE_INDEX] - result1Temp[IDLE_INDEX];

		denominator = (result2Temp[USER_INDEX] + result2Temp[SYSTEM_INDEX] + result2Temp[IDLE_INDEX]) -
		              (result1Temp[USER_INDEX] + result1Temp[SYSTEM_INDEX] + result1Temp[IDLE_INDEX]);

		perCPUUserUsage[i-1] = (float) (((float)userModeElapse/(float)denominator) * 100);
		perCPUSystemUsage[i-1] = (float) (((float)systemModeElapse/(float)denominator) * 100);
		perCPUIdleUsage[i-1] = (float) (((float)idleModeElapse/(float)denominator) * 100);
	    }

	    setUserProcessCPUUsage(perCPUUserUsage);
	    setSystemProcessCPUUsage(perCPUSystemUsage);
	    setIdleProcessCPUUsage(perCPUIdleUsage);
	}
	catch(Exception e)
	{
	    throw e;
	}
    }

   /** 
     * Returns a vector of long array of length 3 with the ff structure
     *             Usermode   SystemMode   IdleMode   
     * CPU_TOTAL   [      ]   [      ]     [      ]
     * CPU 0       [      ]   [      ]     [      ]
     * CPU 1       [      ]   [      ]     [      ]
     * ...           ...        ...          ...
     * CPU N       [      ]   [      ]     [      ]
     */
    private Vector retrieveCPUUsageSnapshot() throws Exception
    {
	try
	{
	    /* Open /proc/stat */
	    File file = new File(PROC_STAT);
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
         
	    /* Parse each line, retrieve data and put into vector.
             * Because of the structure of /proc/stat, we can be sure
             * that resultVector[0] will have the total CPU usage information. */
	    Vector resultVector = new Vector();
	    String line = br.readLine();
	    while(line != null)
	    {
		if(line.startsWith(CPU))
		    resultVector.add(getCPUUsageFromString(line));
		line = br.readLine();
	    }

            br.close();
	    return resultVector;
	}
	catch(Exception e)
	{
	    throw e;
	}
    }

    public long[] getCPUUsageFromString(String procStatLine)
    {
        StringTokenizer tokenizer = new StringTokenizer(procStatLine);
        /* Skip the label, i.e. cpu | cpu0 | cpu1 | cpu2 */
        tokenizer.nextToken();

        long[] usage = new long[3];
	/* Get normal processes executing in user mode */
        usage[0] = Integer.parseInt(tokenizer.nextToken());
        /* Get niced processes executing in user mode */
        usage[0] += Integer.parseInt(tokenizer.nextToken());
        /* Get processes executing in kernel mode */
        usage[1] = Integer.parseInt(tokenizer.nextToken());
        /* Get processes that are twiddling thumbs (idle processes) */
        usage[2] = Integer.parseInt(tokenizer.nextToken());
     
        System.out.println("Usage == "+usage[0]+" "+usage[1]+" "+usage[2]);  
 
        return usage;
    }
}


