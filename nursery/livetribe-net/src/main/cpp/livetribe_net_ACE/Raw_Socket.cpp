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

#include "Raw_Socket.h"


#include "ace/ACE.h"
#include "ace/Log_Msg.h"
#include "ace/OS_NS_netdb.h"
#include "ace/OS_NS_sys_socket.h"

#if !defined (__ACE_INLINE__)
# include "Raw_Socket.inl"
#endif  /* !__ACE_INLINE__ */


ACE_RCSID (ace,
           Raw_Socket,
           "Raw_Socket.cpp,v 1.3 2004/09/26 14:12:19 jwillemsen Exp")


namespace LiveTribe_ACE
{
	ACE_ALLOC_HOOK_DEFINE (Raw_Socket)
}


void
LiveTribe_ACE::Raw_Socket::dump(void) const {

	ACE_TRACE ("LiveTribe::Raw_Socket::dump");
}

LiveTribe_ACE::Raw_Socket::Raw_Socket(void) {

	ACE_TRACE ("LiveTribe::Raw_Socket::Raw_Socket");
}

LiveTribe_ACE::Raw_Socket::Raw_Socket(ACE_HANDLE handle) {

	ACE_TRACE ("LiveTribe::Raw_Socket::Raw_Socket");

	this->set_handle(handle);
}

LiveTribe_ACE::Raw_Socket::Raw_Socket(ACE_Addr const & local, int protocol, int reuse_addr) {

	ACE_TRACE ("LiveTribe::Raw_Socket::Raw_Socket");

	Raw_Socket(protocol, reuse_addr);

	if (this->bind (local) == -1) {
		ACE_DEBUG ((LM_DEBUG, "ACELiveTribe::Raw_Socket::Raw_Socket - bind() error.\n"));
    }
}

LiveTribe_ACE::Raw_Socket::Raw_Socket(int protocol, int reuse_addr) {

	ACE_TRACE ("LiveTribe::Raw_Socket::Raw_Socket");

	if (this->open (protocol, reuse_addr) == -1) {
		ACE_DEBUG ((LM_DEBUG, "ACELiveTribe::Raw_Socket::Raw_Socket - open() error.\n"));
    }

	// trying to increase the size of socket receive buffer - some
	// protection from multiple responces e.g., when falling to the
	// multi-cast address
	int size = 64 * 1024;
	ACE_SOCK::set_option(SOL_SOCKET, SO_RCVBUF, (void *) &size, sizeof (size));
}

ssize_t
LiveTribe_ACE::Raw_Socket::send(void const * buf,
						    size_t n,
						    ACE_Addr const & addr,
						    int flags) const
{
	ACE_TRACE ("LiveTribe::Raw_Socket::send");

	return ACE_OS::sendto(this->get_handle (),
                         (char const *) buf,
                         n,
                         flags,
                         (sockaddr const *) addr.get_addr (),
                         addr.get_size ());
}

ssize_t
LiveTribe_ACE::Raw_Socket::recv (void * buf,
                        size_t n,
                        ACE_Addr & addr,
                        int flags) const
{
  ACE_TRACE ("LiveTribe::Raw_Socket::recv");

  int addr_len = addr.get_size ();
  ssize_t status = ACE_OS::recvfrom (this->get_handle (),
                                     (char *) buf,
                                     n,
                                     flags,
                                     (sockaddr *) addr.get_addr (),
                                     (int*) &addr_len);
  addr.set_size (addr_len);

  return status;
}

ssize_t
LiveTribe_ACE::Raw_Socket::recv (void * buf,
                        size_t n,
                        int flags,
                        ACE_Time_Value const * timeout) const
{
  ACE_TRACE ("LiveTribe::Raw_Socket::recv");

  return ACE::recv (this->get_handle (),
                    buf,
                    n,
                    flags,
                    timeout);
}


int
LiveTribe_ACE::Raw_Socket::open (int protocol, int reuse_addr)
{
	ACE_TRACE ("ACE::Raw_Socket::open");

	if (! this->check_root_euid ()) {
		ACE_ERROR_RETURN ((LM_ERROR, "%p\n", "(%P|%t) ACE::Raw_Socket::open - " "root-privileges required."), -1);
    }


	if (ACE_SOCK::open (SOCK_RAW, AF_INET, protocol, reuse_addr) == -1) {
		return -1;
    }

	return 0;
}

int
LiveTribe_ACE::Raw_Socket::bind (ACE_Addr const & local)
{
  ACE_TRACE ("ACE::Raw_Socket::shared_open");

  int error = 0;
  if (local == ACE_Addr::sap_any)
    {
      if (ACE::bind_port (this->get_handle ()) == -1)
        {
          error = 1;
        }
    }
  else if (ACE_OS::bind (this->get_handle (),
                         reinterpret_cast<sockaddr *> (local.get_addr ()),
                         local.get_size ()) == -1)
    {
      error = 1;
    }

  if (error != 0)
    {
      this->close ();
    }

  return error ? -1 : 0;
}

unsigned short
LiveTribe_ACE::Raw_Socket::calculate_checksum (unsigned short * paddress,
                                      int len)
{
  int nleft = len;
  int sum = 0;
  unsigned short * w = paddress;
  unsigned short answer = 0;
  while (nleft > 1)
    {
      sum += *w++;
      nleft -= 2;
    }

  if (nleft == 1)
    {
      *((unsigned char *) &answer) = *((unsigned char *) w);
      sum += answer;
    }

  // add back carry outs from top 16 bits to low 16 bits
  sum = (sum >> 16) + (sum & 0xffff); // add hi 16 to low 16
  sum += (sum >> 16);                 // add carry
  answer = ~sum;                      // truncate to 16 bits

  return (answer);
}

int
LiveTribe_ACE::Raw_Socket::check_root_euid (void)
{
  int euid = 0;

#if ! defined (ACE_WIN32)
  euid = static_cast<int> (::geteuid ());
#endif  /* #if ! defined (ACE_WIN32) */

  return (euid == 0);
}
