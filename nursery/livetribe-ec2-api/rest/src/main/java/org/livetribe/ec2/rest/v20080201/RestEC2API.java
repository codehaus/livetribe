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
package org.livetribe.ec2.rest.v20080201;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.asyncweb.client.AsyncHttpClient;
import org.apache.asyncweb.client.codec.HttpRequestMessage;
import org.apache.asyncweb.client.codec.HttpResponseMessage;

import org.livetribe.ec2.api.EC2Exception;
import org.livetribe.ec2.api.v20080201.AddressLimitExceededException;
import org.livetribe.ec2.api.v20080201.AuthFailureException;
import org.livetribe.ec2.api.v20080201.EC2API;
import org.livetribe.ec2.api.v20080201.InstanceLimitExceededException;
import org.livetribe.ec2.api.v20080201.InsufficientAddressCapacityException;
import org.livetribe.ec2.api.v20080201.InsufficientInstanceCapacityException;
import org.livetribe.ec2.api.v20080201.InternalErrorException;
import org.livetribe.ec2.api.v20080201.InvalidAMIAttributeItemValueException;
import org.livetribe.ec2.api.v20080201.InvalidAMIIDMalformedException;
import org.livetribe.ec2.api.v20080201.InvalidAMIIDNotFoundException;
import org.livetribe.ec2.api.v20080201.InvalidAMIIDUnavailableException;
import org.livetribe.ec2.api.v20080201.InvalidGroupDuplicateException;
import org.livetribe.ec2.api.v20080201.InvalidGroupInUseException;
import org.livetribe.ec2.api.v20080201.InvalidGroupNotFoundException;
import org.livetribe.ec2.api.v20080201.InvalidGroupReservedException;
import org.livetribe.ec2.api.v20080201.InvalidInstanceIDMalformedException;
import org.livetribe.ec2.api.v20080201.InvalidInstanceIDNotFoundException;
import org.livetribe.ec2.api.v20080201.InvalidKeyPairDuplicateException;
import org.livetribe.ec2.api.v20080201.InvalidKeyPairNotFoundException;
import org.livetribe.ec2.api.v20080201.InvalidManifestException;
import org.livetribe.ec2.api.v20080201.InvalidParameterCombinationException;
import org.livetribe.ec2.api.v20080201.InvalidParameterValueException;
import org.livetribe.ec2.api.v20080201.InvalidPermissionDuplicateException;
import org.livetribe.ec2.api.v20080201.InvalidPermissionMalformedException;
import org.livetribe.ec2.api.v20080201.InvalidReservationIDMalformedException;
import org.livetribe.ec2.api.v20080201.InvalidReservationIDNotFoundException;
import org.livetribe.ec2.api.v20080201.InvalidUserIDMalformedException;
import org.livetribe.ec2.api.v20080201.KeyPair;
import org.livetribe.ec2.api.v20080201.ProductInstanceConfirmation;
import org.livetribe.ec2.api.v20080201.TerminatedInstance;
import org.livetribe.ec2.api.v20080201.UnavailableException;
import org.livetribe.ec2.api.v20080201.UnknownParameterException;
import org.livetribe.ec2.jaxb.Response;
import org.livetribe.ec2.jaxb.v20080201.AllocateAddressResponseType;
import org.livetribe.ec2.jaxb.v20080201.AuthorizeSecurityGroupIngressResponseType;
import org.livetribe.ec2.jaxb.v20080201.BlockDeviceMappingItemType;
import org.livetribe.ec2.jaxb.v20080201.ConfirmProductInstanceResponseType;
import org.livetribe.ec2.jaxb.v20080201.CreateKeyPairResponseType;
import org.livetribe.ec2.jaxb.v20080201.CreateSecurityGroupResponseType;
import org.livetribe.ec2.jaxb.v20080201.DeleteKeyPairResponseType;
import org.livetribe.ec2.jaxb.v20080201.DeleteSecurityGroupResponseType;
import org.livetribe.ec2.jaxb.v20080201.DeregisterImageResponseType;
import org.livetribe.ec2.jaxb.v20080201.DescribeAddressesResponseItemType;
import org.livetribe.ec2.jaxb.v20080201.DescribeAddressesResponseType;
import org.livetribe.ec2.jaxb.v20080201.DescribeImageAttributeResponseType;
import org.livetribe.ec2.jaxb.v20080201.DescribeImagesResponseItemType;
import org.livetribe.ec2.jaxb.v20080201.DescribeImagesResponseType;
import org.livetribe.ec2.jaxb.v20080201.DescribeKeyPairsResponseItemType;
import org.livetribe.ec2.jaxb.v20080201.DescribeKeyPairsResponseType;
import org.livetribe.ec2.jaxb.v20080201.DescribeSecurityGroupsResponseType;
import org.livetribe.ec2.jaxb.v20080201.GroupItemType;
import org.livetribe.ec2.jaxb.v20080201.IpPermissionType;
import org.livetribe.ec2.jaxb.v20080201.IpRangeItemType;
import org.livetribe.ec2.jaxb.v20080201.LaunchPermissionItemType;
import org.livetribe.ec2.jaxb.v20080201.ModifyImageAttributeResponseType;
import org.livetribe.ec2.jaxb.v20080201.ProductCodeItemType;
import org.livetribe.ec2.jaxb.v20080201.ProductCodesSetItemType;
import org.livetribe.ec2.jaxb.v20080201.RegisterImageResponseType;
import org.livetribe.ec2.jaxb.v20080201.ReleaseAddressResponseType;
import org.livetribe.ec2.jaxb.v20080201.ReservationInfoType;
import org.livetribe.ec2.jaxb.v20080201.ResetImageAttributeResponseType;
import org.livetribe.ec2.jaxb.v20080201.RunningInstancesItemType;
import org.livetribe.ec2.jaxb.v20080201.SecurityGroupItemType;
import org.livetribe.ec2.jaxb.v20080201.TerminateInstancesResponseItemType;
import org.livetribe.ec2.jaxb.v20080201.TerminateInstancesResponseType;
import org.livetribe.ec2.jaxb.v20080201.UserIdGroupPairType;
import org.livetribe.ec2.model.AmazonImage;
import org.livetribe.ec2.model.AmazonKernelImage;
import org.livetribe.ec2.model.AmazonMachineImage;
import org.livetribe.ec2.model.AmazonMachineImageState;
import org.livetribe.ec2.model.AmazonRamdiskImage;
import org.livetribe.ec2.model.Architecture;
import org.livetribe.ec2.model.BlockDeviceMappingItem;
import org.livetribe.ec2.model.ImageAttribute;
import org.livetribe.ec2.model.ImageAttributeOperation;
import org.livetribe.ec2.model.Instance;
import org.livetribe.ec2.model.InstanceState;
import org.livetribe.ec2.model.InstanceType;
import org.livetribe.ec2.model.IpPermission;
import org.livetribe.ec2.model.IpProtocol;
import org.livetribe.ec2.model.LaunchPermission;
import org.livetribe.ec2.model.Placement;
import org.livetribe.ec2.model.ReservationInfo;
import org.livetribe.ec2.model.SecurityGroup;
import org.livetribe.ec2.model.UserIdGroupPair;
import org.livetribe.ec2.rest.EC2Callback;
import org.livetribe.ec2.util.Util;


