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

#include "org_livetribe_net_RawPacket.h"
#include "net_util.h"

/************************************************************************
 * RAWPacket
 */

jfieldID rp_addressID;
jfieldID rp_bufID;
jfieldID rp_offsetID;
jfieldID rp_lengthID;

/*
 * Class:     org_livetribe_net_RawPacket
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_RawPacket_init (JNIEnv *env, jclass cls) {

    ip_addressID = env->GetFieldID(cls, "address", "Ljava/net/InetAddress;");
    CHECK_NULL(ip_addressID);
    rp_bufID = env->GetFieldID(cls, "buf", "[B");
    CHECK_NULL(rp_bufID);
    rp_offsetID = env->GetFieldID(cls, "offset", "I");
    CHECK_NULL(rp_offsetID);
    rp_lengthID = env->GetFieldID(cls, "length", "I");
    CHECK_NULL(rp_lengthID);
}
