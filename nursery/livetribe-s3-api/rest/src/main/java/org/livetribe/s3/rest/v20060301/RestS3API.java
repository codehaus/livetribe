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
package org.livetribe.s3.rest.v20060301;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.ahc.HttpHeaders;
import org.apache.ahc.api.HttpClient;
import org.apache.ahc.api.HttpClientFuture;
import org.apache.ahc.api.HttpRequest;
import org.apache.ahc.api.HttpResponse;
import org.apache.ahc.api.HttpVerb;
import org.apache.ahc.api.MimeContent;
import org.apache.ahc.client.AsyncHttpConfig;
import org.apache.ahc.client.ByteArrayBasedMimeContent;

import org.livetribe.s3.api.FutureResult;
import org.livetribe.s3.api.S3Exception;
import org.livetribe.s3.api.v20080201.S3API;
import org.livetribe.s3.jaxb.v20060301.ObjectFactory;
import org.livetribe.s3.model.AccessControlPolicy;
import org.livetribe.s3.model.BucketListing;
import org.livetribe.s3.model.Grant;
import org.livetribe.s3.model.Grantee;
import org.livetribe.s3.model.GranteeAWSUserGroup;
import org.livetribe.s3.model.GranteeAllUsersGroup;
import org.livetribe.s3.model.GranteeEmail;
import org.livetribe.s3.model.GranteeLogDeliveryGroup;
import org.livetribe.s3.model.GranteeUser;
import org.livetribe.s3.model.LoggingConfiguration;
import org.livetribe.s3.model.MetadataDirective;
import org.livetribe.s3.model.ObjectInfo;
import org.livetribe.s3.model.Owner;
import org.livetribe.s3.model.Permission;
import org.livetribe.s3.model.S3Bucket;
import org.livetribe.s3.model.S3Object;
import org.livetribe.s3.model.StorageClass;
import org.livetribe.s3.rest.FutureResultAdapter;
import org.livetribe.s3.rest.RestUtil;
import org.livetribe.s3.util.Util;


