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
package org.livetribe.ec2.api.v20080201;

import java.util.List;
import java.util.Set;

import org.livetribe.ec2.api.EC2Exception;
import org.livetribe.ec2.model.AmazonImage;
import org.livetribe.ec2.model.ImageAttribute;
import org.livetribe.ec2.model.ImageAttributeOperation;
import org.livetribe.ec2.model.InstanceType;
import org.livetribe.ec2.model.ReservationInfo;


/**
 * @version $Revision$ $Date$
 */
public interface EC2API
{
    String registerImage(String imageLocation) throws EC2Exception;

    Set<AmazonImage> describeImages(String[] imageId, String[] owner, String[] executedBy) throws EC2Exception;

    boolean deregisterImage(String imageId) throws EC2Exception;

    ReservationInfo runInstances(String imageId, int minCount, int maxCount,
                                 String keyName,
                                 String[] securityGroup,
                                 String userData,
                                 InstanceType instanceType,
                                 String availabilityZone,
                                 String kernelId,
                                 String ramdiskId,
                                 String[] BDMVirtualNames,
                                 String[] BDMDeviceNames) throws EC2Exception;

    ReservationInfo describeInstances(String[] instanceIds) throws EC2Exception;

    public List<TerminatedInstance> terminateInstances(String[] instanceIds) throws EC2Exception;

    public ProductInstanceConfirmation confirmProductInstance(String productCode, String instanceId) throws EC2Exception;

    public KeyPair createKeyPair(String keyName) throws EC2Exception;

    public List<KeyPair> describeKeyPairs(String[] keyNames) throws EC2Exception;

    public boolean deleteKeyPair(String keyName) throws EC2Exception;

    public boolean modifyImageAttribute(String imageId, ImageAttribute attribute, ImageAttributeOperation operation, String[] userId, String[] userGroup, String[] productCode) throws EC2Exception;

    public Object describeImageAttribute(String imageId, ImageAttribute attribute) throws EC2Exception;

    public boolean resetImageAttribute(String imageId, ImageAttribute attribute) throws EC2Exception;
}
