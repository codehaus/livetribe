/**
 *
 * Copyright 2009-2011 (C) The original author or authors
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
package org.livetribe.s3.rest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.ahc.api.FutureListener;
import org.apache.ahc.api.HttpClientFuture;
import org.apache.ahc.api.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.livetribe.s3.api.FutureResult;
import org.livetribe.s3.api.S3Exception;
import org.livetribe.s3.api.v20060301.AccessDeniedException;
import org.livetribe.s3.api.v20060301.AccountProblemException;
import org.livetribe.s3.api.v20060301.AmbiguousGrantByEmailAddressException;
import org.livetribe.s3.api.v20060301.BadDigestException;
import org.livetribe.s3.api.v20060301.BucketAlreadyExistsException;
import org.livetribe.s3.api.v20060301.BucketAlreadyOwnedByYouException;
import org.livetribe.s3.api.v20060301.BucketNotEmptyException;
import org.livetribe.s3.api.v20060301.CredentialsNotSupportedException;
import org.livetribe.s3.api.v20060301.CrossLocationLoggingProhibitedException;
import org.livetribe.s3.api.v20060301.EntityTooLargeException;
import org.livetribe.s3.api.v20060301.EntityTooSmallException;
import org.livetribe.s3.api.v20060301.ExpiredTokenException;
import org.livetribe.s3.api.v20060301.IncompleteBodyException;
import org.livetribe.s3.api.v20060301.IncorrectNumberOfFilesInPostRequestException;
import org.livetribe.s3.api.v20060301.InlineDataTooLargeException;
import org.livetribe.s3.api.v20060301.InternalErrorException;
import org.livetribe.s3.api.v20060301.InvalidAccessKeyIdException;
import org.livetribe.s3.api.v20060301.InvalidAddressingHeaderException;
import org.livetribe.s3.api.v20060301.InvalidArgumentException;
import org.livetribe.s3.api.v20060301.InvalidBucketNameException;
import org.livetribe.s3.api.v20060301.InvalidDigestException;
import org.livetribe.s3.api.v20060301.InvalidLocationConstraintException;
import org.livetribe.s3.api.v20060301.InvalidPayerException;
import org.livetribe.s3.api.v20060301.InvalidPolicyDocumentException;
import org.livetribe.s3.api.v20060301.InvalidRangeException;
import org.livetribe.s3.api.v20060301.InvalidSOAPRequestException;
import org.livetribe.s3.api.v20060301.InvalidSecurityException;
import org.livetribe.s3.api.v20060301.InvalidStorageClassException;
import org.livetribe.s3.api.v20060301.InvalidTargetBucketForLoggingException;
import org.livetribe.s3.api.v20060301.InvalidTokenException;
import org.livetribe.s3.api.v20060301.InvalidURIException;
import org.livetribe.s3.api.v20060301.KeyTooLongException;
import org.livetribe.s3.api.v20060301.MalformedACLErrorException;
import org.livetribe.s3.api.v20060301.MalformedPOSTRequestException;
import org.livetribe.s3.api.v20060301.MaxMessageLengthExceededException;
import org.livetribe.s3.api.v20060301.MaxPostPreDataLengthExceededErrorException;
import org.livetribe.s3.api.v20060301.MetadataTooLargeException;
import org.livetribe.s3.api.v20060301.MethodNotAllowedException;
import org.livetribe.s3.api.v20060301.MissingAttachmentException;
import org.livetribe.s3.api.v20060301.MissingContentLengthException;
import org.livetribe.s3.api.v20060301.MissingSecurityElementException;
import org.livetribe.s3.api.v20060301.MissingSecurityHeaderException;
import org.livetribe.s3.api.v20060301.NoLoggingStatusForKeyException;
import org.livetribe.s3.api.v20060301.NoSuchBucketException;
import org.livetribe.s3.api.v20060301.NoSuchKeyException;
import org.livetribe.s3.api.v20060301.NotImplementedException;
import org.livetribe.s3.api.v20060301.NotSignedUpException;
import org.livetribe.s3.api.v20060301.OperationAbortedException;
import org.livetribe.s3.api.v20060301.PermanentRedirectException;
import org.livetribe.s3.api.v20060301.PreconditionFailedException;
import org.livetribe.s3.api.v20060301.RedirectException;
import org.livetribe.s3.api.v20060301.RequestIsNotMultiPartContentException;
import org.livetribe.s3.api.v20060301.RequestTimeTooSkewedException;
import org.livetribe.s3.api.v20060301.RequestTimeoutException;
import org.livetribe.s3.api.v20060301.RequestTorrentOfBucketErrorException;
import org.livetribe.s3.api.v20060301.SignatureDoesNotMatchException;
import org.livetribe.s3.api.v20060301.SlowDownException;
import org.livetribe.s3.api.v20060301.TemporaryRedirectException;
import org.livetribe.s3.api.v20060301.TokenRefreshRequiredException;
import org.livetribe.s3.api.v20060301.TooManyBucketsException;
import org.livetribe.s3.api.v20060301.UnexpectedContentException;
import org.livetribe.s3.api.v20060301.UnresolvableGrantByEmailAddressException;
import org.livetribe.s3.api.v20060301.UserKeyMustBeSpecifiedException;


/**
 * @version $Revision$ $Date$
 */
