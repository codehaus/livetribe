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
import java.util.Map;
import java.util.Set;

import org.livetribe.ec2.api.EC2Exception;
import org.livetribe.ec2.model.AmazonImage;
import org.livetribe.ec2.model.AvailabilityZone;
import org.livetribe.ec2.model.ImageAttribute;
import org.livetribe.ec2.model.ImageAttributeOperation;
import org.livetribe.ec2.model.InstanceType;
import org.livetribe.ec2.model.IpProtocol;
import org.livetribe.ec2.model.ReservationInfo;
import org.livetribe.ec2.model.SecurityGroup;


/**
 * @version $Revision$ $Date$
 */
public interface EC2API
{
    /**
     * Registers an AMI with Amazon EC2. Images must be registered before they
     * can be launched. For more information, see <code>runInstances()</code>.
     * <p/>
     * Each AMI is associated with an unique ID which is provided by the Amazon
     * EC2 service through the <code>RegisterImage</code> operation. During
     * registration, Amazon EC2 retrieves the specified image manifest from
     * Amazon S3 and verifies that the image is owned by the user registering
     * the image.
     * <p/>
     * The image manifest is retrieved once and stored within the Amazon EC2.
     * Any modifications to an image in Amazon S3 invalidates this registration.
     * If you make changes to an image, deregister the previous image and
     * register the new image. For more information, see
     * <code>deregisterImage()</code>.
     *
     * @param imageLocation Full path to your AMI manifest in Amazon S3 storage.
     * @return Unique ID of the newly registered machine image.
     * @throws EC2Exception if any error occurs
     * @see #runInstances(String, int, int, String, String[], String, org.livetribe.ec2.model.InstanceType, String, String, String, String[], String[])
     * @see #deregisterImage(String)
     */
    String registerImage(String imageLocation) throws EC2Exception;

    /**
     * Returns information about AMIs, AKIs, and ARIs available to the user.
     * Information returned includes image type, product codes, architecture,
     * and kernel and RAM disk IDs. Images available to the user include public
     * images available for any user to launch, private images owned by the
     * user making the request, and private images owned by other users for
     * which the user has explicit launch permissions.
     *
     * @param imageId    a list of image descriptions
     * @param owner      a list of owners of AMIs to describe
     * @param executedBy a list of AMIs for which specified users have access
     * @return a list of image descriptions
     * @throws EC2Exception if any error occurs
     */
    Set<AmazonImage> describeImages(String[] imageId, String[] owner, String[] executedBy) throws EC2Exception;

    /**
     * Deregisters an AMI. Once deregistered, instances of the AMI can no
     * longer be launched.
     *
     * @param imageId unique ID of a machine image, returned by a call to <code>registerImage()</code> or <code>describeImages()</code>.
     * @return true if deregistration succeeded; otherwise false.
     * @throws EC2Exception if any error occurs
     * @see #registerImage(String)
     * @see #deregisterImage(String)
     */
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

    /**
     * Returns information about instances that you own.
     * <p/>
     * If you specify one or more instance IDs, Amazon EC2 returns information
     * for those instances. If you do not specify instance IDs, Amazon EC2
     * returns information for all relevant instances. If you specify an
     * invalid instance ID, a fault is returned. If you specify an instance
     * that you do not own, it will not be included in the returned results.
     * <p/>
     * Recently terminated instances might appear in the returned results.
     * This interval is usually less than one hour.
     *
     * @param instanceIds a set of instances IDs of which to get the status
     * @return A description of the status of all requested instances.
     * @throws EC2Exception if any error occurs
     */
    ReservationInfo describeInstances(String[] instanceIds) throws EC2Exception;

    /**
     * Requests a reboot of one or more instances. This operation is
     * asynchronous; it only queues a request to reboot the specified
     * instance(s). The operation will succeed if the instances are valid and
     * belong to the user. Requests to reboot terminated instances are ignored.
     *
     * @param instanceIds One or more instance IDs.
     * @return true if the operation succeeded, otherwise false.
     * @throws EC2Exception if any error occurs
     */
    boolean rebootInstances(String[] instanceIds) throws EC2Exception;

    /**
     * Shuts down one or more instances. This operation is idempotent; if you terminate an instance more than once, each call will succeed.
     * <p/>
     * Terminated instances will remain visible after termination (approximately one hour).
     *
     * @param instanceIds One or more instance IDs
     * @return a list of <code>TerminatedInstance</code> instances describing the current and new state of each instance specified
     * @throws EC2Exception if any error occurs
     */
    List<TerminatedInstance> terminateInstances(String[] instanceIds) throws EC2Exception;

