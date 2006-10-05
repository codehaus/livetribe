
#include <jni.h>
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
    ULARGE_INTEGER ltime;

    ltime.LowPart = time->dwLowDateTime;
    ltime.HighPart = time->dwHighDateTime;

    return ltime.QuadPart;
}

/*
 * Class:     org_livetribe_jmxcpu_NativeProcessorUsage
 * Method:    supportsMultiprocessorUsageQuery
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL 
Java_org_livetribe_jmxcpu_NativeProcessorUsage_supportsMultiprocessorUsageQuery(JNIEnv *env, jobject obj)
{
    return JNI_FALSE;
}

/*
 * Class:     org_livetribe_jmxcpu_NativeProcessorUsage
 * Method:    getProcessorSnapshot
 * Signature: ()[J
 */
JNIEXPORT jlongArray JNICALL 
Java_org_livetribe_jmxcpu_NativeProcessorUsage_getProcessorSnapshot(JNIEnv *env, jobject obj)
{
	DEBUGLOG(printf("JNI : -Entry-\n"));
    jlongArray retValue = NULL;
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
            FILETIME idleTime,kernelTime,userTime;
            // Call the API
            BOOL res = hGetSystemTimes( &idleTime, &kernelTime, &userTime );
            if(res != 0)
            {
                DEBUGLOG(printf("Kernel = %I64u\tUser = %I64u\tIdle = %I64u\n",
                                FileTimeToInt64(&kernelTime),FileTimeToInt64(&userTime),FileTimeToInt64(&idleTime)));
                                
                retValue = env->NewLongArray(3);
                
                jlong tempArray[3];
                // User snapshot
                tempArray[0] = FileTimeToInt64(&userTime);
                // System snapshot
                tempArray[1] = FileTimeToInt64(&kernelTime);
                // Idle snapshot
                tempArray[2] = FileTimeToInt64(&idleTime);
                    
                // Copy to array
                env->SetLongArrayRegion(retValue, 0, 3, tempArray);
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

/*
 * Class:     org_livetribe_jmxcpu_NativeProcessorUsage
 * Method:    getProcessorsSnapshot
 * Signature: ()[[J
 */
JNIEXPORT jobjectArray JNICALL 
Java_org_livetribe_jmxcpu_NativeProcessorUsage_getProcessorsSnapshot(JNIEnv *env, jobject obj)
{
    return NULL;
}
