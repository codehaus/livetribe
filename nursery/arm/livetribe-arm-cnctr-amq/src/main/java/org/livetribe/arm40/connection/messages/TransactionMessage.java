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
package org.livetribe.arm40.connection.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class TransactionMessage implements Message
{
    private final String transOID;
    private final String appOID;
    private final String transDefOID;

    public TransactionMessage(String transOID, String appOID, String transDefOID)
    {
        this.transOID = transOID;
        this.appOID = appOID;
        this.transDefOID = transDefOID;
    }

    public String getTransOID()
    {
        return transOID;
    }

    public String getAppOID()
    {
        return appOID;
    }

    public String getTransDefOID()
    {
        return transDefOID;
    }
}
