package org.livetribe.arm.connection.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class UnblockedMessage implements Message
{
    private final byte[] transId;
    private final byte[] correlator;
    private final long handle;

    public UnblockedMessage(byte[] transId, byte[] correlator, long handle)
    {
        this.transId = transId;
        this.correlator = correlator;
        this.handle = handle;
    }

    public byte[] getTransId()
    {
        return transId;
    }

    public byte[] getCorrelator()
    {
        return correlator;
    }

    public long getHandle()
    {
        return handle;
    }
}
