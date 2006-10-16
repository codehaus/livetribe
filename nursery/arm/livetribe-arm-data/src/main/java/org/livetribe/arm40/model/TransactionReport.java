/**
 *  Copyright 2006 Simula Labs Inc., 4676 Admiralty Way, Suite 520 Marina del Rey, CA 90292 U.S.A. All rights reserved.
 */
package org.livetribe.arm40.model;

/**
 * @version $Revision$ $Date$
 */
public class TransactionReport extends Model
{
    private Application application;
    private TransactionDefinition transactionDefinition;

    public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public TransactionDefinition getTransactionDefinition()
    {
        return transactionDefinition;
    }

    public void setTransactionDefinition(TransactionDefinition transactionDefinition)
    {
        this.transactionDefinition = transactionDefinition;
    }
}
