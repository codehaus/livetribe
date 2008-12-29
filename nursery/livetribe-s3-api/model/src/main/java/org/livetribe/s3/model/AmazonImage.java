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
package org.livetribe.s3.model;

/**
 * @version $Revision$ $Date: 2008-05-30 09:40:48 -0700 (Fri, 30 May 2008) $
 */
public class AmazonImage
{
    private String id;
    private AmazonMachineImageState state;
    private String ownerId;
    private boolean pblic;
    private String[] productCodes;
    private Architecture architecture;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public AmazonMachineImageState getState()
    {
        return state;
    }

    public void setState(AmazonMachineImageState state)
    {
        this.state = state;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public boolean isPublic()
    {
        return pblic;
    }

    public void setPublic(boolean pblic)
    {
        this.pblic = pblic;
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

    public Architecture getArchitecture()
    {
        return architecture;
    }

    public void setArchitecture(Architecture architecture)
    {
        this.architecture = architecture;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(" id: ");
        builder.append(id);
        builder.append(" state: ");
        builder.append(state);
        builder.append(" ownerId: ");
        builder.append(ownerId);
        builder.append(" public: ");
        builder.append(pblic);
        builder.append(" [");
        if (productCodes != null)
        {
            for (int i = 0; i < productCodes.length; i++)
            {
                if (i > 0) builder.append(", ");
                builder.append(productCodes[i]);
            }
        }
        builder.append("]");

        return builder.toString();
    }
}
