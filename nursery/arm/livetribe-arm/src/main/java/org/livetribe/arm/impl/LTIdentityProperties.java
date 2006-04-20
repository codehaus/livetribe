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

import org.opengroup.arm40.transaction.ArmIdentityProperties;

import org.livetribe.arm.LTAbstractObject;


/**
 * @version $Revision: $ $Date: $
 */
class LTIdentityProperties extends LTAbstractObject implements ArmIdentityProperties
{
    private final String[] identityNames;
    private final String[] identityValues;
    private final String[] contextNames;

    LTIdentityProperties(String[] identityNames, String[] identityValues, String[] contextNames)
    {
        this.identityNames = identityNames;
        this.identityValues = identityValues;
        this.contextNames = contextNames;
    }

    public String getIdentityName(int i)
    {
        return (i < 20 ? identityNames[i] : null);
    }

    public String getIdentityValue(int i)
    {
        return (i < 20 ? identityValues[i] : null);
    }

    public String getContextName(int i)
    {
        return (i < 20 ? contextNames[i] : null);
    }
}
