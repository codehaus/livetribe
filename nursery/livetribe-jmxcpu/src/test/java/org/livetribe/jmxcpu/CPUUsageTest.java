
package org.livetribe.jmxcpu;

/* This test class will need to be rewritten
 */
public class CPUUsageTest
{
    public static void emit(String s)
    {
	System.out.println("TRACE: " + s);
    }
    
    protected void assertEquals(Object expected, Object value)
    {
        if (expected != value)
	{
	    assert expected != null ? expected.equals(value) : value.equals(expected);
	}
    }

    /**
     * @testng.test
     */
    public void testCPUUsage() throws Exception
    {
	CPUUsageMBean mbean = CPUUsage.getCPUUsage();

        if(mbean != null)
	{
            assertEquals(mbean.getSamplingTime(), 1000);
	    mbean.setSamplingTime(1001);
            assertEquals(mbean.getSamplingTime(), 1001);
	    assertEquals(mbean.getNumberOfProcessors(), 0);
            assertEquals(mbean.getTotalUserProcessCPUUsage(), 0); 
            assertEquals(mbean.getTotalSystemProcessCPUUsage(), 0);
            assertEquals(mbean.getTotalIdleProcessCPUUsage(), 0);
            assertEquals(mbean.getUserProcessCPUUsage(), null);
            assertEquals(mbean.getSystemProcessCPUUsage(), null);  
            assertEquals(mbean.getIdleProcessCPUUsage(), null);

            try
	    {
		mbean.retrieveCPUUsage();
                assert mbean.getNumberOfProcessors() != 0;
                int i = 0;
        	while(i < 20)
		{
		    emit("getTotalUserProcessCPUUsage() == " + mbean.getTotalUserProcessCPUUsage());
		    emit("getTotalSystemProcessCPUUsage() == " + mbean.getTotalSystemProcessCPUUsage());
		    emit("getTotalIdleProcessCPUUsage() == " + mbean.getTotalIdleProcessCPUUsage());
                    i++;
		    mbean.retrieveCPUUsage();
		}
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
