package org.livetribe.arm.connection.activemq.messages;

/**
 * @version $Revision$ $Date$
 */
public class ApplicationRemoteMessage implements Message
{
    private final byte[] appId;
    private final byte[] appDefId;
    private final String group;
    private final String instance;
    private final String[] contextValues;
    private final byte[] systemAddres;

    public ApplicationRemoteMessage(byte[] appId, byte[] appDefId, String group, String instance, String[] contextValues, byte[] systemAddres)
    {
        this.appId = appId;
        this.appDefId = appDefId;
        this.group = group;
        this.instance = instance;
        this.contextValues = contextValues;
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

    public String[] getContextValues()
    {
        return contextValues;
    }

    public byte[] getSystemAddres()
    {
        return systemAddres;
    }
}
