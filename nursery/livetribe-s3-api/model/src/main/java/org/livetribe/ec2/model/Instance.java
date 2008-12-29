/**
 *
 * Copyright 2008 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.ec2.model;

import java.util.Date;


/**
 * @version $Revision$ $Date: 2008-05-30 09:40:48 -0700 (Fri, 30 May 2008) $
 */
public class Instance
{
    private final String dnsName;
    private final String imageId;
    private final String id;
    private final InstanceState instanceState;
    private final InstanceType instanceType;
    private final Date launchTime;
    private final Placement placement;
    private final String privateDnsName;
    private String amiLaunchIndex;
    private String keyName;
    private String[] productCodes;
    private String reason;

    public Instance(String dnsName, String imageId, String id, InstanceState instanceState, InstanceType instanceType, Date launchTime, Placement placement, String privateDnsName)
    {
        this.dnsName = dnsName;
        this.imageId = imageId;
        this.id = id;
        this.instanceState = instanceState;
        this.instanceType = instanceType;
        this.launchTime = launchTime;
        this.placement = placement;
        this.privateDnsName = privateDnsName;
    }

    public String getDnsName()
    {
        return dnsName;
    }

    public String getImageId()
    {
        return imageId;
    }

    public String getId()
    {
        return id;
    }

    public InstanceState getInstanceState()
    {
        return instanceState;
    }

    public InstanceType getInstanceType()
    {
        return instanceType;
    }

    public Date getLaunchTime()
    {
        return launchTime;
    }

    public Placement getPlacement()
    {
        return placement;
    }

    public String getPrivateDnsName()
    {
        return privateDnsName;
    }

    public String getAmiLaunchIndex()
    {
        return amiLaunchIndex;
    }

    public void setAmiLaunchIndex(String amiLaunchIndex)
    {
        this.amiLaunchIndex = amiLaunchIndex;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public void setKeyName(String keyName)
    {
        this.keyName = keyName;
    }

    public String[] getProductCodes()
    {
        String[] result = new String[productCodes.length];
        System.arraycopy(productCodes, 0, result, 0, result.length);
        return result;
    }

    public void setProductCodes(String[] productCodes)
    {
        this.productCodes = new String[productCodes.length];
        System.arraycopy(productCodes, 0, this.productCodes, 0, this.productCodes.length);
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }
}
