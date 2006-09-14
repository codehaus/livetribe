package org.livetribe.jmxcpu;

public interface ProcessorUsageMBean
{
    /**
     * This method will return a boolean indicating whether this 
     * particular ProcessorUsage class can return information regarding
     * its multiple processors if it has one.
     */
    public abstract boolean supportsMultiprocessorUsageQuery();
    
    /**
     * Returns an array of length 3 with the processor values.
     * Use the predefined index USER_INDEX, SYSTEM_INDEX, IDLE_INDEX
     * to get the appropriate values.    
     */
    public int[] getProcessorUsage() throws Exception;
    
    /**
     * Returns a matrix of Processor Usage values if supported by the
     * underlying operating system.
     * The structure of the matrix is as follows
     * 
     *           USER_INDEX  SYSTEM_INDEX IDLE_INDEX 
     * CPU 0        [ ]          [ ]         [ ] 
     * CPU 1        [ ]          [ ]         [ ]
     * ...          ...          ...         ... 
     * CPU N        [ ]          [ ]         [ ]
     * 
     */
    public int[][] getProcessorsUsage() throws Exception; 
}
