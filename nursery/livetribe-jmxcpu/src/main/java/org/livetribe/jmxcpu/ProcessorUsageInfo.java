package org.livetribe.jmxcpu;

/*
 * Setter functions are package private because only
 * implementing class within this package should know them.
 */
public class ProcessorUsageInfo
{
    private int kernelUsage;
    private int userUsage;
    private int idleUsage;
    
    ProcessorUsageInfo(int kernelUsage , int userUsage, int idleUsage)
    {
        this.kernelUsage = kernelUsage;
        this.userUsage = userUsage;
        this.idleUsage = idleUsage;
    }
    
    public int getIdleUsage()
    {
        return idleUsage;
    }
    
    void setIdleUsage(int idleUsage)
    {
        this.idleUsage = idleUsage;
    }
    
    public int getKernelUsage()
    {
        return kernelUsage;
    }
    
    void setKernelUsage(int kernelUsage)
    {
        this.kernelUsage = kernelUsage;
    }
    
    public int getUserUsage()
    {
        return userUsage;
    }
    
    void setUserUsage(int userUsage)
    {
        this.userUsage = userUsage;
    }
}
