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

#include "org_livetribe_net_PlainRawSocketImpl.h"
#include "net_util.h"
#include <ace/ACE.h>
#include <ace/INET_Addr.h>
#include "Raw_Socket.h"


/************************************************************************
 * RAWPacket
 */

static jfieldID  ia6_ipaddressID;
static jclass    class_ia;
static jfieldID  ia_addressID;
static jfieldID  ia_familyID;
static jmethodID ia_getAddressID;
static jfieldID  fd_fdID;
static jfieldID  prsi_protocolID;
static jfieldID  prsi_fileDescriptorID;
static jfieldID  prsi_timeoutID;
static jclass    class_packet;

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    bind
 * Signature: (Ljava/net/InetAddress;)V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_bind (JNIEnv * env, jobject obj, jbyteArray laddr) {

	int fd;

	jobject fdObj = env->GetObjectField(obj, prsi_fileDescriptorID);
	if (IS_NULL(fdObj)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Socket closed");
		return;
	} else {
		fd = env->GetIntField(fdObj, fd_fdID);
	}

	jbyte *address;
	if ((address = env->GetByteArrayElements(laddr, NULL)) == NULL) {
		LiveTribe::ThrowByName(env, "java/lang/RuntimeException", "Socket closed");
		return;
	}

	ACE_INET_Addr ace_address;
	ace_address.set_addr(address, env->GetArrayLength(laddr));
	ace_address.set_type(AF_INET);

	LiveTribe::Raw_Socket raw_socket((ACE_HANDLE)fd);

	if (raw_socket.bind(ace_address) == -1) {
		if (errno == EADDRINUSE || errno == EADDRNOTAVAIL || errno == EPERM || errno == EACCES) {
			LiveTribe::ThrowByName(env, "java/net/BindException", strerror(errno));
		} else {
			LiveTribe::ThrowByName(env, "java/net/SocketException", strerror(errno));
		}
	}
	env->ReleaseByteArrayElements(laddr, address, JNI_ABORT);
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    send
 * Signature: (Lorg/livetribe/net/RawPacket;)V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_send (JNIEnv * env, jobject socket, jobject packet) {
	int fd;

	jobject fdObj = env->GetObjectField(socket, prsi_fileDescriptorID);
	if (IS_NULL(fdObj)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Socket closed");
		return;
	} else {
		fd = env->GetIntField(fdObj, fd_fdID);
	}

	jobject iaObj = env->GetObjectField(packet, ip_addressID);
	if (IS_NULL(iaObj)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Unable to obtain address from packet");
		return;
	}

	jbyteArray iarray = (jbyteArray) env->CallObjectMethod(iaObj,  ia_getAddressID);
	if (IS_NULL(iarray)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "getAddress");
		return;
	}

	jbyte *address;
	if ((address = env->GetByteArrayElements(iarray, NULL)) == NULL) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Unable to obtain address from packet");
		return;
	}

	ACE_INET_Addr ace_address;
	ace_address.set_address((const char*)address, env->GetArrayLength(iarray));
	ace_address.set_type(AF_INET);

	LiveTribe::Raw_Socket raw_socket((ACE_HANDLE)fd);

	jbyteArray barray = (jbyteArray) env->GetObjectField(packet,  rp_bufID);
	if (IS_NULL(barray)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Unable to obtain buffer from packet");
		return;
	}

	jbyte *buffer;
	if ((buffer = env->GetByteArrayElements(barray, NULL)) == NULL) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Unable to obtain buffer from packet");
		return;
	}


	if (raw_socket.send(buffer, env->GetArrayLength(barray), ace_address) == -1) {
		if (errno == EADDRINUSE || errno == EADDRNOTAVAIL || errno == EPERM || errno == EACCES) {
			LiveTribe::ThrowByName(env, "java/net/BindException", strerror(errno));
		} else {
			LiveTribe::ThrowByName(env, "java/net/SocketException", strerror(errno));
		}
	}

	env->ReleaseByteArrayElements(iarray, address, JNI_ABORT);
	env->ReleaseByteArrayElements(barray, buffer, JNI_ABORT);
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    peek
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_peek (JNIEnv * env, jobject obj) {
	return NULL;
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    receive
 * Signature: (Lorg/livetribe/net/RawPacket;)V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_receive (JNIEnv * env, jobject obj, jobject packet) {
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    rawSocketCreate
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_rawSocketCreate (JNIEnv * env, jobject obj, jint protocol) {
	int fd = 0;

	LiveTribe::Raw_Socket raw_socket((int) protocol);
	fd = (int)raw_socket.get_handle();

	jobject fdObj = env->GetObjectField(obj, prsi_fileDescriptorID);
	if (IS_NULL(fdObj)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Socket closed");
		return;
	} else {
		env->SetIntField(fdObj, fd_fdID, fd);
	}
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    rawSocketClose
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_rawSocketClose (JNIEnv * env, jobject obj) {

	jobject fdObj = env->GetObjectField(obj, prsi_fileDescriptorID);
    int fd;

    if (IS_NULL(fdObj)) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Socket closed");
		return;
    } else {
        fd = env->GetIntField(fdObj, fd_fdID);
    }
	LiveTribe::Raw_Socket raw_socket((ACE_HANDLE)fd);

	if (raw_socket.close() < 0) {
		LiveTribe::ThrowByName(env, "java/net/SocketException", "Error closing socket");
	}

	env->SetIntField(fdObj, fd_fdID, -1);
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    socketSetOption
 * Signature: (ILjava/lang/Object;)V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_socketSetOption (JNIEnv * env, jobject obj, jint opt, jobject value) {
}

/*
 * Class:     org_livetribe_net_PlainRawSocketImpl
 * Method:    socketGetOption
 * Signature: (I)[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_socketGetOption (JNIEnv * env, jobject obj, jint opt) {
	return NULL;
}

/*
 * Class:     org_livetribe_net_RawPacket
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_livetribe_net_PlainRawSocketImpl_init (JNIEnv *env, jclass cls) {

    prsi_protocolID = env->GetFieldID(cls, "protocol", "I");
    CHECK_NULL(prsi_protocolID);
    prsi_fileDescriptorID = env->GetFieldID(cls, "fileDescriptor", "Ljava/io/FileDescriptor;");
    CHECK_NULL(prsi_fileDescriptorID);
    prsi_timeoutID = env->GetFieldID(cls, "timeout", "I");
    CHECK_NULL(prsi_timeoutID);

    fd_fdID = env->GetFieldID(env->FindClass("java/io/FileDescriptor"), "fd", "I");
    CHECK_NULL(fd_fdID);

    ia6_ipaddressID = env->GetFieldID(env->FindClass("java/net/Inet6Address"), "ipaddress", "[B");
    CHECK_NULL(ia6_ipaddressID);

	class_ia = env->FindClass("java/net/InetAddress");
    CHECK_NULL(class_ia);
    ia_addressID = env->GetFieldID(class_ia, "address", "I");
    CHECK_NULL(ia_addressID);
    ia_familyID = env->GetFieldID(class_ia, "family", "I");
    CHECK_NULL(ia_familyID);
	ia_getAddressID = env->GetMethodID(class_ia, "getAddress", "()[B");
    CHECK_NULL(ia_getAddressID);

	class_packet = env->FindClass("org/livetribe/net/RawPacket");
    CHECK_NULL(class_packet);
}
