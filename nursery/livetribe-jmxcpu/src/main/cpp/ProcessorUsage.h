/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_livetribe_jmxcpu_NativeProcessorUsage */

#ifndef _Included_org_livetribe_jmxcpu_NativeProcessorUsage
#define _Included_org_livetribe_jmxcpu_NativeProcessorUsage
#ifdef __cplusplus
extern "C" {
#endif
#undef org_livetribe_jmxcpu_NativeProcessorUsage_USER_INDEX
#define org_livetribe_jmxcpu_NativeProcessorUsage_USER_INDEX 0L
#undef org_livetribe_jmxcpu_NativeProcessorUsage_SYSTEM_INDEX
#define org_livetribe_jmxcpu_NativeProcessorUsage_SYSTEM_INDEX 1L
#undef org_livetribe_jmxcpu_NativeProcessorUsage_IDLE_INDEX
#define org_livetribe_jmxcpu_NativeProcessorUsage_IDLE_INDEX 2L
/*
 * Class:     org_livetribe_jmxcpu_NativeProcessorUsage
 * Method:    supportsMultiprocessorUsageQuery
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_livetribe_jmxcpu_NativeProcessorUsage_supportsMultiprocessorUsageQuery
  (JNIEnv *, jobject);

/*
 * Class:     org_livetribe_jmxcpu_NativeProcessorUsage
 * Method:    getProcessorSnapshot
 * Signature: ()[J
 */
JNIEXPORT jlongArray JNICALL Java_org_livetribe_jmxcpu_NativeProcessorUsage_getProcessorSnapshot
  (JNIEnv *, jobject);

/*
 * Class:     org_livetribe_jmxcpu_NativeProcessorUsage
 * Method:    getProcessorsSnapshot
 * Signature: ()[[J
 */
JNIEXPORT jobjectArray JNICALL Java_org_livetribe_jmxcpu_NativeProcessorUsage_getProcessorsSnapshot
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
