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
package com.acme.arm.tranreport;

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.tranreport.ArmTranReport;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;


/**
 * @version $Revision: $ $Date: $
 */
public class AcmeTranReportFactory implements ArmTranReportFactory
{
    public ArmApplicationRemote newArmApplicationRemote(ArmApplicationDefinition armApplicationDefinition, String string, String string1, String[] strings, ArmSystemAddress armSystemAddress)
    {
        return null;
    }

    public ArmSystemAddress newArmSystemAddress(short i, byte[] bytes, ArmID armID)
    {
        return null;
    }

    public ArmSystemAddress newArmSystemAddress(short i, byte[] bytes, int i1, ArmID armID)
    {
        return null;
    }

    public ArmSystemAddress newArmSystemAddress(short i, byte[] bytes, int i1, int i2, ArmID armID)
    {
        return null;
    }

    public ArmTranReport newArmTranReport(ArmApplication armApplication, ArmTransactionDefinition armTransactionDefinition)
    {
        return null;
    }

    public boolean setErrorCallback(ArmErrorCallback armErrorCallback)
    {
        return false;
    }

    public int getErrorCode()
    {
        return 0;
    }

    public int setErrorCode(int i)
    {
        return 0;
    }

    public String getErrorMessage(int i)
    {
        return null;
    }
}