public class FutureResultAdapter<V> implements org.livetribe.s3.api.FutureResult<V>
{
    private final CountDownLatch latch = new CountDownLatch(1);
    private final List<org.livetribe.s3.api.FutureListener<V>> listeners = new ArrayList<org.livetribe.s3.api.FutureListener<V>>();
    private final AtomicReference<Object> result = new AtomicReference<Object>();
    private volatile boolean canceled;
    private final HttpClientFuture<HttpResponse> futureResult;

    public FutureResultAdapter(HttpClientFuture<HttpResponse> futureResult)
    {
        this.futureResult = futureResult;

        futureResult.register(new FutureListener<HttpResponse>()
        {
            public void exception(Throwable t)
            {
                assert !isDone();

                synchronized (latch)
                {
                    result.set(t);
                    latch.countDown();
                }

                for (org.livetribe.s3.api.FutureListener<V> listener : listeners)
                {
                    listener.exception(t);
                }

                listeners.clear();
            }

            public void completed(HttpResponse response)
            {
                assert !isDone();

                try
                {
                    V v = preconvert(response);

                    synchronized (latch)
                    {
                        result.set(v);
                        latch.countDown();
                    }

                    for (org.livetribe.s3.api.FutureListener<V> listener : listeners)
                    {
                        try { listener.completed(v); } catch (Throwable ignore) { }
                    }
                }
                catch (Throwable throwable)
                {
                    synchronized (latch)
                    {
                        result.set(throwable);
                        latch.countDown();
                    }

                    for (org.livetribe.s3.api.FutureListener<V> listener : listeners)
                    {
                        try { listener.exception(throwable); } catch (Throwable ignore) { }
                    }
                }

                listeners.clear();
            }
        });
    }

    public V preconvert(HttpResponse response) throws Throwable
    {
        if (response.getStatusCode() == 200)
            return convert(response);
        else if (response.getStatusCode() == 204)
            return null;
        else
            throw generateException(response.getInputStream());
    }

    public V convert(HttpResponse response) throws Throwable
    {
        return null;
    }

    public boolean cancel(boolean mayInterruptIfRunning)
    {
        synchronized (latch)
        {
            boolean c = !canceled && !isDone() && futureResult.cancel(mayInterruptIfRunning);

            if (c) canceled = true;

            return canceled;
        }
    }

    public boolean isCancelled()
    {
        return canceled;
    }

    public boolean isDone()
    {
        return latch.getCount() == 0;
    }

    @SuppressWarnings({"unchecked"})
    public V get() throws InterruptedException, ExecutionException
    {
        latch.await();

        Object object = result.get();
        if (object instanceof Throwable)
        {
            throw new ExecutionException((Throwable)object);
        }
        else
        {
            return (V)object;
        }
    }

    @SuppressWarnings({"unchecked"})
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        if (!latch.await(timeout, unit)) throw new TimeoutException();

        Object object = result.get();
        if (object instanceof Throwable)
        {
            throw new ExecutionException((Throwable)object);
        }
        else
        {
            return (V)object;
        }
    }

    @SuppressWarnings({"unchecked"})
    public FutureResult<V> register(org.livetribe.s3.api.FutureListener<V> listener)
    {
        synchronized (latch)
        {
            if (!isDone())
            {
                listeners.add(listener);
                listener = null;
            }
        }

        if (listener != null)
        {
            Object object = result.get();
            if (object instanceof Throwable)
            {
                listener.exception((Throwable)object);
            }
            else
            {
                listener.completed((V)object);
            }
        }

        return this;
    }

    private static S3Exception generateException(InputStream inputStream) throws S3Exception
    {
        Document doc;
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            doc = docBuilder.parse(inputStream);
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

        Element error = (Element)doc.getElementsByTagName("Error").item(0);
        Element code = (Element)error.getElementsByTagName("Code").item(0);
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
