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
    public static final int INDEX_OUT_OF_RANGE = TRANSACTION_CODES + 7;
    public static final int ID_PROP_IGNORED = TRANSACTION_CODES + 8;
    public static final int NOT_ALL_BLOCKS_REMOVED = TRANSACTION_CODES + 9;
}
