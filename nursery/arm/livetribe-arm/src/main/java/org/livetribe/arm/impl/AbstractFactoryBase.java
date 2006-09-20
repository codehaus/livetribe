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
package org.livetribe.arm.impl;

import org.opengroup.arm40.transaction.ArmErrorCallback;

import org.livetribe.arm.KnitPoint;
import org.livetribe.arm.connection.Connection;
import org.livetribe.arm.connection.DefaultConnection;
import org.livetribe.util.HexSupport;
import org.livetribe.util.uuid.UUIDGen;
import org.livetribe.util.uuid.UUIDGenDefault;


/**
 * @version $Revision: $ $Date: $
 */
public abstract class AbstractFactoryBase extends AbstractBase
{
    private static ArmErrorCallback callback;
    private final Connection connection;
    private final UUIDGen guidGenerator;

    AbstractFactoryBase()
    {
        Connection connection = (Connection) KnitPoint.get(Connection.class.getName());
        if (connection == null)
        {
            connection = new DefaultConnection();
        }
        this.connection = connection;

        UUIDGen guidGenerator = (UUIDGen) KnitPoint.get(UUIDGen.class.getName());
        if (guidGenerator == null)
        {
            guidGenerator = new UUIDGenDefault();
        }
        this.guidGenerator = guidGenerator;
    }

    public boolean setErrorCallback(ArmErrorCallback callback)
    {
        AbstractFactoryBase.callback = callback;

        return true;
    }

    public static ArmErrorCallback getCallback()
    {
        return callback;
    }

    protected Connection getConnection()
    {
        return connection;
    }

    protected UUIDGen getGuidGenerator()
    {
        return guidGenerator;
    }

    protected String allocateOID()
    {
        return HexSupport.toHexFromBytes(guidGenerator.uuidgen());
    }
}