    /**
     * Confirms that the specified product code is attached to the specified
     * instance.
     * <p/>
     * The method can only be executed by the owner of the AMI. This feature is
     * useful when an AMI owner is providing support and wants to verify
     * whether a user's instance is eligible.
     *
     * @param productCode The product code to confirm.
     * @param instanceId  The instance for which to confirm the product code.
     * @return a <code>ProductInstanceConfirmation</code> response POJO
     * @throws EC2Exception if any error occurs
     */
    ProductInstanceConfirmation confirmProductInstance(String productCode, String instanceId) throws EC2Exception;

    /**
     * Creates a new 2048 bit RSA key pair and returns a unique ID that can be
     * used to reference this key pair when launching new instances. For more
     * information, see <code>runInstances()</code>.
     *
     * @param keyName a unique name for the key pair.
     * @return a <code>KeyPair</code> response POJO
     * @throws EC2Exception if any error occurs
     * @see #runInstances(String, int, int, String, String[], String, org.livetribe.ec2.model.InstanceType, String, String, String, String[], String[])
     */
    KeyPair createKeyPair(String keyName) throws EC2Exception;

    /**
     * Returns information about key pairs available to you. If you specify key
     * pairs, information about those key pairs is returned. Otherwise,
     * information for all registered key pairs is returned.
     *
     * @param keyNames Key pair IDs to describe
     * @return a list of key pair descriptions
     * @throws EC2Exception if any error occurs
     */
    List<KeyPair> describeKeyPairs(String[] keyNames) throws EC2Exception;

    /**
     * Deletes a key pair.
     *
     * @param keyName name of the key pair to delete
     * @return true if the key was successfully deleted. Otherwise, false.
     * @throws EC2Exception if any error occurs
     */
    boolean deleteKeyPair(String keyName) throws EC2Exception;

    /**
     * Modifies an attribute of an AMI.
     *
     * @param imageId     AMI ID to modify
     * @param attribute   Specifies the attribute to modify. See the preceding attributes table for supported attributes.
     * @param operation   Specifies the operation to perform on the attribute. See the preceding attributes table for supported operations for attributes.
     * @param userId      User IDs to add to or remove from the <code>launchPermission</code> attribute.
     * @param userGroup   User groups to add to or remove from the <code>launchPermission</code> attribute. Currently, the <code>all</code> group is available, which will make it a public AMI.
     * @param productCode Attaches a product code to the AMI. Currently only one product code can be associated with an AMI. Once set, the product code cannot be changed or reset.
     * @return true if the operation succeeded, otherwise false.
     * @throws EC2Exception if any error occurs
     */
    boolean modifyImageAttribute(String imageId, ImageAttribute attribute, ImageAttributeOperation operation, String[] userId, String[] userGroup, String[] productCode) throws EC2Exception;

    /**
     * Returns information about an attribute of an AMI. Only one attribute can
     * be specified per call.
     *
     * @param imageId   ID of the AMI for which an attribute will be described
     * @param attribute specifies the attribute to describe
     * @return information on the attribute that was requested
     * @throws EC2Exception if any error occurs
     */
    Object describeImageAttribute(String imageId, ImageAttribute attribute) throws EC2Exception;

    /**
     * Resets an attribute of an AMI to its default value.
     * <p/>
     * <b>Note:</b> The <code>productCodes</code> attribute cannot be reset.
     *
     * @param imageId   ID of the AMI for which an attribute will be described.
     * @param attribute Specifies the attribute to reset. Currently, only
     *                  <code>launchPermission</code> is supported. In the case of
     *                  <code>launchPermission</code>, all public and explicit launch
     *                  permissions for the AMI are revoked.
     * @return true if the operation succeeded, otherwise false.
     * @throws EC2Exception if any error occurs
     */
    boolean resetImageAttribute(String imageId, ImageAttribute attribute) throws EC2Exception;

    /**
     * Creates a new security group.
     * <p/>
     * Every instance is launched in a security group. If no security group is
     * specified during launch, the instances are launched in the default
     * security group. Instances within the same security group have
     * unrestricted network access to each other. Instances will reject network
     * access attempts from other instances in a different security group. As
     * the owner of instances you can grant or revoke specific permissions
     * using the <code>authorizeSecurityGroupIngress()</code> and
     * <code>revokeSecurityGroupIngress()</code> operations.
     *
     * @param name        name of the new security group
     * @param description description of the new security group
     * @return true if call succeeded. Otherwise, false.
     * @throws EC2Exception if any error occurs
     * @see #authorizeSecurityGroupIngress(String, String, String, org.livetribe.ec2.model.IpProtocol, int, int, String)
     * @see #revokeSecurityGroupIngress(String, String, String, org.livetribe.ec2.model.IpProtocol, int, int, String)
     */
    boolean createSecurityGroup(String name, String description) throws EC2Exception;

