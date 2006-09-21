
#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <windows.h>
#include "..\ProcessorUsage.h"

#ifdef DEBUG_BUILD
   #define DEBUGLOG(x) x 
#else
   #define DEBUGLOG(x)
#endif

typedef BOOL _stdcall (*GetSystemTimesType) (LPFILETIME, LPFILETIME, LPFILETIME);

static ULONGLONG
FileTimeToInt64 (const FILETIME * time)
{
    ULARGE_INTEGER _time;

    _time.LowPart = time->dwLowDateTime;
    _time.HighPart = time->dwHighDateTime;

    return _time.QuadPart;
}

/*
 * Code spidered from MySQL source code. 
 */
int 
RInt(double nr)
{
    double f = floor(nr);
    double c = ceil(nr);
    return (int)(((c-nr) >= (nr-f)) ? f :c);
}

/*
 * Code spidered from MySQL source code. 
 */
double 
ULongLong2Double(ULONGLONG value)
{
    LONGLONG nr=(LONGLONG) value;
    if (nr >= 0)
        return (double) nr;  
    return (18446744073709551616.0 + (double) nr);
}

JNIEXPORT jintArray JNICALL 
Java_org_livetribe_jmxcpu_Win32ProcessorUsage_getProcessorAverageUsage(JNIEnv *env, jobject obj, jint sleepingTime)
{
	DEBUGLOG(printf("JNI : -Entry-\n"));
    jintArray retValue = NULL;
    // load the DLL
    HINSTANCE hKernel = LoadLibrary("Kernel32.dll");
    if( hKernel )
    {
        DEBUGLOG(printf("LoadLibrary successful ... \n"));
        // Get a pointer to GetSystemTimes
        GetSystemTimesType hGetSystemTimes = (GetSystemTimesType) GetProcAddress(hKernel, "GetSystemTimes");
        if( hGetSystemTimes )
        {
            DEBUGLOG(printf("GetProcAddress successful ... \n"));
            FILETIME idleTime,kernelTime,userTime,idleTime2,kernelTime2,userTime2;
            // (1) get first sample
            BOOL res = hGetSystemTimes( &idleTime, &kernelTime, &userTime );
            if(res != 0)
            {
                // (2) sleep a few milliseconds
                Sleep(sleepingTime);
                // (3) get second sample
                BOOL res = hGetSystemTimes( &idleTime2, &kernelTime2, &userTime2 );
                if(res != 0)
                {
                    // (4) Calculate
                    ULONGLONG kernel = FileTimeToInt64(&kernelTime2) - FileTimeToInt64(&kernelTime);
                    ULONGLONG idle = FileTimeToInt64(&idleTime2) - FileTimeToInt64(&idleTime);
                    ULONGLONG user = FileTimeToInt64(&userTime2) - FileTimeToInt64(&userTime);
                    ULONGLONG denominator = (FileTimeToInt64(&kernelTime2) + FileTimeToInt64(&idleTime2) + FileTimeToInt64(&userTime2)) -
                                           (FileTimeToInt64(&kernelTime) + FileTimeToInt64(&idleTime) + FileTimeToInt64(&userTime));
                    DEBUGLOG(printf("Kernel = %I64u\tUser = %I64u\tIdle = %I64u\tdenominator = %I64u \n",kernel,user,idle,denominator));
                    DEBUGLOG(printf("Kernel = %f\tUser = %f \tIdle Usage = %f\n",(ULongLong2Double(kernel) / ULongLong2Double(denominator)) * 100,
                                                                                 (ULongLong2Double(user) / ULongLong2Double(denominator)) * 100,
                                                                                 (ULongLong2Double(idle) / ULongLong2Double(denominator)) * 100));
                    
                    retValue = env->NewIntArray(3);
                    
                    jint tempArray[3];
                    
                    // User Usage
                    tempArray[0] = RInt((ULongLong2Double(user) / ULongLong2Double(denominator)) * 100);
                    // System usage
                    tempArray[1] = RInt((ULongLong2Double(kernel) / ULongLong2Double(denominator)) * 100);
                    // Idle usage
                    tempArray[2] = RInt((ULongLong2Double(idle) / ULongLong2Double(denominator)) * 100);
                    
                    DEBUGLOG(printf("Kernel Usage = %d \t User Usage = %d \t Idle Usage = %d\n", tempArray[1] , tempArray[0] , tempArray[2]));
                    // Copy to array
                    env->SetIntArrayRegion(retValue, 0, 3, tempArray);                    
                }
                else
                {
                    // TODO : Report error, should we throw Exception?
                    DEBUGLOG(printf("Second GetSystemTimes failed.\n"));
                }
            }
            else
            {
                // TODO : Report error, should we throw Exception?
                DEBUGLOG(printf("First GetSystemTimes failed.\n"));
            }
        }
        else
        {
            // TODO : Report error, should we throw Exception?
            DEBUGLOG(printf("hGetSystemTimes handle is NULL\n"));    
        }
    }
    else
    {
        // TODO : Report error, should we throw Exception?
        DEBUGLOG(printf("hKernel handle is NULL\n"));    
    }
    
    DEBUGLOG(printf("JNI : -Exit-\n\n"));
	return retValue;
}
