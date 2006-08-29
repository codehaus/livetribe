package org.livetribe.jmxcpu;

import junit.framework.TestCase;

/* 
 * This test class will need to be rewritten
 */
public class ProcessorUsageTest extends TestCase
{
    public void testCPUUsageSingleQuery() throws Exception
    {
        ProcessorUsage usage = ProcessorUsage.getInstance();
        
        // Normal use-case for getting single values
        int kernelTime = usage.getProcessorAverageKernelUsage();
        int userTime = usage.getProcessorAverageUserUsage();
        int idleTime = usage.getProcessorAverageIdleUsage();
        
        // Normal use-case for getting all the values from the same timeslice
        int[] procsKernelTime = usage.getProcessorsKernelUsage();
        int[] procsUserTime = usage.getProcessorsKernelUsage();
        int[] procsIdleTime = usage.getProcessorsKernelUsage();
        
    }
    
    public void testProcessorUsageUsingPojo() throws Exception
    {
        ProcessorUsage usage = ProcessorUsage.getInstance();
        
        ProcessorUsageInfo info = usage.getProcessorAverageUsageInfo();
        if (usage.supportsMultiprocessorUsageQuery())
        {
            ProcessorUsageInfo[] infos = usage.getProcessorsUsageInfo();
        }
    }
}
