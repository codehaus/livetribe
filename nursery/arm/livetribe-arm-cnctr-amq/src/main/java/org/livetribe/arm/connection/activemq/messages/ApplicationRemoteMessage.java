package org.livetribe.arm.connection.activemq.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class ApplicationRemoteMessage implements Message
{
    private final byte[] appId;
    private final byte[] systemAddres;

    public ApplicationRemoteMessage(byte[] appId, byte[] systemAddres)
    {
        this.appId = appId;
        this.systemAddres = systemAddres;
    }

    public byte[] getAppId()
    {
        return appId;
    }

    public byte[] getSystemAddres()
    {
        return systemAddres;
    }
}