/**
 * @version $Revision$ $Date$
 */
public final class RestEC2API implements EC2API
{
    private final static String VERSION = "2008-02-01";
    private final JAXBContext context20080201;
    private final JAXBContext context;
    private final URL url;
    private final String AWSAccessKeyId;
    private final String secretAccessKey;
    private int timeout = 60;

    public RestEC2API(URL url, String AWSAccessKeyId, String secretAccessKey) throws JAXBException
    {
        if (url == null) throw new IllegalArgumentException("url cannot be null");
        if (AWSAccessKeyId == null) throw new IllegalArgumentException("AWSAccessKeyId cannot be null");
        if (secretAccessKey == null) throw new IllegalArgumentException("secretAccessKey cannot be null");

        this.url = url;
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.context = JAXBContext.newInstance("org.livetribe.ec2.jaxb");
        this.context20080201 = JAXBContext.newInstance("org.livetribe.ec2.jaxb.v20080201");
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public String registerImage(String imageLocation) throws EC2Exception
    {
        if (imageLocation == null) throw new IllegalArgumentException("imageLocation cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "RegisterImage");
        map.put("ImageLocation", imageLocation);
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        RegisterImageResponseType response = (RegisterImageResponseType) call(map);

        return response.getImageId();
    }

    public Set<AmazonImage> describeImages(String[] imageId, String[] owner, String[] executableBy) throws EC2Exception
    {
        Map<String, String> map = new HashMap<String, String>();

        if (imageId != null) for (int i = 0; i < imageId.length; i++) map.put("ImageId." + i, imageId[i]);
        if (owner != null) for (int i = 0; i < owner.length; i++) map.put("Owner." + i, owner[i]);
        if (executableBy != null) for (int i = 0; i < executableBy.length; i++) map.put("ExecutableBy." + i, executableBy[i]);

        map.put("Action", "DescribeImages");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        DescribeImagesResponseType images = (DescribeImagesResponseType) call(map);

        Set<AmazonImage> result = new HashSet<AmazonImage>();

        for (DescribeImagesResponseItemType info : images.getImagesSet().getItem())
        {
            AmazonImage image;
            if ("machine".equals(info.getImageType()))
            {
                AmazonMachineImage machineImage = (AmazonMachineImage) (image = new AmazonMachineImage());
                machineImage.setKernelId(info.getKernelId());
                machineImage.setRamdiskId(info.getRamdiskId());
            }
            else if ("kernel".equals(info.getImageType()))
            {
                image = new AmazonKernelImage();
            }
            else
            {
                image = new AmazonRamdiskImage();
            }

            image.setId(info.getImageId());
            image.setState(AmazonMachineImageState.valueOf(info.getImageState()));
            image.setOwnerId(info.getImageOwnerId());
            image.setPublic(info.isIsPublic());

            if (info.getProductCodes() != null)
            {
                List<ProductCodesSetItemType> list = info.getProductCodes().getItem();
                String[] productCodes = new String[list.size()];
                int i = 0;

                for (ProductCodesSetItemType productCode : list) productCodes[i++] = productCode.getProductCode();

                image.setProductCodes(productCodes);
            }

            image.setArchitecture(Architecture.valueOf(info.getArchitecture()));

            result.add(image);
        }
        return result;
    }

    public boolean deregisterImage(String imageId) throws EC2Exception
    {
        if (imageId == null) throw new IllegalArgumentException("imageId cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DeregisterImage");
        map.put("ImageId", imageId);
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        DeregisterImageResponseType response = (DeregisterImageResponseType) call(map);

        return response.isReturn();
    }

    public ReservationInfo runInstances(String imageId, int minCount, int maxCount, String keyName, String[] securityGroup, String userData, InstanceType instanceType, String availabilityZone, String kernelId, String ramdiskId, String[] BDMVirtualNames, String[] BDMDeviceNames) throws EC2Exception
    {
        if (imageId == null) throw new IllegalArgumentException("imageId cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "RunInstances");
        map.put("ImageId", imageId);
        map.put("MinCount", Integer.toString(minCount));
        map.put("MaxCount", Integer.toString(maxCount));
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        if (keyName != null) map.put("KeyName", keyName);
        if (securityGroup != null) for (int i = 0; i < securityGroup.length; i++) map.put("SecurityGroup." + i, securityGroup[i]);
        if (userData != null) map.put("UserData", userData);
        if (instanceType != null) map.put("InstanceType", instanceType.toString());
        if (availabilityZone != null) map.put("Placement.AvailabilityZone", availabilityZone);
        if (kernelId != null) map.put("KernelId", kernelId);
        if (ramdiskId != null) map.put("RamdiskId", ramdiskId);
        if (BDMVirtualNames != null) for (int i = 0; i < BDMVirtualNames.length; i++) map.put("BlockDeviceMapping." + i + ".VirtualName", BDMVirtualNames[i]);
        if (BDMDeviceNames != null) for (int i = 0; i < BDMDeviceNames.length; i++) map.put("BlockDeviceMapping." + i + ".DeviceName", BDMDeviceNames[i]);

        return obtainReservationInfo((ReservationInfoType) call(map));
    }

    public ReservationInfo describeInstances(String[] instanceIds) throws EC2Exception
    {

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DescribeInstances");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        if (instanceIds != null) for (int i = 0; i < instanceIds.length; i++) map.put("InstanceId." + i, instanceIds[i]);

        return obtainReservationInfo((ReservationInfoType) call(map));
    }

    public List<TerminatedInstance> terminateInstances(String[] instanceIds) throws EC2Exception
    {
        if (instanceIds == null) throw new IllegalArgumentException("instanceIds cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "TerminateInstances");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        for (int i = 0; i < instanceIds.length; i++) map.put("InstanceId." + i, instanceIds[i]);

        TerminateInstancesResponseType response = (TerminateInstancesResponseType) call(map);
        List<TerminatedInstance> instances = new ArrayList<TerminatedInstance>();

        for (TerminateInstancesResponseItemType item : response.getInstancesSet().getItem())
        {
            InstanceState shutdownState = new InstanceState((short) item.getShutdownState().getCode(), InstanceState.State.getValue(item.getShutdownState().getName()));
            InstanceState prevState = new InstanceState((short) item.getPreviousState().getCode(), InstanceState.State.getValue(item.getPreviousState().getName()));

            instances.add(new TerminatedInstance(item.getInstanceId(), shutdownState, prevState));
        }

        return instances;
    }

    public ProductInstanceConfirmation confirmProductInstance(String productCode, String instanceId) throws EC2Exception
    {
        if (productCode == null) throw new IllegalArgumentException("productCode cannot be null");
        if (instanceId == null) throw new IllegalArgumentException("instanceId cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "ConfirmProductInstance");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("ProductCode", productCode);
        map.put("InstanceId", instanceId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        ConfirmProductInstanceResponseType response = (ConfirmProductInstanceResponseType) call(map);

        if (response.getOwnerId() != null) return new ProductInstanceConfirmation(response.isReturn(), response.getOwnerId());
        else return new ProductInstanceConfirmation(response.isReturn());
    }

    public KeyPair createKeyPair(String keyName) throws EC2Exception
    {
        if (keyName == null) throw new IllegalArgumentException("keyName cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "CreateKeyPair");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("KeyName", keyName);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        CreateKeyPairResponseType response = (CreateKeyPairResponseType) call(map);

        if (response.getKeyMaterial() != null) return new KeyPair(response.getKeyName(), response.getKeyFingerprint(), response.getKeyMaterial());
        else return new KeyPair(response.getKeyName(), response.getKeyFingerprint());
    }

    public List<KeyPair> describeKeyPairs(String[] keyNames) throws EC2Exception
    {
        if (keyNames == null) throw new IllegalArgumentException("keyNames cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DescribeKeyPairs");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        for (int i = 0; i < keyNames.length; i++) map.put("KeyName." + i, keyNames[i]);

        DescribeKeyPairsResponseType response = (DescribeKeyPairsResponseType) call(map);
        List<KeyPair> keyPairs = new ArrayList<KeyPair>();

        for (DescribeKeyPairsResponseItemType keyPair : response.getKeySet().getItem())
        {
            keyPairs.add(new KeyPair(keyPair.getKeyName(), keyPair.getKeyFingerprint()));
        }

        return keyPairs;
    }

    public boolean deleteKeyPair(String keyName) throws EC2Exception
    {
        if (keyName == null) throw new IllegalArgumentException("keyName cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DeleteKeyPair");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("KeyName", keyName);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        DeleteKeyPairResponseType response = (DeleteKeyPairResponseType) call(map);

        return response.isReturn();
    }

    public boolean modifyImageAttribute(String imageId, ImageAttribute attribute, ImageAttributeOperation operation, String[] userId, String[] userGroup, String[] productCode) throws EC2Exception
    {
        if (imageId == null) throw new IllegalArgumentException("imageId cannot be null");
        if (attribute == null) throw new IllegalArgumentException("attributeType cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "ModifyImageAttribute");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("ImageId", imageId);
        map.put("Attribute", attribute.toString());
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        if (attribute == ImageAttribute.launchPermission)
        {
            if (operation == null) throw new IllegalArgumentException("operationType cannot be null");
            if (userId == null) throw new IllegalArgumentException("userId cannot be null");
            if (userGroup == null) throw new IllegalArgumentException("userGroup cannot be null");

            map.put("OperationType", operation.toString());

            for (int i = 0; i < userId.length; i++) map.put("UserId." + i, userId[i]);
            for (int i = 0; i < userGroup.length; i++) map.put("UserGroup." + i, userGroup[i]);
        }
        else if (attribute == ImageAttribute.productCodes)
        {
            if (productCode == null) throw new IllegalArgumentException("productCode cannot be null");

            for (int i = 0; i < productCode.length; i++) map.put("ProductCode." + i, productCode[i]);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported image attribute type: " + attribute);
        }

        ModifyImageAttributeResponseType response = (ModifyImageAttributeResponseType) call(map);

        return response.isReturn();
    }

    public Object describeImageAttribute(String imageId, ImageAttribute attribute) throws EC2Exception
    {
        if (imageId == null) throw new IllegalArgumentException("imageId cannot be null");
        if (attribute == null) throw new IllegalArgumentException("attributeType cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DescribeImageAttribute");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("ImageId", imageId);
        map.put("Attribute", attribute.toString());
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        DescribeImageAttributeResponseType response = (DescribeImageAttributeResponseType) call(map);

        if (attribute == ImageAttribute.launchPermission)
        {
            List<LaunchPermissionItemType> results = response.getLaunchPermission().getItem();
            List<LaunchPermission> permissions = new ArrayList<LaunchPermission>(results.size());

            for (LaunchPermissionItemType result : results)
            {
                permissions.add(new LaunchPermission(result.getGroup(), result.getUserId()));
            }

            return permissions;
        }
        else if (attribute == ImageAttribute.productCodes)
        {
            List<ProductCodeItemType> results = response.getProductCodes().getItem();
            List<String> codes = new ArrayList<String>(results.size());

            for (ProductCodeItemType result : results)
            {
                codes.add(result.getProductCode());
            }

            return codes;
        }
        else if (attribute == ImageAttribute.kernel)
        {
            return response.getKernel();
        }
        else if (attribute == ImageAttribute.ramdisk)
        {
            return response.getRamdisk().getValue();
        }
        else if (attribute == ImageAttribute.blockDeviceMapping)
        {
            List<BlockDeviceMappingItemType> results = response.getBlockDeviceMapping().getItem();
            List<BlockDeviceMappingItem> mappings = new ArrayList<BlockDeviceMappingItem>(results.size());

            for (BlockDeviceMappingItemType result : results)
            {
                mappings.add(new BlockDeviceMappingItem(result.getVirtualName(), result.getDeviceName()));
            }

            return mappings;
        }
        else
        {
            throw new AssertionError("Should not have reached this condition");
        }
    }

    public boolean resetImageAttribute(String imageId, ImageAttribute attribute) throws EC2Exception
    {
        if (imageId == null) throw new IllegalArgumentException("imageId cannot be null");
        if (attribute == null) throw new IllegalArgumentException("attributeType cannot be null");
        if (attribute != ImageAttribute.launchPermission) throw new IllegalArgumentException("Can only reset launchPermission attribute");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "ResetImageAttribute");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("ImageId", imageId);
        map.put("Attribute", attribute.toString());
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        ResetImageAttributeResponseType response = (ResetImageAttributeResponseType) call(map);

        return response.isReturn();
    }

    public boolean createSecurityGroup(String name, String description) throws EC2Exception
    {
        if (name == null) throw new IllegalArgumentException("Group name cannot be null");
        if (description == null) throw new IllegalArgumentException("Group description cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "CreateSecurityGroup");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("GroupName", name);
        map.put("GroupDescription", description);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        CreateSecurityGroupResponseType response = (CreateSecurityGroupResponseType) call(map);

        return response.isReturn();
    }

    public List<SecurityGroup> describeSecurityGroups(String[] names) throws EC2Exception
    {
        if (names == null) throw new IllegalArgumentException("names cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DescribeSecurityGroups");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        for (int i = 0; i < names.length; i++) map.put("GroupName." + i, names[i]);

        DescribeSecurityGroupsResponseType response = (DescribeSecurityGroupsResponseType) call(map);
        List<SecurityGroup> securityGroups = new ArrayList<SecurityGroup>();

        for (SecurityGroupItemType group : response.getSecurityGroupInfo().getItem())
        {
            List<IpPermission> permissions = new ArrayList<IpPermission>(group.getIpPermissions().getItem().size());

            for (IpPermissionType permission : group.getIpPermissions().getItem())
            {
                List<UserIdGroupPair> groups = new ArrayList<UserIdGroupPair>(permission.getGroups().getItem().size());

                for (UserIdGroupPairType pair : permission.getGroups().getItem()) groups.add(new UserIdGroupPair(pair.getUserId(), pair.getGroupName()));

                List<String> ranges = new ArrayList<String>(permission.getIpRanges().getItem().size());

                for (IpRangeItemType range : permission.getIpRanges().getItem()) ranges.add(range.getCidrIp());

                permissions.add(new IpPermission(IpProtocol.valueOf(permission.getIpProtocol()), permission.getFromPort(), permission.getToPort(), groups, ranges));
            }

            securityGroups.add(new SecurityGroup(group.getOwnerId(), group.getGroupName(), group.getGroupDescription(), permissions));
        }

        return securityGroups;
    }

    public boolean deleteSecurityGroup(String name) throws EC2Exception
    {
        if (name == null) throw new IllegalArgumentException("Group name cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DeleteSecurityGroup");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("GroupName", name);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        DeleteSecurityGroupResponseType response = (DeleteSecurityGroupResponseType) call(map);

        return response.isReturn();
    }

    public boolean authorizeSecurityGroupIngress(String name,
                                                 String sourceSecurityGroupName, String sourceSecurityGroupOwnerId,
                                                 IpProtocol ipProtocol, int fromPort, int toPort, String cidrIp) throws EC2Exception
    {
        if (name == null) throw new IllegalArgumentException("Group name cannot be null");

        boolean userGroupPairPermission = sourceSecurityGroupName != null && sourceSecurityGroupOwnerId != null;
        boolean cidrIpPermission = ipProtocol != null && cidrIp != null;

        if (userGroupPairPermission && cidrIpPermission) throw new IllegalArgumentException("Cannot set both user/group pair and CIDR IP permissions at the same time");
        if (!(userGroupPairPermission || cidrIpPermission)) throw new IllegalArgumentException("Not enough parameters specified for either user/group pair and CIDR IP permissions");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "AuthorizeSecurityGroupIngress");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("GroupName", name);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        if (userGroupPairPermission)
        {
            map.put("SourceSecurityGroupName", sourceSecurityGroupName);
            map.put("SourceSecurityGroupOwnerId", sourceSecurityGroupOwnerId);
        }
        else
        {
            map.put("IpProtocol", ipProtocol.toString());
            map.put("FromPort", Integer.toString(fromPort));
            map.put("ToPort", Integer.toString(toPort));
            map.put("CidrIp", cidrIp);
        }

        AuthorizeSecurityGroupIngressResponseType response = (AuthorizeSecurityGroupIngressResponseType) call(map);

        return response.isReturn();
    }

    public boolean revokeSecurityGroupIngress(String name,
                                              String sourceSecurityGroupName, String sourceSecurityGroupOwnerId,
                                              IpProtocol ipProtocol, int fromPort, int toPort, String cidrIp) throws EC2Exception
    {
        if (name == null) throw new IllegalArgumentException("Group name cannot be null");

        boolean userGroupPairPermission = sourceSecurityGroupName != null && sourceSecurityGroupOwnerId != null;
        boolean cidrIpPermission = ipProtocol != null && cidrIp != null;

        if (userGroupPairPermission && cidrIpPermission) throw new IllegalArgumentException("Cannot set both user/group pair and CIDR IP permissions at the same time");
        if (!(userGroupPairPermission || cidrIpPermission)) throw new IllegalArgumentException("Not enough parameters specified for either user/group pair and CIDR IP permissions");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "RevokeSecurityGroupIngress");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("GroupName", name);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        if (userGroupPairPermission)
        {
            map.put("SourceSecurityGroupName", sourceSecurityGroupName);
            map.put("SourceSecurityGroupOwnerId", sourceSecurityGroupOwnerId);
        }
        else
        {
            map.put("IpProtocol", ipProtocol.toString());
            map.put("FromPort", Integer.toString(fromPort));
            map.put("ToPort", Integer.toString(toPort));
            map.put("CidrIp", cidrIp);
        }

        AuthorizeSecurityGroupIngressResponseType response = (AuthorizeSecurityGroupIngressResponseType) call(map);

        return response.isReturn();
    }

    public String allocateAddress() throws EC2Exception
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "AllocateAddress");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        AllocateAddressResponseType response = (AllocateAddressResponseType) call(map);

        return response.getPublicIp();
    }

    public Map<String, List<String>> DescribeAddresses(String[] publicIps) throws EC2Exception
    {
        if (publicIps == null) throw new IllegalArgumentException("publicIps cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DescribeAddresses");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        for (int i = 0; i < publicIps.length; i++) map.put("PublicIp." + i, publicIps[i]);

        DescribeAddressesResponseType response = (DescribeAddressesResponseType) call(map);
        Map<String, List<String>> addresses = new HashMap<String, List<String>>();

        for (DescribeAddressesResponseItemType address : response.getAddressesSet().getItem())
        {
            List<String> ips = addresses.get(address.getInstanceId());
            if (ips == null) addresses.put(address.getInstanceId(), ips = new ArrayList<String>());
            ips.add(address.getPublicIp());
        }

        return addresses;
    }

    public boolean releaseAddress(String publicIp) throws EC2Exception
    {
        if (publicIp == null) throw new IllegalArgumentException("publicIp cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "ReleaseAddress");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("PublicIp", publicIp);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        ReleaseAddressResponseType response = (ReleaseAddressResponseType) call(map);

        return response.isReturn();
    }

    public boolean associateAddress(String instanceId, String publicIp) throws EC2Exception
    {
        if (instanceId == null) throw new IllegalArgumentException("instanceId cannot be null");
        if (publicIp == null) throw new IllegalArgumentException("publicIp cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "AssociateAddress");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("InstanceId", instanceId);
        map.put("PublicIp", publicIp);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        ReleaseAddressResponseType response = (ReleaseAddressResponseType) call(map);

        return response.isReturn();
    }

    public boolean disassociateAddress(String publicIp) throws EC2Exception
    {
        if (publicIp == null) throw new IllegalArgumentException("publicIp cannot be null");

        Map<String, String> map = new HashMap<String, String>();

        map.put("Action", "DisassociateAddress");
        map.put("AWSAccessKeyId", AWSAccessKeyId);
        map.put("PublicIp", publicIp);
        map.put("SignatureVersion", "1");
        map.put("Version", VERSION);
        map.put("Timestamp", Util.iso8601Conversion(new Date()));

        ReleaseAddressResponseType response = (ReleaseAddressResponseType) call(map);

        return response.isReturn();
    }

    private ReservationInfo obtainReservationInfo(ReservationInfoType reservationInfoType)
    {
        List<GroupItemType> elements = reservationInfoType.getGroupSet().getItem();
        String[] groups = new String[elements.size()];
        for (int i = 0; i < groups.length; i++) groups[i] = elements.get(i).getGroupId();

        Instance[] instances = new Instance[reservationInfoType.getInstancesSet().getItem().size()];
        for (int i = 0; i < instances.length; i++)
        {
            RunningInstancesItemType riit = reservationInfoType.getInstancesSet().getItem().get(i);
            InstanceState state = new InstanceState((short) riit.getInstanceState().getCode(), InstanceState.State.getValue(riit.getInstanceState().getName()));

            instances[i] = new Instance(riit.getDnsName(), riit.getImageId(), riit.getInstanceId(), state, InstanceType.getValue(riit.getInstanceType()), riit.getLaunchTime().toGregorianCalendar().getTime(), new Placement(riit.getPlacement().getAvailabilityZone()), riit.getPrivateDnsName());
        }

        return new ReservationInfo(reservationInfoType.getReservationId(), reservationInfoType.getOwnerId(), groups, instances);
    }

    private Object call(Map<String, String> map) throws EC2Exception
    {
        assert !map.containsKey("Signature");

        try
        {
            map.put("Signature", Util.sign(map, secretAccessKey));

            EC2Callback callback = new EC2Callback();

            HttpRequestMessage request = new HttpRequestMessage(url, callback);

            request.getParameters().putAll(map);
            request.setTimeOut(timeout * 1000);

            AsyncHttpClient ahc = new AsyncHttpClient();
            ahc.setTcpNoDelay(true);
            ahc.sendRequest(request);

            HttpResponseMessage response = callback.getMessage(timeout, TimeUnit.SECONDS);

            if (response.getStatusCode() == 200)
            {
                JAXBElement element = (JAXBElement) context20080201.createUnmarshaller().unmarshal(new ByteArrayInputStream(response.getContent()));
                return element.getValue();
            }
            else
            {
                Response errorResponse = (Response) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(response.getContent()));
                String errorCode = errorResponse.getErrors().getError().get(0).getCode();

                if ("AddressLimitExceeded".equals(errorCode)) throw new AddressLimitExceededException();
                else if ("AuthFailure".equals(errorCode)) throw new AuthFailureException();
                else if ("InvalidManifest".equals(errorCode)) throw new InvalidManifestException();
                else if ("InvalidAMIID.Malformed".equals(errorCode)) throw new InvalidAMIIDMalformedException();
                else if ("InvalidAMIID.NotFound".equals(errorCode)) throw new InvalidAMIIDNotFoundException();
                else if ("InvalidAMIID.Unavailable".equals(errorCode)) throw new InvalidAMIIDUnavailableException();
                else if ("InvalidInstanceID.Malformed".equals(errorCode)) throw new InvalidInstanceIDMalformedException();
                else if ("InvalidInstanceID.NotFound".equals(errorCode)) throw new InvalidInstanceIDNotFoundException();
                else if ("InvalidKeyPair.NotFound".equals(errorCode)) throw new InvalidKeyPairNotFoundException();
                else if ("InvalidKeyPair.Duplicate".equals(errorCode)) throw new InvalidKeyPairDuplicateException();
                else if ("InvalidGroup.NotFound".equals(errorCode)) throw new InvalidGroupNotFoundException();
                else if ("InvalidGroup.Duplicate".equals(errorCode)) throw new InvalidGroupDuplicateException();
                else if ("InvalidGroup.InUse".equals(errorCode)) throw new InvalidGroupInUseException();
                else if ("InvalidGroup.Reserved".equals(errorCode)) throw new InvalidGroupReservedException();
                else if ("InvalidParameterValue".equals(errorCode)) throw new InvalidParameterValueException();
                else if ("InvalidPermission.Duplicate".equals(errorCode)) throw new InvalidPermissionDuplicateException();
                else if ("InvalidPermission.Malformed".equals(errorCode)) throw new InvalidPermissionMalformedException();
                else if ("InvalidReservationID.Malformed".equals(errorCode)) throw new InvalidReservationIDMalformedException();
                else if ("InvalidReservationID.NotFound".equals(errorCode)) throw new InvalidReservationIDNotFoundException();
                else if ("InstanceLimitExceeded".equals(errorCode)) throw new InstanceLimitExceededException();
                else if ("InvalidParameterCombination".equals(errorCode)) throw new InvalidParameterCombinationException();
                else if ("InvalidUserID.Malformed".equals(errorCode)) throw new InvalidUserIDMalformedException();
                else if ("InvalidAMIAttributeItemValue".equals(errorCode)) throw new InvalidAMIAttributeItemValueException();
                else if ("UnknownParameter".equals(errorCode)) throw new UnknownParameterException();
                else if ("InsufficientAddressCapacity".equals(errorCode)) throw new InsufficientAddressCapacityException();
                else if ("InsufficientInstanceCapacity".equals(errorCode)) throw new InsufficientInstanceCapacityException();
                else if ("Unavailable".equals(errorCode)) throw new UnavailableException();

                else throw new InternalErrorException();
            }
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
            throw new EC2Exception("Thread interrupted", ie);
        }
        catch (JAXBException jaxbe)
        {
            throw new EC2Exception("Error unmarshalling XML", jaxbe);
        }
        catch (SignatureException se)
        {
            throw new EC2Exception("Unable to sign request", se);
        }
    }

}