    /**
     * Returns information about security groups that you own.
     * <p/>
     * If you specify security group names, information about those security
     * group is returned. Otherwise, information for all security group is
     * returned. If you specify a group that does not exist, a fault is
     * returned.
     *
     * @param names list of security groups to describe
     * @return information about security groups
     * @throws EC2Exception if any error occurs
     */
    List<SecurityGroup> describeSecurityGroups(String[] names) throws EC2Exception;

    /**
     * Deletes a security group.
     * <p/>
     * If you attempt to delete a security group that contains instances, a
     * fault is returned.
     * <p/>
     * If you attempt to delete a security group that is referenced by another
     * security group, a fault is returned. For example, if security group B
     * has a rule that allows access from security group A, security group A
     * cannot be deleted until the allow rule is removed.
     *
     * @param name name of the security group to delete
     * @return true if the group is deleted. Otherwise, false.
     * @throws EC2Exception if any error occurs
     */
    boolean deleteSecurityGroup(String name) throws EC2Exception;

    /**
     * Adds permissions to a security group.
     * <p/>
     * Permissions are specified by the IP protocol (TCP, UDP or ICMP), the
     * source of the request (by IP range or an Amazon EC2 user-group pair),
     * the source and destination port ranges (for TCP and UDP), and the ICMP
     * codes and types (for ICMP). When authorizing ICMP, -1 can be used as a
     * wildcard in the type and code fields.
     * <p/>
     * Permission changes are propagated to instances within the security group
     * as quickly as possible. However, depending on the number of instances, a
     * small delay might occur.
     * <p/>
     * When authorizing a user/group pair permission, name,
     * sourceSecurityGroupName and sourceSecurityGroupOwnerId must be specified.
     * When authorizing a CIDR IP permission, name, ipProtocol, fromPort,
     * toPort and cidrIp must be specified. Mixing these two types of parameters
     * is not allowed.
     *
     * @param name                       Name of the group to modify.
     * @param sourceSecurityGroupName    Name of security group to authorize access to when operating on a user/group pair.
     * @param sourceSecurityGroupOwnerId Owner of security group to authorize access to when operating on a user/group pair.
     * @param ipProtocol                 IP protocol to authorize access to when operating on a CIDR IP.
     * @param fromPort                   Bottom of port range to authorize access to when operating on a CIDR IP. This contains the ICMP type if ICMP is being authorized.
     * @param toPort                     Top of port range to authorize access to when operating on a CIDR IP. This contains the ICMP code if ICMP is being authorized.
     * @param cidrIp                     CIDR IP range to authorize access to when operating on a CIDR IP.
     * @return true if permissions successfully added. Otherwise, false.
     * @throws EC2Exception if any error occurs
     */
    boolean authorizeSecurityGroupIngress(String name,
                                          String sourceSecurityGroupName, String sourceSecurityGroupOwnerId,
                                          IpProtocol ipProtocol, int fromPort, int toPort, String cidrIp) throws EC2Exception;

    /**
     * Revokes permissions from a security group. The permissions used to
     * revoke must be specified using the same values used to grant the
     * permissions.
     * <p/>
     * Permissions are specified by IP protocol (TCP, UDP, or ICMP), the source
     * of the request (by IP range or an Amazon EC2 user-group pair), the
     * source and destination port ranges (for TCP and UDP), and the ICMP codes
     * and types (for ICMP).
     * <p/>
     * Permission changes are quickly propagated to instances within the
     * security group. However, depending on the number of instances in the
     * group, a small delay is might occur.
     * <p/>
     * When revoking a user/group pair permission, GroupName,
     * SourceSecurityGroupName and SourceSecurityGroupOwnerId must be
     * specified. When authorizing a CIDR IP permission, GroupName, IpProtocol,
     * FromPort, ToPort and CidrIp must be specified. Mixing these two types of
     * parameters is not allowed.
     *
     * @param name                       Name of the group to modify.
     * @param sourceSecurityGroupName    Name of security group to revoke access to when operating on a user/group pair.
     * @param sourceSecurityGroupOwnerId Owner of security group to revoke access to when operating on a user/group pair.
     * @param ipProtocol                 IP protocol to revoke access to when operating on a CIDR IP.
     * @param fromPort                   Bottom of port range to revoke access to when operating on a CIDR IP. This contains the ICMP type if ICMP is being authorized.
     * @param toPort                     Top of port range to revoke access to when operating on a CIDR IP. This contains the ICMP code if ICMP is being authorized.
     * @param cidrIp                     CIDR IP range to revoke access to when operating on a CIDR IP.
     * @return true if permissions successfully revoked.
     * @throws EC2Exception if any error occurs
     */
    boolean revokeSecurityGroupIngress(String name,
                                       String sourceSecurityGroupName, String sourceSecurityGroupOwnerId,
                                       IpProtocol ipProtocol, int fromPort, int toPort, String cidrIp) throws EC2Exception;

