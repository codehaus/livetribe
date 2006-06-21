package org.livetribe.arm.connection.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class StopMessage implements Message
{
    private final byte[] transId;
    private final byte[] correlator;
    private final long end;
    private final int status;
    private final String message;

    public StopMessage(byte[] transId, byte[] correlator, long end, int status, String message)
    {
        this.transId = transId;
        this.correlator = correlator;
        this.end = end;
        this.status = status;
        this.message = message;
    }

    public byte[] getTransId()
    {
        return transId;
    }

    public byte[] getCorrelator()
    {
        return correlator;
    }

    public long getEnd()
    {
        return end;
    }

    public int getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }
}
