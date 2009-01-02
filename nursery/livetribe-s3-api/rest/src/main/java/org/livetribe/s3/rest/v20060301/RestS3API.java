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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.asyncweb.client.AsyncHttpClient;
import org.apache.asyncweb.client.codec.HttpRequestMessage;
import org.apache.asyncweb.client.codec.HttpResponseMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.livetribe.s3.api.S3Exception;
import org.livetribe.s3.api.v20080201.AccessDeniedException;
import org.livetribe.s3.api.v20080201.AccountProblemException;
import org.livetribe.s3.api.v20080201.AmbiguousGrantByEmailAddressException;
import org.livetribe.s3.api.v20080201.BadDigestException;
import org.livetribe.s3.api.v20080201.BucketAlreadyExistsException;
import org.livetribe.s3.api.v20080201.BucketAlreadyOwnedByYouException;
import org.livetribe.s3.api.v20080201.BucketNotEmptyException;
import org.livetribe.s3.api.v20080201.CredentialsNotSupportedException;
import org.livetribe.s3.api.v20080201.CrossLocationLoggingProhibitedException;
import org.livetribe.s3.api.v20080201.EntityTooLargeException;
import org.livetribe.s3.api.v20080201.EntityTooSmallException;
import org.livetribe.s3.api.v20080201.ExpiredTokenException;
import org.livetribe.s3.api.v20080201.IncompleteBodyException;
import org.livetribe.s3.api.v20080201.IncorrectNumberOfFilesInPostRequestException;
import org.livetribe.s3.api.v20080201.InlineDataTooLargeException;
import org.livetribe.s3.api.v20080201.InternalErrorException;
import org.livetribe.s3.api.v20080201.InvalidAccessKeyIdException;
import org.livetribe.s3.api.v20080201.InvalidAddressingHeaderException;
import org.livetribe.s3.api.v20080201.InvalidArgumentException;
import org.livetribe.s3.api.v20080201.InvalidBucketNameException;
import org.livetribe.s3.api.v20080201.InvalidDigestException;
import org.livetribe.s3.api.v20080201.InvalidLocationConstraintException;
import org.livetribe.s3.api.v20080201.InvalidPayerException;
import org.livetribe.s3.api.v20080201.InvalidPolicyDocumentException;
import org.livetribe.s3.api.v20080201.InvalidRangeException;
import org.livetribe.s3.api.v20080201.InvalidSOAPRequestException;
import org.livetribe.s3.api.v20080201.InvalidSecurityException;
import org.livetribe.s3.api.v20080201.InvalidStorageClassException;
import org.livetribe.s3.api.v20080201.InvalidTargetBucketForLoggingException;
import org.livetribe.s3.api.v20080201.InvalidTokenException;
import org.livetribe.s3.api.v20080201.InvalidURIException;
import org.livetribe.s3.api.v20080201.KeyTooLongException;
import org.livetribe.s3.api.v20080201.MalformedACLErrorException;
import org.livetribe.s3.api.v20080201.MalformedPOSTRequestException;
import org.livetribe.s3.api.v20080201.MaxMessageLengthExceededException;
import org.livetribe.s3.api.v20080201.MaxPostPreDataLengthExceededErrorException;
import org.livetribe.s3.api.v20080201.MetadataTooLargeException;
import org.livetribe.s3.api.v20080201.MethodNotAllowedException;
import org.livetribe.s3.api.v20080201.MissingAttachmentException;
import org.livetribe.s3.api.v20080201.MissingContentLengthException;
import org.livetribe.s3.api.v20080201.MissingSecurityElementException;
import org.livetribe.s3.api.v20080201.MissingSecurityHeaderException;
import org.livetribe.s3.api.v20080201.NoLoggingStatusForKeyException;
import org.livetribe.s3.api.v20080201.NoSuchBucketException;
import org.livetribe.s3.api.v20080201.NoSuchKeyException;
import org.livetribe.s3.api.v20080201.NotImplementedException;
import org.livetribe.s3.api.v20080201.NotSignedUpException;
import org.livetribe.s3.api.v20080201.OperationAbortedException;
import org.livetribe.s3.api.v20080201.PermanentRedirectException;
import org.livetribe.s3.api.v20080201.PreconditionFailedException;
import org.livetribe.s3.api.v20080201.RedirectException;
import org.livetribe.s3.api.v20080201.RequestIsNotMultiPartContentException;
import org.livetribe.s3.api.v20080201.RequestTimeTooSkewedException;
import org.livetribe.s3.api.v20080201.RequestTimeoutException;
import org.livetribe.s3.api.v20080201.RequestTorrentOfBucketErrorException;
import org.livetribe.s3.api.v20080201.S3API;
import org.livetribe.s3.api.v20080201.SignatureDoesNotMatchException;
import org.livetribe.s3.api.v20080201.SlowDownException;
import org.livetribe.s3.api.v20080201.TemporaryRedirectException;
import org.livetribe.s3.api.v20080201.TokenRefreshRequiredException;
import org.livetribe.s3.api.v20080201.TooManyBucketsException;
import org.livetribe.s3.api.v20080201.UnexpectedContentException;
import org.livetribe.s3.api.v20080201.UnresolvableGrantByEmailAddressException;
import org.livetribe.s3.api.v20080201.UserKeyMustBeSpecifiedException;
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
import org.livetribe.s3.model.Owner;
import org.livetribe.s3.model.Permission;
import org.livetribe.s3.model.S3Bucket;
import org.livetribe.s3.model.S3Object;
import org.livetribe.s3.rest.RestUtil;
import org.livetribe.s3.rest.S3Callback;
import org.livetribe.s3.util.HttpHeaders;
import org.livetribe.s3.util.HttpVerb;
import org.livetribe.s3.util.Util;


