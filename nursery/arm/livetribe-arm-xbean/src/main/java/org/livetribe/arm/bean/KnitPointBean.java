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
package org.livetribe.arm.bean;

import org.livetribe.arm.KnitPoint;
import org.livetribe.arm.connection.Connection;
import org.livetribe.util.uuid.UUIDGen;
import org.livetribe.util.uuid.UUIDGenDefault;


/**
 * @version $Revision$ $Date$
 */
public class KnitPointBean
{
    public static final String CONNECTION_KEY = "org.livetribe.arm.connection.Connection";
    public static final String UUIDGEN_KEY = "org.livetribe.util.uuid.UUIDGen";

    public Connection getConnection()
    {
        return (Connection) KnitPoint.get(CONNECTION_KEY);
    }

    public void setConnection(Connection connection)
    {
        KnitPoint.set(CONNECTION_KEY, connection);
    }

    public UUIDGen getUuidgen()
    {
        return (UUIDGen) KnitPoint.get(CONNECTION_KEY);
    }

    public void setUuidgen(UUIDGen uuidgen)
    {
        KnitPoint.set(UUIDGEN_KEY, uuidgen);
    }

    public void init()
    {
        if (getConnection() == null) throw new IllegalStateException("Connection has not been set");

        if (getUuidgen() == null) setUuidgen(new UUIDGenDefault());
    }
}
