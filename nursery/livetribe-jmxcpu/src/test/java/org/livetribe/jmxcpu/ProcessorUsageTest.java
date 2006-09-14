package org.livetribe.jmxcpu;

import junit.framework.TestCase;
import java.lang.Thread;
import java.io.FileWriter;
import java.io.File;


public class ProcessorUsageTest extends TestCase
{
    public void emit(String s)
    {
        System.out.println("Trace: " + s);
    }
    
    /**
     * What this test does is to test "User Process" processor usage and
     * "Kernel Process" processor usage. The "User Process" is spiked by
     *  calculating primes and "Kernel Process" is spiked by doing 
     *  many "writes" on a created file.
     * @throws Exception
     */
    public void testProcessorUsage() throws Exception
    {
        ProcessorUsage usage = ProcessorUsage.getInstance();
        if( usage != null )
        {
            // Launch our parallel task to simulate user CPU usage.
            CountPrimes userUsageSpiker = new CountPrimes();
            WriteNonsense kernelUsageSpiker = new WriteNonsense();
            Thread primesThread = new Thread(userUsageSpiker);
            Thread writeThread = new Thread(kernelUsageSpiker);
            
            // (1) Start the thread that will spike the user process 
            primesThread.start();
            // (2) Get processor usage info
            int[] procUsage = usage.getProcessorUsage();
            // (3) Stop our "User Process" spiker thread
            userUsageSpiker.stopCounting();
            
            emit("Usage ["+procUsage[ProcessorUsage.USER_INDEX]+"]\t["+
                           procUsage[ProcessorUsage.SYSTEM_INDEX]+"]\t["+
                           procUsage[ProcessorUsage.IDLE_INDEX]+"]");
            
            // Our user CPU usage should not be 0
            assertTrue(procUsage[ProcessorUsage.USER_INDEX] != 0);
            // Everything must add up to 100%
            assertTrue((procUsage[ProcessorUsage.USER_INDEX] + 
                        procUsage[ProcessorUsage.SYSTEM_INDEX] + 
                        procUsage[ProcessorUsage.IDLE_INDEX]) == 100);
            
            // (1) Start the thread that will spike the Kernel process
            writeThread.start();
            // (2) Get processor usage info
            procUsage = usage.getProcessorUsage();
            // (3) Stop our "Kernel CPU Usage" spiker thread.
            kernelUsageSpiker.stopWriting();
            
            emit("Usage ["+procUsage[ProcessorUsage.USER_INDEX]+"]\t["+
                           procUsage[ProcessorUsage.SYSTEM_INDEX]+"]\t["+
                           procUsage[ProcessorUsage.IDLE_INDEX]+"]");
            // Our kernel CPU usage should not be 0
            assertTrue(procUsage[ProcessorUsage.SYSTEM_INDEX] != 0);
            // Everything must add up to 100%
            assertTrue((procUsage[ProcessorUsage.USER_INDEX] + 
                        procUsage[ProcessorUsage.SYSTEM_INDEX] + 
                        procUsage[ProcessorUsage.IDLE_INDEX]) == 100);
        
            // Normal use-case for getting all the values, from all the processors, from the same timeslice
            // TODO : Rewrite this part of test, don't know how to currently test this one.
            int[][] procUsages = usage.getProcessorsUsage();
        }
    }
    
    // This class is only used to spike the "User Process" usage.
    class CountPrimes implements Runnable
    {
        private boolean continueCalculation = true;
        // The simplest prime algorithm i can think of
        public void run()
        {
            int primeCount = 0;
            int i = 2;
            while(continueCalculation)
            {
                if(isPrime(i))
                    primeCount++;
                i++;
            }
            emit("Reached "+ primeCount+ " primes.");
        }
    
        // Gently stop our thread 
        public void stopCounting()
        {
            continueCalculation = false;
        }
        
        boolean isPrime(int n) 
        {        
            // 2 is the smallest prime
            if (n <= 2) 
                return n == 2;
            // even numbers other than 2 are not prime
            if (n % 2 == 0) 
                return false;
            // check odd divisors from 3
            // to the square root of n
            for (int i = 3, end = (int)Math.sqrt(n); i <= end; i += 2)
            {
                if (n % i == 0) 
                    return false;
            }

            return true;
        }
    }
    
    // Lets write something to a file, to spike the Kernel usage
    class WriteNonsense implements Runnable
    {
        private boolean continueWriting = true;
        public void run()
        {
            FileWriter outputStream = null;
            char [] msg = {'I',' ','S','P','I','K','E',' ','T','H','E',' ','K','E','R','N','E','L','\n'};
            try 
            {
                File f1 = new File("Nonsense.txt");
                outputStream = new FileWriter(f1);
                int numCounts = 0;
                while(continueWriting)
                {
                    outputStream.write(msg);
                    numCounts++;
                }
                emit("Reached "+ numCounts+ " write's.");
                f1.delete();
                outputStream.close();
            } 
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    
        // Gently stop our thread
        public void stopWriting()
        {
            continueWriting = false;
        }
    }
    
    // TODO : Need to add similar test as above for this.
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
