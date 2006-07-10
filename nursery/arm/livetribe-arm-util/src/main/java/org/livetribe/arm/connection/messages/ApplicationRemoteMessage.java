package org.livetribe.arm.connection.messages;

/**
 * @version $Revision: $ $Date: $
 */
public class ApplicationRemoteMessage implements Message
{
    private final byte[] appId;
    private final byte[] appDefId;
    private final String group;
    private final String instance;
    private final String[] ctxValues;
    private final byte[] systemAddres;

    public ApplicationRemoteMessage(byte[] appId, byte[] appDefId, String group, String instance, String[] ctxValues, byte[] systemAddres)
    {
        this.appId = appId;
        this.appDefId = appDefId;
        this.group = group;
        this.instance = instance;
        this.ctxValues = ctxValues;
        this.systemAddres = systemAddres;
    }

    public byte[] getAppId()
    {
        return appId;
    }

    public byte[] getAppDefId()
    {
        return appDefId;
    }

    public String getGroup()
    {
        return group;
    }

    public String getInstance()
    {
        return instance;
    }

    public String[] getCtxValues()
    {
        return ctxValues;
    }

    public byte[] getSystemAddres()
    {
        return systemAddres;
    }
}
