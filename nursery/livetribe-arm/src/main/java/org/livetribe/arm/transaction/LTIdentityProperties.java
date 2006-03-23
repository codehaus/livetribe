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
package org.livetribe.arm.transaction;

import org.opengroup.arm40.transaction.ArmIdentityProperties;

import org.livetribe.arm.LTAbstractFactoryBase;
import org.livetribe.arm.LTAbstractObjectBase;


/**
 * @version $Revision: $ $Date: $
 */
class LTIdentityProperties extends LTAbstractObjectBase implements ArmIdentityProperties
{
    String[] identityNames;
    String[] identityValues;
    String[] contextNames;

    LTIdentityProperties(LTIdentityProperties that)
    {
        this(that.getFactory(), that.identityNames, that.identityValues, that.contextNames);
    }

    LTIdentityProperties(LTAbstractFactoryBase factory, String[] identityNames, String[] identityValues, String[] contextNames)
    {
        super(factory);

        this.identityNames = new String[20];
        System.arraycopy(identityNames, 0, this.identityNames, 0, Math.min(20, identityNames.length));

        this.identityValues = new String[20];
        System.arraycopy(identityValues, 0, this.identityValues, 0, Math.min(20, identityValues.length));

        this.contextNames = new String[20];
        System.arraycopy(contextNames, 0, this.contextNames, 0, Math.min(20, contextNames.length));

        for (int i = 0; i < 20; i++)
        {
            if (this.identityNames[i] == null || this.identityNames[i].length() == 0)
            {
                this.identityNames[i] = null;
                this.identityValues[i] = null;
            }
            else if (this.identityValues[i] == null || this.identityValues[i].length() == 0)
            {
                this.identityNames[i] = null;
                this.identityValues[i] = null;
            }
        }
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
