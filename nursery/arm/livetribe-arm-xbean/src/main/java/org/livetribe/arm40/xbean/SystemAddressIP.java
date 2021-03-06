/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm40.xbean;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.opengroup.arm40.tranreport.ArmSystemAddress;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class SystemAddressIP extends SystemAddress
{
    private final byte[] address;
    private final short format;

    public SystemAddressIP(String strAddress) throws UnknownHostException
    {
        this(InetAddress.getByName(strAddress));
    }

    public SystemAddressIP(InetAddress ipAddress)
    {
        address = ipAddress.getAddress();

        if (ipAddress instanceof Inet4Address) format = ArmSystemAddress.FORMAT_IPV4;
        else format = ArmSystemAddress.FORMAT_IPV6;
    }

    protected short getFormat()
    {
        return format;
    }

    protected byte[] getAddress()
    {
        return address;
    }

    protected int getStart()
    {
        return 0;
    }

    protected int getLength()
    {
        return address.length;
    }
}
