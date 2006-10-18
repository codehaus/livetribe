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

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.tranreport.ArmTranReport;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;

import org.livetribe.arm40.xbean.ErrorCheckingAdvice;


/**
 * @version $Revision: $ $Date: $
 */
public class LTTranReportFactory extends FacadeFactoryBase implements ArmTranReportFactory
{
    private final ArmTranReportFactory factoryProxy;

    public LTTranReportFactory()
    {
        factoryProxy = (ArmTranReportFactory) getApplicationContext().getBean("tranReportFactory");
        ErrorCheckingAdvice.registerProxy(factoryProxy, this);
    }

    protected String getFactoryType()
    {
        return "tranreport";
    }

    public ArmApplicationRemote newArmApplicationRemote(ArmApplicationDefinition definition, String group, String instance, String[] contextValues, ArmSystemAddress systemAddress)
    {
        return factoryProxy.newArmApplicationRemote(definition, group, instance, contextValues, systemAddress);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, ArmID id)
    {
        return factoryProxy.newArmSystemAddress(format, addressBytes, id);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, ArmID id)
    {
        return factoryProxy.newArmSystemAddress(format, addressBytes, offset, id);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, int length, ArmID id)
    {
        return factoryProxy.newArmSystemAddress(format, addressBytes, offset, id);
    }

    public ArmTranReport newArmTranReport(ArmApplication app, ArmTransactionDefinition definition)
    {
        return factoryProxy.newArmTranReport(app, definition);
    }
}
