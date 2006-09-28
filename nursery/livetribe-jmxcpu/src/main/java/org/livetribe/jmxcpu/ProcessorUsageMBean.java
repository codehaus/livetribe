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
     * The indexes to the array are as follows
     *      User Process   == 0 
     *      System Process == 1 
     *      Idle Process   == 2
     */
    public int[] getProcessorUsage() throws Exception;
    
    /**
     * Returns a matrix of Processor Usage values if supported by the
     * underlying operating system.
     * The structure of the matrix is as follows
     * 
     *         User Process System Process   Idle Index 
     * CPU 0        [ ]          [ ]             [ ] 
     * CPU 1        [ ]          [ ]             [ ]
     * ...          ...          ...             ... 
     * CPU N        [ ]          [ ]             [ ]
     * 
     */
    public int[][] getProcessorsUsage() throws Exception; 
}