/**
 * A restful implementation of the <code>S3API</code> interface.
 *
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public final class RestS3API implements S3API
{
    public final static int DEFAULT_TIMEOUT = 60 * 1000;
    private final JAXBContext context20060301;
    private final ObjectFactory objectFactory = new ObjectFactory();
    private final HttpClient ahc;
    private final URL url;
    private final String AWSAccessKeyId;
    private final String secretAccessKey;
    private volatile int timeout = DEFAULT_TIMEOUT;

    /**
     * A restful implementation of the <code>S3API</code> interface.
     *
     * @param ahc             the instance of {@link HttpClient} that will be
     *                        used to communicate with the Amazon S3 restful
     *                        API.
     * @param url             the URL of the Amazon S3 restful API
     * @param AWSAccessKeyId  the Access Key ID for the request sender. This
     *                        identifies the account which will be charged for
     *                        usage of the service. The account with which the
     *                        Access Key ID is associated must be signed up for
     *                        Amazon S3, or requests will not be accepted.
     * @param secretAccessKey the Secret Access Key is used to calculate a signature the requests
     * @throws JAXBException if unable to create JAXB Contexts for org.livetribe.s3.jaxb or org.livetribe.s3.jaxb.v20060301
     */
    public RestS3API(HttpClient ahc, URL url, String AWSAccessKeyId, String secretAccessKey) throws JAXBException
    {
        if (ahc == null) throw new IllegalArgumentException("HttpClient cannot be null");
        if (url == null) throw new IllegalArgumentException("url cannot be null");
        if (AWSAccessKeyId == null) throw new IllegalArgumentException("AWSAccessKeyId cannot be null");
        if (secretAccessKey == null) throw new IllegalArgumentException("secretAccessKey cannot be null");

        this.url = url;
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.context20060301 = JAXBContext.newInstance("org.livetribe.s3.jaxb.v20060301");

        this.ahc = ahc;

        AsyncHttpConfig config = new AsyncHttpConfig();
        config.setTcpNoDelay(true);

        this.ahc.addConfig(config);
    }

    /**
     * Get how long to wait for a reply in milliseconds
     *
     * @return how long to wait for a reply in milliseconds
     */
    public int getTimeout()
    {
        return timeout;
    }

    /**
     * Set how long to wait for a reply in milliseconds
     *
     * @param timeout how long to wait for a reply in milliseconds
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Set<S3Bucket>> listAllMyBuckets() throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.GET, "/"));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.GET, "/");

        return new FutureResultAdapter<Set<S3Bucket>>(response)
        {
            @Override
            public Set<S3Bucket> convert(HttpResponse response) throws Throwable
            {
                Set<S3Bucket> result = new HashSet<S3Bucket>();

                InputStream inputStream = response.getInputStream();
                JAXBElement element = (JAXBElement)context20060301.createUnmarshaller().unmarshal(inputStream);

                org.livetribe.s3.jaxb.v20060301.ListAllMyBucketsResultType r = (org.livetribe.s3.jaxb.v20060301.ListAllMyBucketsResultType)element.getValue();

                for (org.livetribe.s3.jaxb.v20060301.ListAllMyBucketsEntry entry : r.getBuckets().getBucket())
                {
                    S3Bucket bucket = new S3Bucket();

                    bucket.setName(entry.getName());
                    bucket.setCreationDate(entry.getCreationDate().toGregorianCalendar().getGregorianChange());

                    result.add(bucket);
                }

                return result;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Void> createBucket(String bucketName) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.PUT, "/" + bucketName + "/"));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.PUT, "/" + bucketName + "/");

        return new FutureResultAdapter<Void>(response);
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Void> deleteBucket(String bucketName) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.DELETE, "/" + bucketName + "/"));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.DELETE, "/" + bucketName + "/");

        return new FutureResultAdapter<Void>(response);
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<BucketListing> listBuckets(String prefix, String marker, String delimiter, long maxKeys) throws S3Exception
    {
        StringBuilder builder = new StringBuilder("?");

        if (prefix != null) builder.append("prefix=").append(prefix);
        if (marker != null) builder.append("marker=").append(prefix);
        if (delimiter != null) builder.append("delimiter=").append(prefix);
        if (maxKeys > 0) builder.append("max-keys=").append(maxKeys);

        String path = builder.toString();

        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.GET, path));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.GET, path);

        return new FutureResultAdapter<BucketListing>(response)
        {
            @Override
            public BucketListing convert(HttpResponse response) throws Throwable
            {
                BucketListing result = new BucketListing();

                InputStream inputStream = response.getInputStream();
                JAXBElement element = (JAXBElement)context20060301.createUnmarshaller().unmarshal(inputStream);

                org.livetribe.s3.jaxb.v20060301.ListBucketResult r = (org.livetribe.s3.jaxb.v20060301.ListBucketResult)element.getValue();

                List<String> commonPrefixes = new ArrayList<String>();
                for (org.livetribe.s3.jaxb.v20060301.PrefixEntry prefixEntry : r.getCommonPrefixes())
                {
                    commonPrefixes.add(prefixEntry.getPrefix());
                }
                result.setCommonPrefixes(commonPrefixes);

                List<ObjectInfo> objectInfoList = new ArrayList<ObjectInfo>();
                for (org.livetribe.s3.jaxb.v20060301.ListEntry entry : r.getContents())
                {
                    ObjectInfo objectInfo = new ObjectInfo();

                    objectInfo.setETag(entry.getETag());
                    objectInfo.setKey(entry.getKey());
                    objectInfo.setLastModified(entry.getLastModified().toGregorianCalendar().getTime());
                    objectInfo.setOwner(new Owner(entry.getOwner().getID(), entry.getOwner().getDisplayName()));
                    objectInfo.setSize(entry.getSize());
                    objectInfo.setStorageClass(StorageClass.valueOf(entry.getStorageClass().value()));

                    objectInfoList.add(objectInfo);
                }
                result.setObjects(objectInfoList);

                return result;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<AccessControlPolicy> getBucketAccessControlPolicy(String bucketName) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.GET, "/" + bucketName + "/?acl"));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.GET, "/" + bucketName + "/?acl");

        return new FutureResultAdapter<AccessControlPolicy>(response)
        {
            @Override
            public AccessControlPolicy convert(HttpResponse response) throws Throwable
            {
                InputStream inputStream = response.getInputStream();
                JAXBElement element = (JAXBElement)context20060301.createUnmarshaller().unmarshal(inputStream);

                org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType acp = (org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType)element.getValue();

                return translateFromJaxbObject(acp);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Void> setBucketAccessControlPolicy(String bucketName, AccessControlPolicy accessControlPolicy) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.PUT, "/" + bucketName + "/?acl"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            context20060301.createMarshaller().marshal(objectFactory.createAccessControlPolicy(translateToJaxbObject(accessControlPolicy)), out);
        }
        catch (JAXBException je)
        {
            throw new S3Exception(je);
        }

        HttpClientFuture<HttpResponse> response = call(headers,
                                                       HttpVerb.PUT,
                                                       "/" + bucketName + "/?acl",
                                                       new ByteArrayBasedMimeContent("AccessControlPolicy",
                                                                                     null,
                                                                                     "text/xml",
                                                                                     out.toByteArray()));

        return new FutureResultAdapter<Void>(response);
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<LoggingConfiguration> getBucketLoggingConfiguration(String bucketName) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Void> setBucketLoggingConfiguration(String bucketName, LoggingConfiguration loggingConfiguration) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<S3Object> putObject(String bucketName, String key, Map<String, String> metaData, int contentLength, List<Grant> accessControlList, InputStream data) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<S3Object> copyObject(String sourceBucketName, String sourceKey, String destBucketName, String destKey, MetadataDirective metadataDirective, Map<String, String> metaData, List<Grant> accessControlList, String copyIfMatch, String copyIfNoMatch, Date copyIfUnmodifiedSince, Date copyIfModifiedSince) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<S3Object> getObject(String bucketName, String key, String ifMatch, String ifNoMatch, Date ifUnmodifiedSince, Date ifModifiedSince) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<InputStream> getObjectData(String bucketName, String key, long start, long end, String ifMatch, String ifNoMatch, Date ifUnmodifiedSince, Date ifModifiedSince, boolean returnCompleteObjectOnConditionFailure) throws S3Exception
    {
        if (bucketName == null) throw new IllegalArgumentException("Bucket name cannot be null");
        if (key == null || key.trim().length() == 0) throw new IllegalArgumentException("Key cannot be null");

        HttpHeaders headers = new HttpHeaders();

        headers.put("Host", bucketName + ".s3.amazonaws.com");
        headers.put("Date", Util.rfc822Format(new Date()));

        if ((start > 0 || end > 0) && start < end) headers.put("Range", start + "-" + end);
        if (ifMatch != null) headers.put("If-Match", "\"" + ifMatch + "\"");
        if (ifNoMatch != null) headers.put("If-None-Match", "\"" + ifNoMatch + "\"");
        if (ifUnmodifiedSince != null) headers.put("If-Unmodified-Since", Util.rfc822Format(ifUnmodifiedSince));
        if (ifModifiedSince != null) headers.put("If-Modified-Since", Util.rfc822Format(ifModifiedSince));

        headers.put("Authorization", "AWS " + AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.GET, key));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.GET, key);

        return new FutureResultAdapter<InputStream>(response)
        {
            @Override
            public InputStream convert(HttpResponse response) throws Throwable
            {
                return response.getInputStream();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Void> deleteObject(String bucketName, String key) throws S3Exception
    {
        if (bucketName == null) throw new IllegalArgumentException("Bucket name cannot be null");
        if (key == null || key.trim().length() == 0) throw new IllegalArgumentException("Key cannot be null");

        HttpHeaders headers = new HttpHeaders();

        headers.put("Host", bucketName + ".s3.amazonaws.com");
        headers.put("Date", Util.rfc822Format(new Date()));

        headers.put("Authorization", "AWS " + AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.DELETE, key));

        HttpClientFuture<HttpResponse> response = call(headers, HttpVerb.DELETE, key);

        return new FutureResultAdapter<Void>(response);
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<AccessControlPolicy> getObjectAccessControlPolicy(String bucketName, String key) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public FutureResult<Void> setObjectAccessControlPolicy(String bucketName, String key, AccessControlPolicy accessControlPolicy) throws S3Exception
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Send the HTTP request to Amazon S3.  Read the results using our JAXB
     * context and return the resulting JAXB Element.
     *
     * @param headers the parameters to be signed and appened to the URL parameters
     * @param verb    the HTTP verb to use when making the S3 call
     * @param path    the path to append to the URL
     * @param content MimeContent that will get sent to the S3 server, e.g. XML or files
     * @return the resulting JAXB Element of the HTTP request to Amazon S3
     * @throws S3Exception if something goes wrong with the request or Amazon S3 returns an error
     */
    private HttpClientFuture<HttpResponse> call(HttpHeaders headers, HttpVerb verb, String path, MimeContent... content) throws S3Exception
    {
        assert !headers.containsKey("Authorization");
        assert verb != null;
        assert path != null;

        try
        {
            HttpRequest request = new HttpRequest(new URL(url.getProtocol(), url.getHost(), url.getPort(), path));

            request.setHeaders(headers);
            request.setVerb(verb);
            request.setTimeOut(timeout, TimeUnit.MILLISECONDS);
            request.addMimeContent(content);

            return ahc.send(request);
        }
        catch (MalformedURLException mue)
        {
            throw new S3Exception("Bad composite URL", mue);
        }
    }

    private static AccessControlPolicy translateFromJaxbObject(org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType acp) throws S3Exception
    {
        AccessControlPolicy result = new AccessControlPolicy();

        result.setOwner(new Owner(acp.getOwner().getID(), acp.getOwner().getDisplayName()));

        for (org.livetribe.s3.jaxb.v20060301.Grant g : acp.getAccessControlList().getGrant())
        {
            org.livetribe.s3.jaxb.v20060301.Grantee ge = g.getGrantee();

            Grantee grantee;
            if (ge instanceof org.livetribe.s3.jaxb.v20060301.CanonicalUser)
            {
                org.livetribe.s3.jaxb.v20060301.CanonicalUser user = (org.livetribe.s3.jaxb.v20060301.CanonicalUser)ge;
                grantee = new GranteeUser(user.getID(), user.getDisplayName());
            }
            else if (ge instanceof org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail)
            {
                org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail email = (org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail)ge;
                grantee = new GranteeEmail(email.getEmailAddress());
            }
            else
            {
                org.livetribe.s3.jaxb.v20060301.Group group = (org.livetribe.s3.jaxb.v20060301.Group)ge;
                if ("http://acs.amazonaws.com/groups/s3/LogDelivery".equals(group.getURI()))
                {
                    grantee = new GranteeLogDeliveryGroup();
                }
                else if ("http://acs.amazonaws.com/groups/global/AuthenticatedUsers".equals(group.getURI()))
                {
                    grantee = new GranteeAWSUserGroup();
                }
                else if ("http://acs.amazonaws.com/groups/global/AllUsers".equals(group.getURI()))
                {
                    grantee = new GranteeAllUsersGroup();
                }
                else
                {
                    throw new S3Exception("Unrecognized group URI: " + group.getURI());
                }
            }

            result.getAccessControlList().add(new Grant(grantee, Permission.valueOf(g.getPermission().value())));
        }

        return result;
    }

    private static org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType translateToJaxbObject(AccessControlPolicy accessControlPolicy)
    {
        org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType acp = new org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType();

        org.livetribe.s3.jaxb.v20060301.CanonicalUser o = new org.livetribe.s3.jaxb.v20060301.CanonicalUser();
        o.setID(accessControlPolicy.getOwner().getId());
        o.setDisplayName(accessControlPolicy.getOwner().getDisplayName());

        acp.setOwner(o);

        org.livetribe.s3.jaxb.v20060301.AccessControlListType acl = new org.livetribe.s3.jaxb.v20060301.AccessControlListType();

        acp.setAccessControlList(acl);

        for (Grant grant : accessControlPolicy.getAccessControlList())
        {
            org.livetribe.s3.jaxb.v20060301.Grant g = new org.livetribe.s3.jaxb.v20060301.Grant();

            org.livetribe.s3.jaxb.v20060301.Grantee ge;
            Grantee grantee = grant.getGrantee();
            if (grantee instanceof GranteeUser)
            {
                GranteeUser user = (GranteeUser)grantee;

                org.livetribe.s3.jaxb.v20060301.CanonicalUser u = new org.livetribe.s3.jaxb.v20060301.CanonicalUser();
                u.setID(user.getId());
                u.setDisplayName(user.getDisplayName());

                ge = u;
            }
            else if (grantee instanceof GranteeEmail)
            {
                GranteeEmail email = (GranteeEmail)grantee;

                org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail e = new org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail();
                e.setEmailAddress(email.getEmail());

                ge = e;
            }
            else if (grantee instanceof GranteeAWSUserGroup)
            {
                org.livetribe.s3.jaxb.v20060301.Group gp = new org.livetribe.s3.jaxb.v20060301.Group();
                gp.setURI("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");

                ge = gp;
            }
            else if (grantee instanceof GranteeAllUsersGroup)
            {
                org.livetribe.s3.jaxb.v20060301.Group gp = new org.livetribe.s3.jaxb.v20060301.Group();
                gp.setURI("http://acs.amazonaws.com/groups/global/AllUsers");

                ge = gp;
            }
            else
            {
                org.livetribe.s3.jaxb.v20060301.Group gp = new org.livetribe.s3.jaxb.v20060301.Group();
                gp.setURI("http://acs.amazonaws.com/groups/s3/LogDelivery");

                ge = gp;
            }

            g.setGrantee(ge);
            g.setPermission(org.livetribe.s3.jaxb.v20060301.Permission.fromValue(grant.getPermission().toString()));

            acl.getGrant().add(g);
        }

        return acp;
    }
}
