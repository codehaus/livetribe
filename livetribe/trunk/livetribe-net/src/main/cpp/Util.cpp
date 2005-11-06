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

#include <ace/Basic_Types.h>
#include "org_livetribe_net_util_Util.h"
#include "net_util.h"

/************************************************************************
 * Util
 */

/*
 * Class:     org_livetribe_net_util_Util
 * Method:    uncheckedChecksum
 * Signature: ([BII)S
 */
JNIEXPORT jshort JNICALL Java_org_livetribe_net_util_Util_uncheckedChecksum
  (JNIEnv *env, jclass cls, jbyteArray data, jint start, jint length) {

	jbyte *buffer;
	if ((buffer = env->GetByteArrayElements(data, NULL)) == NULL) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Unable to obtain buffer from packet");
		return 0;
	}

    int nleft = length;
    int sum = 0;
    unsigned short * w = (unsigned short *) buffer + start;
    unsigned short answer = 0;

    while (nleft > 1) {
        sum += *w++;
        nleft -= 2;
    }

    if (nleft == 1) {
        *((unsigned char *) &answer) = *((unsigned char *) w);
        sum += ACE_NTOHS(answer);
    }

    // add back carry outs from top 16 bits to low 16 bits
    sum = (sum >> 16) + (sum & 0xffff); // add hi 16 to low 16
    sum += (sum >> 16);                 // add carry
    answer = ACE_HTONS(~sum);           // 1's complement and truncate

	env->ReleaseByteArrayElements(data, buffer, JNI_ABORT);

	return answer;
}

/*
 * Class:     org_livetribe_net_Util
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL
  Java_org_livetribe_net_util_Util_init (JNIEnv *env, jclass cls) {
}
