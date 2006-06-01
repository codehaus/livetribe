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
package org.livetribe.arm.xbean;

import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionFactory;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class IdentityProperties extends Base
{
    private ArmTransactionFactory factory;
    private String[] identityNames;
    private String[] identityValues;
    private String[] contextNames;

    protected ArmInterface generate()
    {
        return factory.newArmIdentityProperties(identityNames, identityValues, contextNames);
    }

    public ArmTransactionFactory getFactory()
    {
        return factory;
    }

    public void setFactory(ArmTransactionFactory factory)
    {
        this.factory = factory;
    }

    public String[] getIdentityNames()
    {
        return identityNames;
    }

    public void setIdentityNames(String[] identityNames)
    {
        this.identityNames = identityNames;
    }

    public String[] getIdentityValues()
    {
        return identityValues;
    }

    public void setIdentityValues(String[] identityValues)
    {
        this.identityValues = identityValues;
    }

    public String[] getContextNames()
    {
        return contextNames;
    }

    public void setContextNames(String[] contextNames)
    {
        this.contextNames = contextNames;
    }
}
