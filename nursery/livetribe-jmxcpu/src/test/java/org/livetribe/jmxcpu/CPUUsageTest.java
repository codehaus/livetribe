package org.livetribe.jmxcpu;

import junit.framework.TestCase;
/* 
 * This test class will need to be rewritten
 */
public class CPUUsageTest extends TestCase
{
    public static void emit(String s)
    {
	System.out.println("TRACE: " + s);
    }
   
   /**
     * @testng.test
     */
    public void testCPUUsage() throws Exception
    {
	CPUUsageMBean mbean = CPUUsage.getCPUUsage();

        if(mbean != null)
	{
            assertTrue(mbean.getSamplingTime() == 1000);
	    mbean.setSamplingTime(1001);
            assertTrue(mbean.getSamplingTime() == 1001);
	    assertTrue(mbean.getNumberOfProcessors()== 0);
            assertTrue(mbean.getTotalUserProcessCPUUsage() == 0); 
            assertTrue(mbean.getTotalSystemProcessCPUUsage() == 0);
            assertTrue(mbean.getTotalIdleProcessCPUUsage() == 0);
            assertTrue(mbean.getUserProcessCPUUsage() == null);
            assertTrue(mbean.getSystemProcessCPUUsage() == null);  
            assertTrue(mbean.getIdleProcessCPUUsage() == null);

            try
	    {
		mbean.retrieveCPUUsage();
                assertTrue(mbean.getNumberOfProcessors() != 0);
                /* 
                int i = 0;
        	while(i < 20)
		{
		    emit("getTotalUserProcessCPUUsage() == " + mbean.getTotalUserProcessCPUUsage());
		    emit("getTotalSystemProcessCPUUsage() == " + mbean.getTotalSystemProcessCPUUsage());
		    emit("getTotalIdleProcessCPUUsage() == " + mbean.getTotalIdleProcessCPUUsage());
                    i++;
		    mbean.retrieveCPUUsage();
		}
                */
            }
	    catch(Exception e)
	    {
		e.printStackTrace();
	    }
	}
	else
	{
	    emit("mbean == null");
	}
    }
}
