/**
 *
 * Copyright 2005 (C) The original author or authors.
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
package org.livetribe.totem.single;

import java.net.InetAddress;

import org.livetribe.totem.Identifier;


/**
 * @version $Revision: $ $Date: $
 */
public class DefaultIdentifier implements Identifier
{
    private final InetAddress address;
    private final int port;

    public DefaultIdentifier(InetAddress address, int port)
    {
        if (address == null) throw new IllegalArgumentException("address cannot be null");
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("port out of range:" + port);

        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DefaultIdentifier that = (DefaultIdentifier) o;

        if (port != that.port) return false;
        if (!address.equals(that.address)) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = address.hashCode();
        result = 29 * result + (int) port;
        return result;
    }

    public int compareTo(Object o)
    {
        if (!(o instanceof DefaultIdentifier)) return 1;

        final DefaultIdentifier that = (DefaultIdentifier) o;

        byte[] testThis = address.getAddress();
        byte[] testThat = that.getAddress().getAddress();

        for (int i = 0; i < testThis.length; i++)
        {
            int test = testThis[i] - testThat[i];
            if (test != 0) return test;
        }

        return port - that.getPort();
    }

    public String toString()
    {
        return address + ":" + port;
    }
}
