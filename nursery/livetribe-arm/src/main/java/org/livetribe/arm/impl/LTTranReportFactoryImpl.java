package org.livetribe.arm.impl;

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.tranreport.ArmTranReport;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;

import org.livetribe.arm.LTAbstractFactoryBase;


/**
 * @version $Revision: $ $Date: $
 */
public class LTTranReportFactoryImpl extends LTAbstractFactoryBase implements ArmTranReportFactory, TranReportErrorCodes
{
    public ArmApplicationRemote newArmApplicationRemote(ArmApplicationDefinition definition, String group, String instance, String[] contextValues, ArmSystemAddress systemAddress)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, ArmID id)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, ArmID id)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, int length, ArmID id)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    public ArmTranReport newArmTranReport(ArmApplication app, ArmTransactionDefinition definition)
    {
        return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
    }
}
