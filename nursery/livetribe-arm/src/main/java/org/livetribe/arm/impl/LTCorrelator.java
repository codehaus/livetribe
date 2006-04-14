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

import org.opengroup.arm40.transaction.ArmCorrelator;


/**
 * @version $Revision: $ $Date: $
 */
class LTCorrelator extends LTToken implements ArmCorrelator
{
    private final boolean agentTrace;
    private final boolean applicationTrace;

    LTCorrelator(byte[] id)
    {
        super(id);

        assert id.length >= 4;

        this.agentTrace = (id[3] & 0x40) != 0;
        this.applicationTrace = (id[3] & 0x20) != 0;
    }

    public boolean isAgentTrace()
    {
        return agentTrace;
    }

    public boolean isApplicationTrace()
    {
        return applicationTrace;
    }
}
