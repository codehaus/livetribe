package org.livetribe.arm.xbean;

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.tranreport.ArmTranReport;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;

import org.livetribe.arm.ARMException;
import org.livetribe.arm.util.ARMUtil;


/**
 * @version $Revision: $ $Date$
 * @org.apache.xbean.XBean
 */
public class TranReportFactory implements ArmTranReportFactory
{
    private final ArmTranReportFactory base;

    public TranReportFactory() throws ARMException
    {
        base = ARMUtil.createArmTranReportFactory();
    }

    public ArmApplicationRemote newArmApplicationRemote(ArmApplicationDefinition definition, String group, String instance, String[] contextValues, ArmSystemAddress systemAddress)
    {
        return base.newArmApplicationRemote(definition, group, instance, contextValues, systemAddress);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, ArmID id)
    {
        return base.newArmSystemAddress(format, addressBytes, id);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, ArmID id)
    {
        return base.newArmSystemAddress(format, addressBytes, offset, id);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, int length, ArmID id)
    {
        return base.newArmSystemAddress(format, addressBytes, offset, id);
    }

    public ArmTranReport newArmTranReport(ArmApplication app, ArmTransactionDefinition definition)
    {
        return base.newArmTranReport(app, definition);
    }

    public boolean setErrorCallback(ArmErrorCallback armErrorCallback)
    {
        return base.setErrorCallback(armErrorCallback);
    }

    public void setErrorListener(ArmErrorCallback armErrorCallback)
    {
        base.setErrorCallback(armErrorCallback);
    }

    public int getErrorCode()
    {
        return base.getErrorCode();
    }

    public int setErrorCode(int i)
    {
        return base.setErrorCode(i);
    }

    public String getErrorMessage(int i)
    {
        return base.getErrorMessage(i);
    }
}
