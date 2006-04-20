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

import org.opengroup.arm40.tranreport.ArmTranReport;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.LTAbstractObject;


/**
 * @version $Revision: $ $Date: $
 */
class LTTranReport extends LTAbstractObject implements ArmTranReport
{
    private final ArmApplication app;
    private final ArmTransactionDefinition appTranDef;

    LTTranReport(ArmApplication app, ArmTransactionDefinition appTranDef)
    {
        this.app = app;
        this.appTranDef = appTranDef;
    }

    public ArmCorrelator generateCorrelator()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmApplication getApplication()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public String getContextURIValue()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public String getContextValue(int index)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmCorrelator getCorrelator()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmCorrelator getParentCorrelator()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public long getResponseTime()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int getStatus()
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmTransactionDefinition getDefinition()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmUser getUser()
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int report(int status, long respTime)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int report(int status, long respTime, long stopTime)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int report(int status, long respTime, String diagnosticDetail)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int report(int status, long respTime, long stopTime, String diagnosticDetail)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setContextURIValue(String value)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setContextValue(int index, String value)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setParentCorrelator(ArmCorrelator parent)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public int setUser(ArmUser user)
    {
        return 0;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }
}
