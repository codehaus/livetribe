package org.livetribe.jmxcpu;

public abstract class CPUUsage implements CPUUsageMBean
{
    private static final String OS_LINUX = "Linux";
    private static final String OS_WINXP = "Windows XP";
    
    private float[] userProcessCPUUsage;
    private float[] systemProcessCPUUsage;
    private float[] idleProcessCPUUsage;
    private float   totalUserProcessCPUUsage;
    private float   totalSystemProcessCPUUsage;
    private float   totalIdleProcessCPUUsage;
    private int numberOfProcessors;
    private long samplingTime = 1000;
    
    public static CPUUsageMBean getCPUUsage()
    {
	/* if platform is Linux-based */
        if(System.getProperty("os.name").equals(OS_LINUX))
	    return new ProcStatBasedCPUUsage();
	else
	    return null;
    }

    public abstract void retrieveCPUUsage() throws Exception;

    public int getNumberOfProcessors()
    {
	return numberOfProcessors;
    }

    public void setSamplingTime(long samplingTime)
    {
	this.samplingTime = samplingTime;
    }

    public long getSamplingTime()
    {
	return samplingTime;
    }

    public float[] getUserProcessCPUUsage()
    {
	return userProcessCPUUsage;
    }

    public float[] getSystemProcessCPUUsage()
    {
	return systemProcessCPUUsage;
    }

    public float[] getIdleProcessCPUUsage()
    {
	return idleProcessCPUUsage;
    }

    public float   getTotalUserProcessCPUUsage()
    {
	return totalUserProcessCPUUsage;
    }

    public float   getTotalSystemProcessCPUUsage()
    {
	return totalSystemProcessCPUUsage;
    }

    public float   getTotalIdleProcessCPUUsage()
    {
	return totalIdleProcessCPUUsage;
    }
    
    /* Downgraded to "protected" because this is supposed to be 
     * used only by subclasses implementing the actual logic of 
     * retrieving CPU usage information.
     */
    protected void setNumberOfProcessors(int numberOfProcessors)
    {
	this.numberOfProcessors = numberOfProcessors;
    }

    protected void setUserProcessCPUUsage(float [] userProcessCPUUsage)
    {
	this.userProcessCPUUsage = userProcessCPUUsage;
    }

    protected void setSystemProcessCPUUsage(float [] systemProcessCPUUsage)
    {
	this.systemProcessCPUUsage = systemProcessCPUUsage;
    }
    
    protected void setIdleProcessCPUUsage(float [] idleProcessCPUUsage)
    {
	this.idleProcessCPUUsage = idleProcessCPUUsage;
    }

    protected void setTotalUserProcessCPUUsage(float totalUserProcessSPU)
    {
        this.totalUserProcessCPUUsage = totalUserProcessCPUUsage;
    }

    protected void setTotalSystemProcessCPUUsage(float totalSystemProcessCPUUsage)
    {
        this.totalSystemProcessCPUUsage = totalSystemProcessCPUUsage;
    }

    protected void setTotalIdleProcessCPUUsage(float totalIdleProcessCPUUsage)
    {
        this.totalIdleProcessCPUUsage = totalIdleProcessCPUUsage;
    }
}
