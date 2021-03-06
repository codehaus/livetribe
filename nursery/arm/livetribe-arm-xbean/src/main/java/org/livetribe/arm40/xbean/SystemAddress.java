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
package org.livetribe.arm40.xbean;

import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionFactory;


/**
 * @version $Revision: $ $Date$
 */
public abstract class SystemAddress extends Base
{
    private ArmTranReportFactory tranReportFactory;
    private ArmTransactionFactory transactionFactory;
    private byte[] armId;

    protected ArmInterface generate()
    {
        return tranReportFactory.newArmSystemAddress(getFormat(),
                                                     getAddress(), getStart(), getLength(),
                                                     transactionFactory.newArmID(armId));
    }

    protected abstract short getFormat();

    protected abstract byte[] getAddress();

    protected abstract int getStart();

    protected abstract int getLength();

    public ArmTranReportFactory getTranReportFactory()
    {
        return tranReportFactory;
    }

    public void setTranReportFactory(ArmTranReportFactory tranReportFactory)
    {
        this.tranReportFactory = tranReportFactory;
    }

    public ArmTransactionFactory getTransactionFactory()
    {
        return transactionFactory;
    }

    public void setTransactionFactory(ArmTransactionFactory transactionFactory)
    {
        this.transactionFactory = transactionFactory;
    }

    public byte[] getArmId()
    {
        return armId;
    }

    public void setArmId(byte[] armId)
    {
        this.armId = armId;
    }
}
