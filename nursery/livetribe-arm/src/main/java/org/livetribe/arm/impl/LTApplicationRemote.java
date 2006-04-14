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
package org.livetribe.arm.impl;

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;


/**
 * @version $Revision: $ $Date: $
 */
public class LTApplicationRemote extends LTApplication implements ArmApplicationRemote
{
    private final ArmSystemAddress systemAddress;

    public LTApplicationRemote(ArmApplicationDefinition definition, String group, String instance, String[] contextValues, ArmSystemAddress systemAddress)
    {
        super(definition, group, instance, contextValues);
        this.systemAddress = systemAddress;
    }

    public ArmSystemAddress getSystemAddress()
    {
        return systemAddress;
    }
}
