package org.livetribe.arm.impl;

import org.livetribe.arm.GeneralErrorCodes;


/**
 * @version $Revision: $ $Date: $
 */
public interface TranReportErrorCodes extends GeneralErrorCodes
{
    public static final int ADDRESS_NULL = TRANREPORT_CODES + 0;
    public static final int ADDR_TOO_SHORT = TRANREPORT_CODES + 1;
    public static final int FORMAT_ZERO = TRANREPORT_CODES + 2;
}
