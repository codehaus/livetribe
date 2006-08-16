package org.livetribe.jmxcpu;

public interface CPUUsageMBean
{
    /* getter for number of processors in the system */
    public int     getNumberOfProcessors();
    /* getter for user process usage */
    public float[] getUserProcessCPUUsage();
    /* getter for system process usage */
    public float[] getSystemProcessCPUUsage();
    /* getter for idle process usage */
    public float[] getIdleProcessCPUUsage();
    /* getter for total user process usage */
    public float   getTotalUserProcessCPUUsage();
    /* getter for total system process usage */
    public float   getTotalSystemProcessCPUUsage();
    /* getter for total idle process usage */
    public float   getTotalIdleProcessCPUUsage();
    /* getter and setter for the amount of sampling time */
    public void    setSamplingTime(long samplingTime);
    public long    getSamplingTime(); 
    /* operation to retrieve current CPU information */
    public void    retrieveCPUUsage() throws Exception;
}
