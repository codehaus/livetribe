package org.livetribe.arm.connection.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class StopMessage implements Message
{
    private final String transIdOID;
    private final byte[] correlator;
    private final long end;
    private final int status;
    private final String message;

    public StopMessage(String transIdOID, byte[] correlator, long end, int status, String message)
    {
        this.transIdOID = transIdOID;
        this.correlator = correlator;
        this.end = end;
        this.status = status;
        this.message = message;
    }

    public String getTransIdOID()
    {
        return transIdOID;
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