    /**
     * Acquires an elastic IP address for use with your account.
     *
     * @return IP address
     * @throws EC2Exception if any error occurs
     */
    String allocateAddress() throws EC2Exception;

    /**
     * Lists elastic IP addresses assigned to your account.
     *
     * @param publicIps elastic IP addresses to describe.  This array can be empty.
     * @return a mapping between instance IDs and their elastic IP addresses
     * @throws EC2Exception if any error occurs
     */
    Map<String, List<String>> describeAddresses(String[] publicIps) throws EC2Exception;

    /**
     * Releases an elastic IP address associated with your account.
     * <p/>
     * If you run this operation on an elastic IP address that is already
     * released, the address might be assigned to another account which will
     * cause Amazon EC2 to return an error.
     * <p/>
     * <b>Note:</b> Releasing an IP address automatically disassociates it
     * from any instance with which it is associated. For more information,
     * see <code>disassociateAddress()</code>.
     * <p/>
     * <b>Important:</b> After releasing an elastic IP address, it is released
     * to the IP address pool and might no longer be available to your account.
     * Make sure to update your DNS records and any servers or devices that
     * communicate with the address.
     *
     * @param publicIp IP address that you are releasing from your account.
     * @return true if the IP address is released. Otherwise, false.
     * @throws EC2Exception if any error occurs
     * @see #disassociateAddress(String)
     */
    boolean releaseAddress(String publicIp) throws EC2Exception;

    /**
     * Associates an elastic IP address with an instance.
     * <p/>
     * If the IP address is currently assigned to another instance, the IP
     * address is assigned to the new instance. This is an idempotent
     * operation. If you enter it more than once, Amazon EC2 does not return an
     * error.
     *
     * @param instanceId The instance to which the IP address is assigned
     * @param publicIp   IP address that you are assigning to the instance
     * @return true if the IP address is associated with the instance. Otherwise, false.
     * @throws EC2Exception if any error occurs
     */
    boolean associateAddress(String instanceId, String publicIp) throws EC2Exception;

    /**
     * Disassociates the specified elastic IP address from the instance to
     * which it is assigned. This is an idempotent operation. If you enter
     * it more than once, Amazon EC2 does not return an error.
     *
     * @param publicIp IP address that you are disassociating from the instance
     * @return true if the IP address is disassociated from the instance. Otherwise, false.
     * @throws EC2Exception if any error occurs
     */
    boolean disassociateAddress(String publicIp) throws EC2Exception;

    /**
     * Describes availability zones that are currently available to the account
     * and their states.
     * <p/>
     * Availability zones are not the same across accounts. The availability
     * zone us-east-1a for account A is not necessarily the same as us-east-1a
     * for account B. Zone assignments are mapped independently for each
     * account.
     *
     * @param zoneNames an array of an availability zone names which is optional
     * @return a list of <code>AvailabilityZone</code> response POJOs
     * @throws EC2Exception if any error occurs
     */
    List<AvailabilityZone> describeAvailabilityZones(String[] zoneNames) throws EC2Exception;

    /**
     * Retrieves console output for the specified instance.
     * <p/>
     * Instance console output is buffered and posted shortly after instance
     * boot, reboot, and termination. Amazon EC2 preserves the most recent
     * 64 KB output which will be available for at least one hour after the
     * most recent post.
     *
     * @param instanceId an instance ID returned from a previous call to <code>runInstances()</code>
     * @return a <code>ConsoleOutput</code> object containing the bit of console output
     * @throws EC2Exception if any error occurs
     */
    ConsoleOutput getConsoleOutput(String instanceId) throws EC2Exception;
}
