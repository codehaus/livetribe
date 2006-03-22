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

import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;

import org.livetribe.arm.LTAbstractFactoryBase;

/**
 * @version $Revision: $ $Date: $
 */
class LTIdentityPropertiesTransaction extends LTIdentityProperties implements ArmIdentityPropertiesTransaction
{
    private final String uri;

    LTIdentityPropertiesTransaction(LTIdentityPropertiesTransaction that)
    {
        super(that);

        this.uri = that.uri;
    }

    LTIdentityPropertiesTransaction(LTAbstractFactoryBase factory, String[] identityNames, String[] identityValues, String[] contextNames, String uri)
    {
        super(factory, identityNames, identityValues, contextNames);

        this.uri = uri;
    }

    public String getURIValue()
    {
        return uri;
    }
}
