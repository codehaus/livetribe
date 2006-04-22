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

#ifndef LIVETRIBE_RAW_SOCKET_H
#define LIVETRIBE_RAW_SOCKET_H

#include /**/ "ace/pre.h"

#include "org_livetribe_export.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
# pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

#include "ace/SOCK.h"
#include "ace/Time_Value.h"
#include "ace/os_include/netinet/os_in.h"

namespace LiveTribe
{
/**
 * @class Raw_Socket
 */
class LIVETRIBE_Export Raw_Socket : public ACE_SOCK {
public:

    Raw_Socket (void);

    Raw_Socket (ACE_HANDLE);

    Raw_Socket (ACE_Addr const & local, int protocol, int reuse_addr = 0);

    Raw_Socket (int protocol, int reuse_addr = 0);

    ~Raw_Socket (void);

    ssize_t send (void const * buf,
                  size_t n,
                  ACE_Addr const & addr,
                  int flags = 0) const;

    ssize_t recv (void * buf,
                  size_t n,
                  ACE_Addr & addr,
                  int flags = 0) const;

    ssize_t recv (void * buf,
                  size_t n,
                  int flags,
                  ACE_Time_Value const * timeout) const;

	int open (int protocol = IPPROTO_ICMP, int reuse_addr = 0);

    int bind (ACE_Addr const & local);

    void dump (void) const;

    ACE_ALLOC_HOOK_DECLARE;

protected:

    unsigned short calculate_checksum (unsigned short* paddress, int len);

    int check_root_euid (void);

  };

}


#if defined (__ACE_INLINE__)
# include "Raw_Socket.inl"
#endif /* __ACE_INLINE__ */

#include /**/ "ace/post.h"

#endif /* LIVETRIBE_RAW_SOCKET_H */
