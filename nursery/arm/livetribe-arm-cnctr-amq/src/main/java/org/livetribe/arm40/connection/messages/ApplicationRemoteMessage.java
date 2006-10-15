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
public class ApplicationRemoteMessage implements Message
{
    private final String appOID;
    private final String appDefOID;
    private final String group;
    private final String instance;
    private final String[] ctxValues;
    private final byte[] systemAddres;

    public ApplicationRemoteMessage(String appOID, String appDefOID, String group, String instance, String[] ctxValues, byte[] systemAddres)
    {
        this.appOID = appOID;
        this.appDefOID = appDefOID;
        this.group = group;
        this.instance = instance;
        this.ctxValues = ctxValues;
        this.systemAddres = systemAddres;
    }

    public String getAppOID()
    {
        return appOID;
    }

    public String getAppDefOID()
    {
        return appDefOID;
    }

    public String getGroup()
    {
        return group;
    }

    public String getInstance()
    {
        return instance;
    }

    public String[] getCtxValues()
    {
        return ctxValues;
    }

    public byte[] getSystemAddres()
    {
        return systemAddres;
    }
}
