/**
 *
 * Copyright 2005 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#ifndef NET_UTILS_H
#define NET_UTILS_H

#include <jni.h>

#define CHECK_NULL(x) if ((x) == NULL) return;
#define CHECK_NULL_RETURN(x, y) if ((x) == NULL) return y;
#define IS_NULL(obj) ((obj) == NULL)

/*
 * Package shorthand for use by native libraries
 */
#define JNU_JAVAPKG         "java/lang/"
#define JNU_JAVAIOPKG       "java/io/"
#define JNU_JAVANETPKG      "java/net/"

/*
 * Exception handling
 */
#define SocketException           "java/net/SocketException"
#define RuntimeException          "java/lang/RuntimeException"
#define BindException             "java/net/BindException"
#define NullPointerException      "java/lang/NullPointerException"
#define OutOfMemoryError          "java/lang/OutOfMemoryError"
#define IOException               "java/io/IOException"
#define InterruptedIOException    "java/io/InterruptedIOException"
#define IllegalArgumentException  "java/lang/IllegalArgumentException"


extern jfieldID ip_addressID;
extern jfieldID ip_typeID;
extern jfieldID ip_codeID;
extern jfieldID rp_addressID;
extern jfieldID rp_bufID;
extern jfieldID rp_offsetID;
extern jfieldID rp_lengthID;

namespace LiveTribe
{

	inline void ThrowByName(JNIEnv * env, const char * name, const char * msg) {
		jclass cls = env->FindClass(name);

		if (cls != 0) env->ThrowNew(cls, msg);

		env->DeleteLocalRef(cls);
	}

}

#endif