/**
 * A restful implementation of the <code>S3API</code> interface.
 *
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public final class RestS3API implements S3API
{
    public final static int DEFAULT_TIMEOUT = 60 * 1000;
    private final static String VERSION = "2006-03-01";
    private final JAXBContext context20060301;
    private final ObjectFactory objectFactory = new ObjectFactory();
    private final AsyncHttpClient ahc;
    private final URL url;
    private final String AWSAccessKeyId;
    private final String secretAccessKey;
    private volatile int timeout = DEFAULT_TIMEOUT;

    /**
     * A restful implementation of the <code>S3API</code> interface.
     *
     * @param url             the URL of the Amazon S3 restful API
     * @param AWSAccessKeyId  the Access Key ID for the request sender. This
     *                        identifies the account which will be charged for
     *                        usage of the service. The account with which the
     *                        Access Key ID is associated must be signed up for
     *                        Amazon S3, or requests will not be accepted.
     * @param secretAccessKey the Secret Access Key is used to calculate a signature the requests
     * @throws JAXBException if unable to create JAXB Contexts for org.livetribe.s3.jaxb or org.livetribe.s3.jaxb.v20060301
     */
    public RestS3API(URL url, String AWSAccessKeyId, String secretAccessKey) throws JAXBException
    {
        this(null, null, url, AWSAccessKeyId, secretAccessKey);
    }

    /**
     * A restful implementation of the <code>S3API</code> interface.
     *
     * @param executor        the ExceutorService to use to process connections.
     * @param scheduler       the scheduler to use to track timeouts
     * @param url             the URL of the Amazon S3 restful API
     * @param AWSAccessKeyId  the Access Key ID for the request sender. This
     *                        identifies the account which will be charged for
     *                        usage of the service. The account with which the
     *                        Access Key ID is associated must be signed up for
     *                        Amazon S3, or requests will not be accepted.
     * @param secretAccessKey the Secret Access Key is used to calculate a signature the requests
     * @throws JAXBException if unable to create JAXB Contexts for org.livetribe.s3.jaxb or org.livetribe.s3.jaxb.v20060301
     */
    public RestS3API(Executor executor, ScheduledExecutorService scheduler, URL url, String AWSAccessKeyId, String secretAccessKey) throws JAXBException
    {
        if (url == null) throw new IllegalArgumentException("url cannot be null");
        if (AWSAccessKeyId == null) throw new IllegalArgumentException("AWSAccessKeyId cannot be null");
        if (secretAccessKey == null) throw new IllegalArgumentException("secretAccessKey cannot be null");

        this.url = url;
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.context20060301 = JAXBContext.newInstance("org.livetribe.s3.jaxb.v20060301");

        this.ahc = new AsyncHttpClient(executor, scheduler);
        this.ahc.setTcpNoDelay(true);
    }

    /**
     * Get how long to wait for a reply in seconds
     *
     * @return how long to wait for a reply in seconds
     */
    public int getTimeout()
    {
        return timeout;
    }

    /**
     * Set how long to wait for a reply in seconds
     *
     * @param timeout how long to wait for a reply in seconds
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    /**
     * {@inheritDoc}
     */
    public Set<S3Bucket> listAllMyBuckets() throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.GET, "/"));

        org.livetribe.s3.jaxb.v20060301.ListAllMyBucketsResultType response = (org.livetribe.s3.jaxb.v20060301.ListAllMyBucketsResultType) call(headers, HttpVerb.GET, "/", null);

        Set<S3Bucket> result = new HashSet<S3Bucket>();

        for (org.livetribe.s3.jaxb.v20060301.ListAllMyBucketsEntry entry : response.getBuckets().getBucket())
        {
            S3Bucket bucket = new S3Bucket();

            bucket.setName(entry.getName());
            bucket.setCreationDate(entry.getCreationDate().toGregorianCalendar().getGregorianChange());

            result.add(bucket);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void createBucket(String bucketName) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.PUT, "/" + bucketName + "/"));

        call(headers, HttpVerb.PUT, "/" + bucketName + "/", null);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteBucket(String bucketName) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.DELETE, "/" + bucketName + "/"));

        call(headers, HttpVerb.DELETE, "/" + bucketName + "/", null);
    }

    /**
     * {@inheritDoc}
     */
    public BucketListing listBuckets(String prefix, String marker, String delimiter, long maxKeys) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public AccessControlPolicy getBucketAccessControlPolicy(String bucketName) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.GET, "/" + bucketName + "/?acl"));

        org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType acp = (org.livetribe.s3.jaxb.v20060301.AccessControlPolicyType) call(headers, HttpVerb.GET, "/" + bucketName + "/?acl", null);

        return translateFromJaxbObject(acp);
    }

    /**
     * {@inheritDoc}
     */
    public void setBucketAccessControlPolicy(String bucketName, AccessControlPolicy accessControlPolicy) throws S3Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Date", Util.rfc822Format(new Date()));
        headers.put("Authorization", "AWS " + this.AWSAccessKeyId + ":" + RestUtil.sign(headers, secretAccessKey, HttpVerb.PUT, "/" + bucketName + "/?acl"));

        call(headers, HttpVerb.PUT, "/" + bucketName + "/?acl", objectFactory.createAccessControlPolicy(translateToJaxbObject(accessControlPolicy)));
    }

    /**
     * {@inheritDoc}
     */
    public LoggingConfiguration getBucketLoggingConfiguration(String bucketName) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public void setBucketLoggingConfiguration(String bucketName, LoggingConfiguration loggingConfiguration) throws S3Exception
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public S3Object putObject(String bucketName, String key, Map<String, String> metaData, int contentLength, List<Grant> accessControlList, InputStream data) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public S3Object copyObject(String sourceBucketName, String sourceKey, String destBucketName, String destKey, MetadataDirective metadataDirective, Map<String, String> metaData, List<Grant> accessControlList, String copyIfMatch, String copyIfNoMatch, Date copyIfUnmodifiedSince, Date copyIfModifiedSince) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public S3Object getObject(String bucketName, String key, String ifMatch, String ifNoMatch, Date ifUnmodifiedSince, Date ifModifiedSince) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getObjectData(String bucketName, String key, long start, long end, String ifMatch, String ifNoMatch, Date ifUnmodifiedSince, Date ifModifiedSince, boolean returnCompleteObjectOnConditionFailure) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public void deleteObject(String bucketName, String key) throws S3Exception
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public AccessControlPolicy getObjectAccessControlPolicy(String bucketName, String key) throws S3Exception
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public void setObjectAccessControlPolicy(String bucketName, String key, AccessControlPolicy accessControlPolicy) throws S3Exception
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Send the HTTP request to Amazon S3.  Read the results using our JAXB
     * context and return the resulting JAXB Element.
     *
     * @param headers the parameters to be signed and appened to the URL parameters
     * @param verb    the HTTP verb to use when making the S3 call
     * @param path    the path to append to the URL
     * @param payload an optional JAXB object that can be serialized in the request
     * @return the resulting JAXB Element of the HTTP request to Amazon S3
     * @throws S3Exception if something goes wrong with the request or Amazon S3 returns an error
     */
    private Object call(HttpHeaders headers, HttpVerb verb, String path, JAXBElement payload) throws S3Exception
    {
        assert !headers.containsKey("Authorization");
        assert verb != null;
        assert path != null;

        try
        {
            URL target = new URL(url.getProtocol(), url.getHost(), url.getPort(), path);

            S3Callback callback = new S3Callback();

            final HttpRequestMessage request = new HttpRequestMessage(target, callback);

            for (String key : headers.keySet())
            {
                for (String value : headers.get(key)) request.setHeader(key, value);
            }
            request.setRequestMethod(verb.toString());
            request.setTimeOut(timeout);

            if (payload != null)
            {
                context20060301.createMarshaller().marshal(payload, new OutputStream()
                {
                    @Override
                    public void write(byte[] b) throws IOException
                    {
                        request.addContent(b);
                    }

                    @Override
                    public void write(byte[] b, int off, int len) throws IOException
                    {
                        byte[] buffer = new byte[len];
                        System.arraycopy(b, off, buffer, 0, len);
                        request.addContent(buffer);
                    }

                    public void write(int b) throws IOException
                    {
                        request.addContent(new byte[]{(byte) b});
                    }
                });
            }

            ahc.sendRequest(request);

            HttpResponseMessage response = callback.getMessage(timeout, TimeUnit.MILLISECONDS);

            if (response.getStatusCode() == 200)
            {
                if (response.getContentLength() == 0) return null;
                String r = new String(response.getContent());
                JAXBElement element = (JAXBElement) context20060301.createUnmarshaller().unmarshal(new ByteArrayInputStream(response.getContent()));
                return element.getValue();
            }
            else if (response.getStatusCode() == 204)
            {
                return null;
            }
            else
            {
                throw generateException(response.getContent());
            }
        }
        catch (MalformedURLException mue)
        {
            throw new S3Exception("Bad composite URL", mue);
        }
        catch (ProtocolException pe)
        {
            throw new S3Exception(pe);
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
            throw new S3Exception("Thread interrupted", ie);
        }
        catch (JAXBException jaxbe)
        {
            throw new S3Exception("Error unmarshalling XML", jaxbe);
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
                org.livetribe.s3.jaxb.v20060301.CanonicalUser user = (org.livetribe.s3.jaxb.v20060301.CanonicalUser) ge;
                grantee = new GranteeUser(user.getID(), user.getDisplayName());
            }
            else if (ge instanceof org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail)
            {
                org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail email = (org.livetribe.s3.jaxb.v20060301.AmazonCustomerByEmail) ge;
                grantee = new GranteeEmail(email.getEmailAddress());
            }
            else
            {
                org.livetribe.s3.jaxb.v20060301.Group group = (org.livetribe.s3.jaxb.v20060301.Group) ge;
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
                GranteeUser user = (GranteeUser) grantee;

                org.livetribe.s3.jaxb.v20060301.CanonicalUser u = new org.livetribe.s3.jaxb.v20060301.CanonicalUser();
                u.setID(user.getId());
                u.setDisplayName(user.getDisplayName());

                ge = u;
            }
            else if (grantee instanceof GranteeEmail)
            {
                GranteeEmail email = (GranteeEmail) grantee;

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

    private static S3Exception generateException(byte[] message) throws S3Exception
    {
        Document doc;
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            doc = docBuilder.parse(new ByteArrayInputStream(message));
        }
        catch (ParserConfigurationException e)
        {
            throw new S3Exception(e);
        }
        catch (SAXException e)
        {
            throw new S3Exception("Wrong XML file structure", e);
        }
        catch (IOException e)
        {
            throw new S3Exception("Could not read source stream ", e);
        }

        Element error = (Element) doc.getElementsByTagName("Error").item(0);
        Element code = (Element) error.getElementsByTagName("Code").item(0);
        String errorCode = code.getChildNodes().item(0).getNodeValue().trim();

        if ("AccessDenied".equals(errorCode)) return new AccessDeniedException();
        else if ("AccountProblem".equals(errorCode)) return new AccountProblemException();
        else if ("AmbiguousGrantByEmailAddress".equals(errorCode)) return new AmbiguousGrantByEmailAddressException();
        else if ("BadDigest".equals(errorCode)) return new BadDigestException();
        else if ("BucketAlreadyExists".equals(errorCode)) return new BucketAlreadyExistsException();
        else if ("BucketAlreadyOwnedByYou".equals(errorCode)) return new BucketAlreadyOwnedByYouException();
        else if ("BucketNotEmpty".equals(errorCode)) return new BucketNotEmptyException();
        else if ("CredentialsNotSupported".equals(errorCode)) return new CredentialsNotSupportedException();
        else if ("CrossLocationLoggingProhibited".equals(errorCode)) return new CrossLocationLoggingProhibitedException();
        else if ("EntityTooLarge".equals(errorCode)) return new EntityTooLargeException();
        else if ("EntityTooSmall".equals(errorCode)) return new EntityTooSmallException();
        else if ("ExpiredToken".equals(errorCode)) return new ExpiredTokenException();
        else if ("IncompleteBody".equals(errorCode)) return new IncompleteBodyException();
        else if ("IncorrectNumberOfFilesInPostRequest".equals(errorCode)) return new IncorrectNumberOfFilesInPostRequestException();
        else if ("InlineDataTooLarge".equals(errorCode)) return new InlineDataTooLargeException();
        else if ("InternalError".equals(errorCode)) return new InternalErrorException();
        else if ("InvalidAccessKeyId".equals(errorCode)) return new InvalidAccessKeyIdException();
        else if ("InvalidAddressingHeader".equals(errorCode)) return new InvalidAddressingHeaderException();
        else if ("InvalidArgument".equals(errorCode)) return new InvalidArgumentException();
        else if ("InvalidBucketName".equals(errorCode)) return new InvalidBucketNameException();
        else if ("InvalidDigest".equals(errorCode)) return new InvalidDigestException();
        else if ("InvalidLocationConstraint".equals(errorCode)) return new InvalidLocationConstraintException();
        else if ("InvalidPayer".equals(errorCode)) return new InvalidPayerException();
        else if ("InvalidPolicyDocument".equals(errorCode)) return new InvalidPolicyDocumentException();
        else if ("InvalidRange".equals(errorCode)) return new InvalidRangeException();
        else if ("InvalidSecurity".equals(errorCode)) return new InvalidSecurityException();
        else if ("InvalidSOAPRequest".equals(errorCode)) return new InvalidSOAPRequestException();
        else if ("InvalidStorageClass".equals(errorCode)) return new InvalidStorageClassException();
        else if ("InvalidTargetBucketForLogging".equals(errorCode)) return new InvalidTargetBucketForLoggingException();
        else if ("InvalidToken".equals(errorCode)) return new InvalidTokenException();
        else if ("InvalidURI".equals(errorCode)) return new InvalidURIException();
        else if ("KeyTooLong".equals(errorCode)) return new KeyTooLongException();
        else if ("MalformedACLError".equals(errorCode)) return new MalformedACLErrorException();
        else if ("MalformedPOSTRequest".equals(errorCode)) return new MalformedPOSTRequestException();
        else if ("MaxMessageLengthExceeded".equals(errorCode)) return new MaxMessageLengthExceededException();
        else if ("MaxPostPreDataLengthExceededError".equals(errorCode)) return new MaxPostPreDataLengthExceededErrorException();
        else if ("MetadataTooLarge".equals(errorCode)) return new MetadataTooLargeException();
        else if ("MethodNotAllowed".equals(errorCode)) return new MethodNotAllowedException();
        else if ("MissingAttachment".equals(errorCode)) return new MissingAttachmentException();
        else if ("MissingContentLength".equals(errorCode)) return new MissingContentLengthException();
        else if ("MissingSecurityElement".equals(errorCode)) return new MissingSecurityElementException();
        else if ("MissingSecurityHeader".equals(errorCode)) return new MissingSecurityHeaderException();
        else if ("NoLoggingStatusForKey".equals(errorCode)) return new NoLoggingStatusForKeyException();
        else if ("NoSuchBucket".equals(errorCode)) return new NoSuchBucketException();
        else if ("NoSuchKey".equals(errorCode)) return new NoSuchKeyException();
        else if ("NotImplemented".equals(errorCode)) return new NotImplementedException();
        else if ("NotSignedUp".equals(errorCode)) return new NotSignedUpException();
        else if ("OperationAborted".equals(errorCode)) return new OperationAbortedException();
        else if ("PermanentRedirect".equals(errorCode)) return new PermanentRedirectException();
        else if ("PreconditionFailed".equals(errorCode)) return new PreconditionFailedException();
        else if ("Redirect".equals(errorCode)) return new RedirectException();
        else if ("RequestIsNotMultiPartContent".equals(errorCode)) return new RequestIsNotMultiPartContentException();
        else if ("RequestTimeout".equals(errorCode)) return new RequestTimeoutException();
        else if ("RequestTimeTooSkewed".equals(errorCode)) return new RequestTimeTooSkewedException();
        else if ("RequestTorrentOfBucketError".equals(errorCode)) return new RequestTorrentOfBucketErrorException();
        else if ("SignatureDoesNotMatch".equals(errorCode)) return new SignatureDoesNotMatchException();
        else if ("SlowDown".equals(errorCode)) return new SlowDownException();
        else if ("TemporaryRedirect".equals(errorCode)) return new TemporaryRedirectException();
        else if ("TokenRefreshRequired".equals(errorCode)) return new TokenRefreshRequiredException();
        else if ("TooManyBuckets".equals(errorCode)) return new TooManyBucketsException();
        else if ("UnexpectedContent".equals(errorCode)) return new UnexpectedContentException();
        else if ("UnresolvableGrantByEmailAddress".equals(errorCode)) return new UnresolvableGrantByEmailAddressException();
        else if ("UserKeyMustBeSpecified".equals(errorCode)) return new UserKeyMustBeSpecifiedException();

        else return new InternalErrorException();
    }
}
