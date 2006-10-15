package org.livetribe.arm.impl;

import org.opengroup.arm40.tranreport.ArmApplicationRemote;
import org.opengroup.arm40.tranreport.ArmSystemAddress;
import org.opengroup.arm40.tranreport.ArmTranReport;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;

import org.livetribe.arm.util.StaticArmAPIMonitor;


/**
 * @version $Revision: $ $Date: $
 */
public class LTTranReportFactoryImpl extends AbstractFactoryBase implements ArmTranReportFactory, TranReportErrorCodes
{
    public ArmApplicationRemote newArmApplicationRemote(ArmApplicationDefinition appDef, String group, String instance, String[] contextValues, ArmSystemAddress systemAddress)
    {
        appDef = APIUtil.checkRequired(appDef);
        group = APIUtil.checkOptional255(group);
        instance = APIUtil.checkOptional255(instance);
        contextValues = APIUtil.checkOptional(appDef, contextValues);
        systemAddress = APIUtil.checkOptional(systemAddress);

        LTApplicationRemote appRemote = new LTApplicationRemote(allocateOID(), appDef, group, instance, contextValues, systemAddress);

        getConnection().declareApplicationRemote(appRemote.getObjectId(),
                                                 ((Identifiable) appDef).getObjectId(),
                                                 group, instance, contextValues,
                                                 APIUtil.extractArmSystemAddress(systemAddress));

        return appRemote;
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, ArmID id)
    {
        return newArmSystemAddress(format, addressBytes, 0, id);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, ArmID id)
    {
        return newArmSystemAddress(format, addressBytes, 0, addressBytes.length, id);
    }

    public ArmSystemAddress newArmSystemAddress(short format, byte[] addressBytes, int offset, int length, ArmID id)
    {
        if (format == 0) StaticArmAPIMonitor.error(FORMAT_ZERO);
        id = APIUtil.checkOptional(id);

        if (addressBytes == null)
        {
            addressBytes = new byte[0];
            offset = 0;
            length = 0;

            StaticArmAPIMonitor.error(ADDRESS_NULL);
        }

        if (addressBytes.length < offset + length) StaticArmAPIMonitor.error(ADDR_TOO_SHORT);

        length = Math.min(addressBytes.length - offset, length);

        byte[] cleanBytes = new byte[length];
        System.arraycopy(addressBytes, offset, cleanBytes, 0, length);

        return new LTSystemAddress(cleanBytes, format, id);
    }

    public ArmTranReport newArmTranReport(ArmApplication app, ArmTransactionDefinition appTranDef)
    {
        app = APIUtil.checkRequired(app);
        appTranDef = APIUtil.checkRequired(appTranDef);

        return new LTTranReport(allocateOID(), getConnection(), getGuidGenerator(), app, appTranDef);
    }
}
