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
package org.livetribe.arm40.impl;

import java.util.Iterator;
import java.util.Vector;

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;


/**
 * @version $Revision: $ $Date: $
 */
class LTApplicationRemote extends AbstractIdentifiableObject implements ArmApplicationRemote
{
    private final Vector listeners = new Vector();
    private final ArmApplicationDefinition definition;
    private final String group;
    private final String instance;
    private final String[] contextValues;
    private final ArmSystemAddress systemAddress;

    LTApplicationRemote(String oid, ArmApplicationDefinition definition, String group, String instance, String[] contextValues, ArmSystemAddress systemAddress)
    {
        super(oid);

        this.definition = definition;
        this.group = group;
        this.instance = instance;
        this.contextValues = contextValues;
        this.systemAddress = systemAddress;
    }

    public int end()
    {
        setBad(true);

        Iterator iter = listeners.iterator();
        while (iter.hasNext())
        {
            try
            {
                ((AbstractTransactionBase) iter.next()).end();
            }
            catch (Exception ignore)
            {
            }
        }
        return GeneralErrorCodes.SUCCESS;
    }

    public String getContextValue(int index)
    {
        return contextValues[index];
    }

    public ArmApplicationDefinition getDefinition()
    {
        return definition;
    }

    public String getGroup()
    {
        return group;
    }

    public String getInstance()
    {
        return instance;
    }

    public ArmSystemAddress getSystemAddress()
    {
        return systemAddress;
    }

    public void addApplicationLifecycleListener(AbstractTransactionBase listener)
    {
        listeners.add(listener);
    }
}
