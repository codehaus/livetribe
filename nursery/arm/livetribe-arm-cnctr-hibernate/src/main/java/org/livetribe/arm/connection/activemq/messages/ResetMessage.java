package org.livetribe.arm.connection.activemq.messages;

/**
 * @version $Revision$ $Date$
 */
public class ResetMessage implements Message
{
    private final byte[] transId;
    private final byte[] correlator;

    public ResetMessage(byte[] transId, byte[] correlator)
    {
        this.transId = transId;
        this.correlator = correlator;
    }

    public byte[] getTransId()
    {
        return transId;
    }

    public byte[] getCorrelator()
    {
        return correlator;
    }
}
