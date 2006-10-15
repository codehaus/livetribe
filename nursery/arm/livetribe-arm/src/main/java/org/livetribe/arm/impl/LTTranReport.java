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
import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;

import org.livetribe.arm.connection.Connection;
import org.livetribe.arm.util.StaticArmAPIMonitor;
import org.livetribe.util.uuid.UUIDGen;


/**
 * @version $Revision: $ $Date: $
 */
class LTTranReport extends AbstractIdentifiableObject implements ArmTranReport, ApplicationLifecycleListener
{
    private final Connection connection;
    private final UUIDGen guidGenerator;
    private final ArmApplication application;
    private final ArmTransactionDefinition tranDef;
    private ArmCorrelator parentCorrelator;
    private ArmCorrelator correlator;
    private boolean fresh;
    private String contextURI;
    private final String[] contextValues = new String[20];
    private ArmUser user;

    LTTranReport(String oid, Connection connection, UUIDGen guidGenerator, ArmApplication application, ArmTransactionDefinition tranDef)
    {
        super(oid);

        this.connection = connection;
        this.guidGenerator = guidGenerator;
        this.application = application;
        this.tranDef = tranDef;

        ((ApplicationLifecycleSupport) application).addApplicationLifecycleListener(this);
    }

    public void end()
    {
        setBad(true);
    }

    public ArmCorrelator generateCorrelator()
    {
        correlator = APIUtil.constructArmCorrelator(guidGenerator.uuidgen(), false);
        fresh = true;

        return correlator;
    }

    public ArmApplication getApplication()
    {
        return application;
    }

    public String getContextURIValue()
    {
        return contextURI;
    }

    public String getContextValue(int index)
    {
        return (index < 20 ? contextValues[index] : null);
    }

    public ArmCorrelator getCorrelator()
    {
        return correlator;
    }

    public ArmCorrelator getParentCorrelator()
    {
        return parentCorrelator;
    }

    /**
     * The ARM v4.0 spec does not explain what this method should return.  Return
     * a "safe" value of 0;
     *
     * @return 0
     */
    public long getResponseTime()
    {
        return 0;
    }

    /**
     * The ARM v4.0 spec does not explain what this method should return for
     * ArmTranReports.  Return a "safe" value of <code>STATUS_GOOD</code>.
     *
     * @return STATUS_GOOD
     */
    public int getStatus()
    {
        return ArmConstants.STATUS_GOOD;
    }

    public ArmTransactionDefinition getDefinition()
    {
        return tranDef;
    }

    public ArmUser getUser()
    {
        return user;
    }

    public int report(int status, long respTime)
    {
        return report(status, respTime, "");
    }

    public int report(int status, long respTime, long stopTime)
    {
        return report(status, respTime, stopTime, "");
    }

    public int report(int status, long respTime, String diagnosticDetail)
    {
        if (fresh) fresh = false;
        else correlator = APIUtil.constructArmCorrelator(guidGenerator.uuidgen(), false);

        connection.report(getObjectId(), (parentCorrelator != null ? parentCorrelator.getBytes() : null), correlator.getBytes(), status, respTime, diagnosticDetail);

        return GeneralErrorCodes.SUCCESS;
    }

    public int report(int status, long respTime, long stopTime, String diagnosticDetail)
    {
        if (fresh) fresh = false;
        else correlator = APIUtil.constructArmCorrelator(guidGenerator.uuidgen(), false);

        connection.report(getObjectId(), (parentCorrelator != null ? parentCorrelator.getBytes() : null), correlator.getBytes(), status, respTime, stopTime, diagnosticDetail);

        return GeneralErrorCodes.SUCCESS;
    }

    public int setContextURIValue(String value)
    {
        contextURI = value;

        return GeneralErrorCodes.SUCCESS;
    }

    public int setContextValue(int index, String value)
    {
        if (index > 20) return StaticArmAPIMonitor.error(TransactionErrorCodes.INDEX_OUT_OF_RANGE);

        if (value != null)
        {
            int length = value.length();

            if (length == 0) value = null;
            if (length > 127) StaticArmAPIMonitor.warning(TransactionErrorCodes.ID_PROP_TOO_LONG);
            if (tranDef.getIdentityProperties().getContextName(index) != null)
            {
                contextValues[index] = value;
            }
            else
            {
                StaticArmAPIMonitor.warning(TransactionErrorCodes.ID_PROP_IGNORED);
            }
        }

        return GeneralErrorCodes.SUCCESS;
    }

    public int setParentCorrelator(ArmCorrelator parent)
    {
        parentCorrelator = parent;

        return GeneralErrorCodes.SUCCESS;
    }

    public int setUser(ArmUser user)
    {
        this.user = user;

        return GeneralErrorCodes.SUCCESS;
    }
}
