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
package org.livetribe.util;


/**
 * @version $Revision: $ $Date: $
 */
public final class Bin
{
    private Bin()
    {
    }

    public static byte[] convert(long source)
    {
        return insert(source, new byte[8]);
    }

    public static byte[] insert(long source, byte[] destination)
    {
        return insert(source, destination, 0);
    }

    public static byte[] insert(long source, byte[] destination, int offset)
    {
        if (destination.length < offset + 8) throw new IllegalArgumentException("destination cannot hold a long");

        destination[offset + 7] = (byte) source;
        source >>>= 8;
        destination[offset + 6] = (byte) source;
        source >>>= 8;
        destination[offset + 5] = (byte) source;
        source >>>= 8;
        destination[offset + 4] = (byte) source;
        source >>>= 8;
        destination[offset + 3] = (byte) source;
        source >>>= 8;
        destination[offset + 2] = (byte) source;
        source >>>= 8;
        destination[offset + 1] = (byte) source;
        source >>>= 8;
        destination[offset] = (byte) source;

        return destination;
    }

    public static long toLong(byte[] source)
    {
        return ((((long) source[7]) & 0xFF)
                + ((((long) source[6]) & 0xFF) << 8)
                + ((((long) source[5]) & 0xFF) << 16)
                + ((((long) source[4]) & 0xFF) << 24)
                + ((((long) source[3]) & 0xFF) << 32)
                + ((((long) source[2]) & 0xFF) << 40)
                + ((((long) source[1]) & 0xFF) << 48)
                + ((((long) source[0]) & 0xFF) << 56));
    }

    public static byte[] convert(int source)
    {
        return insert(source, new byte[4]);
    }

    public static byte[] insert(int source, byte[] destination)
    {
        return insert(source, destination, 0);
    }

    public static byte[] insert(int source, byte[] destination, int offset)
    {
        if (destination.length < offset + 4) throw new IllegalArgumentException("destination cannot hold a long");

        destination[offset + 3] = (byte) source;
        source >>>= 8;
        destination[offset + 2] = (byte) source;
        source >>>= 8;
        destination[offset + 1] = (byte) source;
        source >>>= 8;
        destination[offset] = (byte) source;

        return destination;
    }

    public static long toInteger(byte[] source)
    {
        return ((((long) source[3]) & 0xFF)
                + ((((long) source[2]) & 0xFF) << 8)
                + ((((long) source[1]) & 0xFF) << 16)
                + ((((long) source[0]) & 0xFF) << 24));
    }

    public static byte[] convert(short source)
    {
        return insert(source, new byte[2]);
    }

    public static byte[] insert(short source, byte[] destination)
    {
        return insert(source, destination, 0);
    }

    public static byte[] insert(short source, byte[] destination, int offset)
    {
        if (destination.length < offset + 2) throw new IllegalArgumentException("destination cannot hold a short");

        destination[offset + 1] = (byte) source;
        source >>>= 8;
        destination[offset] = (byte) source;

        return destination;
    }

    public static long toShort(byte[] source)
    {
        return ((((long) source[1]) & 0xFF)
                + ((((long) source[0]) & 0xFF) << 8));
    }
}
