package org.livetribe.arm.impl;

import org.livetribe.arm.GeneralErrorCodes;


/**
 * @version $Revision: $ $Date: $
 */
public interface TransactionErrorCodes extends GeneralErrorCodes
{
    public static final int TOKEN_TOO_LONG = TRANSACTION_CODES + 0;
    public static final int TOKEN_TOO_SHORT = TRANSACTION_CODES + 1;
    public static final int ID_TOO_SHORT = TRANSACTION_CODES + 2;
    public static final int ID_NULL = TRANSACTION_CODES + 3;
    public static final int ID_PROP_TOO_LONG = TRANSACTION_CODES + 4;
    public static final int URI_TOO_LONG = TRANSACTION_CODES + 5;
    public static final int TRANS_DEF_NULL = TRANSACTION_CODES + 6;
}
