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

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;

import org.livetribe.arm.LTAbstractObject;


/**
 * @version $Revision: $ $Date: $
 */
class LTApplication extends LTAbstractObject implements ArmApplication
{
    private final ArmApplicationDefinition definition;
    private final String group;
    private final String instance;
    private final String[] contextValues;

    LTApplication(ArmApplicationDefinition definition, String group, String instance, String[] contextValues)
    {
        this.definition = definition;
        this.group = group;
        this.instance = instance;
        this.contextValues = contextValues;
    }

    public int end()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
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
}
