/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.util.uuid;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.SecureRandom;
import java.util.Random;

import org.livetribe.util.Bin;
import org.livetribe.util.HexSupport;


/**
 * Blatently stolen from Apache ActiveMQ
 *
 * @version $Revision: $ $Date: $
 */
public class UUIDGenDefault implements UUIDGen
{
    private static final byte[] UNIQUE_STUB;
    private static int instanceCount;
    private byte[] seed;
    private long sequence;
    private byte[] temp = new byte[8];

    static
    {
        byte[] stub = new byte[0];
        try
        {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null)
            {
                sm.checkPropertiesAccess();
            }
            try
            {
                byte[] addr = InetAddress.getLocalHost().getAddress();
                ServerSocket ss = new ServerSocket(0);
                byte[] port = Bin.convert((short) ss.getLocalPort());
                byte[] time = Bin.convert(System.currentTimeMillis());
                Thread.sleep(100);
                ss.close();

                stub = new byte[addr.length + 2 + 8];

                System.arraycopy(time, 0, stub, 0, 8);
                System.arraycopy(port, 0, stub, 8, 2);
                System.arraycopy(addr, 0, stub, 10, addr.length);
            }
            catch (Exception notLikely)
            {
            }
        }
        catch (SecurityException se)
        {
            byte[] time = Bin.convert(System.currentTimeMillis());

            Random secureRandom = null;
            try
            {
                secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            }
            catch (Exception e)
            {
                secureRandom = new Random();
            }

            secureRandom.setSeed(System.currentTimeMillis());

            byte[] fake = Bin.convert(secureRandom.nextLong());

            stub = new byte[16];

            System.arraycopy(time, 0, stub, 0, 8);
            System.arraycopy(fake, 0, stub, 8, 8);
        }

        UNIQUE_STUB = stub;
    }


    public UUIDGenDefault()
    {
        synchronized (UNIQUE_STUB)
        {
            seed = new byte[UNIQUE_STUB.length + 4];

            System.arraycopy(UNIQUE_STUB, 0, seed, 0, UNIQUE_STUB.length);
            System.arraycopy(Bin.convert(instanceCount++), 0, seed, UNIQUE_STUB.length, 4);
        }
    }

    public synchronized byte[] uuidgen()
    {
        byte[] result = new byte[seed.length + 8];

        System.arraycopy(seed, 0, result, 0, seed.length);

        Bin.insert(sequence++, temp);
        System.arraycopy(temp, 0, result, seed.length, 8);

        return result;
    }

    public String uuidgenString()
    {
        return HexSupport.toHexFromBytes(uuidgen());
    }

    public static void main(String args[])
    {
        final int ITERATIONS = 10000000;
        UUIDGenDefault generator = new UUIDGenDefault();

        generator.uuidgen();

        long start = System.currentTimeMillis();

        for (int i = 1; i <= ITERATIONS; ++i)
        {
            generator.uuidgen();
        }

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) / 1000.0 + "s, " + (end - start) * 1000000.0 / ITERATIONS + "ns/call " + generator.uuidgenString());
    }
}

