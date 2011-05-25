/**
 *
 * Copyright 2008-2011(C) The original author or authors
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
package org.livetribe.s3.api.v20060301;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.livetribe.s3.api.FutureResult;
import org.livetribe.s3.api.S3Exception;
import org.livetribe.s3.model.AccessControlPolicy;
import org.livetribe.s3.model.BucketListing;
import org.livetribe.s3.model.Grant;
import org.livetribe.s3.model.LoggingConfiguration;
import org.livetribe.s3.model.MetadataDirective;
import org.livetribe.s3.model.S3Bucket;
import org.livetribe.s3.model.S3Object;

/**
 * @version $Revision$ $Date$
 */
public interface S3API
{
    /**
     * Returns a list of all buckets owned by the sender of the request.
     * <p/>
     * You must authenticate with a valid AWS Access Key ID. Anonymous requests
     * are never allowed to list buckets, and you can only list buckets for
     * which you are the owner.
     *
     * @return a Set of all of the buckets owned by the authenticated sender of the request
     * @throws S3Exception if any error occurs
     */
    FutureResult<Set<S3Bucket>> listAllMyBuckets() throws S3Exception;

    /**
     * Creates a bucket. Not every string is an acceptable bucket name. For
     * information on bucket naming restrictions, see
     * http://docs.amazonwebservices.com/AmazonS3/2006-03-01/UsingBucket.html.
     * <p/>
     * You must authenticate with a valid AWS Access Key ID. Anonymous requests
     * are never allowed to create buckets.
     *
     * @param bucketName The name of the bucket you are trying to create
     * @return a {@link FutureResult} that facilitates waiting synchronously or asynchronously for completion
     * @throws S3Exception if any error occurs
     */
    FutureResult<Void> createBucket(String bucketName) throws S3Exception;

    /**
     * Deletes a bucket. All objects in the bucket must be deleted before the
     * bucket itself can be deleted.
     * <p/>
     * Only the owner of a bucket is allowed to delete it, regardless the
     * access control policy on the bucket.
     *
     * @param bucketName The name of the bucket you are trying to delete
     * @return a {@link FutureResult} that facilitates waiting synchronously or asynchronously for completion
     * @throws S3Exception if any error occurs
     */
    FutureResult<Void> deleteBucket(String bucketName) throws S3Exception;

    /**
     * Returns information about some of the items in the bucket
     *
     * @param prefix    Restricts the response to only contain results that
     *                  begin with the specified prefix. If you omit this
     *                  optional argument by passing null, the value of Prefix
     *                  for your query will be the empty string. In other
     *                  words, the results will be not be restricted by prefix.
     * @param marker    This optional parameter enables pagination of large
     *                  result sets. Marker specifies where in the result set
     *                  to resume listing. It restricts the response to only
     *                  contain results that occur alphabetically after the
     *                  value of marker. To retrieve the next page of results,
     *                  use the last key from the current page of results as
     *                  the marker in your next request. For more information,
     *                  see the NextMarker response element. If Marker is
     *                  omitted by passing null, the first page of results is
     *                  returned.
     * @param delimiter If this optional, Unicode string parameter is included
     *                  with your request, then keys that contain the same
     *                  string between the prefix and the first occurrence of
     *                  the delimiter will be rolled up into a single result
     *                  element in the CommonPrefixes collection. These
     *                  rolled-up keys are not returned elsewhere in the
     *                  response.  If the Delimiter parameter is not present in
     *                  your request by passing null, keys in the result set
     *                  will not be rolled-up and neither the CommonPrefixes
     *                  collection nor the NextMarker element will be present
     *                  in the response.
     * @param maxKeys   This optional argument limits the number of results
     *                  returned in response to your query. Amazon S3 will
     *                  return no more than this number of results, but
     *                  possibly less. Even if MaxKeys is not specified by
     *                  passing null, Amazon S3 will limit the number of
     *                  results in the response. Check the IsTruncated flag to
     *                  see if your results are incomplete. If so, use the
     *                  Marker parameter to request the next page of results.
     *                  <p/>
     *                  For the purpose of counting MaxKeys, a 'result' is
     *                  either a key in the 'Contents' collection, or a
     *                  delimited prefix in the 'CommonPrefixes' collection. So
     *                  for delimiter requests, MaxKeys limits the total number
     *                  of list results, not just the number of keys.
     * @return a information about objects that matched the criteria passed.
     * @throws S3Exception if any error occurs
     */
    FutureResult<BucketListing> listBuckets(String prefix, String marker, String delimiter, long maxKeys) throws S3Exception;

    /**
     * Fetches the access control policy for a bucket.
     * <p/>
     * You must have <code>READ_ACP</code> rights to the bucket in order to
     * retrieve the access control policy for a bucket.
     *
     * @param bucketName The name of the bucket whose access control policy you
     *                   are trying to obtain
     * @return the access control policy of the bucket
     * @throws S3Exception if any error occurs
     */
    FutureResult<AccessControlPolicy> getBucketAccessControlPolicy(String bucketName) throws S3Exception;

    /**
     * Sets the Access Control Policy for an existing bucket. If successful,
     * the previous Access Control Policy for the bucket is entirely replaced
     * with the specified Access Control Policy.
     * <p/>
     * You must have <code>WRITE_ACP</code> rights to the bucket in order to
     * set the access control policy for a bucket.
     *
     * @param bucketName          The name of the bucket whose access control
     *                            policy you are trying to set
     * @param accessControlPolicy the access control policy
     * @return a {@link FutureResult} that facilitates waiting synchronously or asynchronously for completion
     * @throws S3Exception if any error occurs
     */
    FutureResult<Void> setBucketAccessControlPolicy(String bucketName, AccessControlPolicy accessControlPolicy) throws S3Exception;

    /**
     * Retrieves the logging status for an existing bucket.
     * <p/>
     * Only the owner of a bucket is permitted to invoke this operation.
     *
     * @param bucketName The name of the bucket whose logging status you are trying
     *                   to obtain
     * @return the logging configuration for the specified bucket or null if
     *         there is none
     * @throws S3Exception if any error occurs
     */
    FutureResult<LoggingConfiguration> getBucketLoggingConfiguration(String bucketName) throws S3Exception;

    /**
     * Updates the logging status for an existing bucket.
     * <p/>
     * Only the owner of a bucket is permitted to invoke this operation.
     *
     * @param bucketName           The name of the bucket whose logging status
     *                             you are trying to set
     * @param loggingConfiguration the logging configuration
     * @return a {@link FutureResult} that facilitates waiting synchronously or asynchronously for completion
     * @throws S3Exception if any error occurs
     */
    FutureResult<Void> setBucketLoggingConfiguration(String bucketName, LoggingConfiguration loggingConfiguration) throws S3Exception;

    /**
     * Adds an object to a bucket.
     * <p/>
     * To ensure an object is not corrupted over the network, you can calculate
     * the MD5 of an object, PUT it to Amazon S3, and compare the returned Etag
     * to the calculated MD5 value.
     * <p/>
     * If an object already exists in a bucket, the new object will overwrite it
     * because Amazon S3 stores the last write request. However, Amazon S3 is a
     * distributed system. If Amazon S3 receives multiple write requests for
     * the same object nearly simultaneously, all of the objects might be
     * stored, even though only one wins in the end. Amazon S3 does not provide
     * object locking; if you need this, make sure to build it into your
     * application layer.
     * <p/>
     * To put objects into a bucket, you must have <code>WRITE</code> access to
     * the bucket.
     *
     * @param bucketName        The bucket in which to add the object.
     * @param key               The key to assign to the object.
     * @param metaData          You can provide name-value metadata pairs in
     *                          the metadata element. These will be stored with
     *                          the object.
     * @param contentLength     The length of the data in bytes.
     * @param accessControlList An Access Control List for the resource. This
     *                          element is optional. If omitted, the requestor
     *                          is given FULL_CONTROL access to the object. If
     *                          the object already exists, the pre-existing
     *                          Access Control Policy is replaced.
     * @param data              the data of the object
     * @return a populated S3Object that represents the created object
     * @throws S3Exception if any error occurs
     */
    FutureResult<S3Object> putObject(String bucketName, String key,
                                     Map<String, String> metaData,
                                     int contentLength,
                                     List<Grant> accessControlList,
                                     InputStream data) throws S3Exception;

    /**
     * Creates a copy of an object when you specify the key and bucket of a
     * source object and the key and bucket of a target destination.
     * <p/>
     * When copying an object, you can preserve all metadata (default) or
     * specify new metadata. However, the ACL is not preserved and is set to
     * private for the user making the request. To override the default ACL
     * setting, specify a new ACL when generating a copy request.
     * <p/>
     * All copy requests must be authenticated. Additionally, you must have
     * read access to the source object and write access to the destination
     * bucket.
     * <p/>
     * To only copy an object under certain conditions, such as whether the
     * Etag matches or whether the object was modified before or after a
     * specified date, use the request parameters copyIfUnmodifiedSince,
     * copyIfUnmodifiedSince, copyIfMatch, or copyIfNoneMatch.
     *
     * @param sourceBucketName      The name of the source bucket.
     * @param sourceKey             The key name of the source object.
     * @param destBucketName        The name of the destination bucket.
     * @param destKey               The key of the destination object.
     * @param metadataDirective     Specifies whether the metadata is copied
     *                              from the source object or replaced with
     *                              metadata provided in the request.  The
     *                              default value is <code>COPY</code>.
     * @param metaData              Specifies metadata name-value pairs to set
     *                              for the object.  If MetadataDirective is
     *                              set to <code>COPY</code>, all metadata is
     *                              ignored.
     * @param accessControlList     Grants access to users by e-mail addresses
     *                              or canonical user ID.
     * @param copyIfMatch           Copies the object if its entity tag (ETag)
     *                              matches the specified tag; otherwise throw
     *                              a <code>PreconditionFailedException</code>.
     * @param copyIfNoMatch         Copies the object if its entity tag (ETag)
     *                              is different than the specified Etag;
     *                              otherwise throw an exception.
     * @param copyIfUnmodifiedSince Copies the object if it hasn't been
     *                              modified since the specified time;
     *                              otherwise throw a
     *                              <code>PreconditionFailed</code>.
     * @param copyIfModifiedSince   Copies the object if it has been modified
     *                              since the specified time; otherwise returns
     *                              an error.
     * @return a populated S3Object that represents the copied object
     * @throws S3Exception if any error occurs
     */
    FutureResult<S3Object> copyObject(String sourceBucketName, String sourceKey,
                                      String destBucketName, String destKey,
                                      MetadataDirective metadataDirective, Map<String, String> metaData,
                                      List<Grant> accessControlList,
                                      String copyIfMatch, String copyIfNoMatch,
                                      Date copyIfUnmodifiedSince, Date copyIfModifiedSince) throws S3Exception;

    /**
     * Retrieve an object stored in Amazon S3.
     * <p/>
     * You can read an object only if you have been granted <code>READ</code>
     * access to the object.
     *
     * @param bucketName        The bucket from which to retrieve the object.
     * @param key               The key that identifies the object.
     * @param ifMatch           Return the object only if its ETag matches the
     *                          supplied tag(s).
     * @param ifNoMatch         Return the object only if its ETag does not
     *                          match the supplied tag(s).
     * @param ifUnmodifiedSince Return the object only if the object's
     *                          timestamp is earlier than or equal to the
     *                          specified timestamp.
     * @param ifModifiedSince   Return the object only if the object's
     *                          timestamp is later than the specified timestamp.
     * @return a populated S3Object that represents the object
     * @throws S3Exception if any error occurs
     */
    FutureResult<S3Object> getObject(String bucketName, String key,
                                     String ifMatch, String ifNoMatch,
                                     Date ifUnmodifiedSince, Date ifModifiedSince) throws S3Exception;

    /**
     * Retrieve the data contents object stored in Amazon S3.
     * <p/>
     * You can read an object only if you have been granted <code>READ</code>
     * access to the object.
     *
     * @param bucketName        The bucket from which to retrieve the object.
     * @param key               The key that identifies the object.
     * @param start             The byte-offset of the first byte in a range.
     *                          Byte offsets start at zero. The byte positions
     *                          specified are inclusive.
     * @param end               The last byte in the range. The byte positions
     *                          specified are inclusive. A value that is larger
     *                          than the last byte position is reset to the
     *                          last byte postion.
     * @param ifMatch           Return the object only if its ETag matches the
     *                          supplied tag(s).
     * @param ifNoMatch         Return the object only if its ETag does not
     *                          match the supplied tag(s).
     * @param ifUnmodifiedSince Return the object only if the object's
     *                          timestamp is earlier than or equal to the
     *                          specified timestamp.
     * @param ifModifiedSince   Return the object only if the object's
     *                          timestamp is later than the specified timestamp.
     * @param returnCompleteObjectOnConditionFailure
     *                          If true, then if the request includes a range
     *                          element and one or both of IfUnmodifiedSince/IfMatch
     *                          elements, and the condition fails, return the
     *                          entire object rather than a fault. This enables
     *                          the If-Range functionality (go to
     *                          http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.27).
     * @return an input stream of the data contents of the object
     * @throws S3Exception if any error occurs
     */
    FutureResult<InputStream> getObjectData(String bucketName, String key,
                                            long start, long end,
                                            String ifMatch, String ifNoMatch,
                                            Date ifUnmodifiedSince, Date ifModifiedSince,
                                            boolean returnCompleteObjectOnConditionFailure) throws S3Exception;

    /**
     * Remove the specified object from Amazon S3. Once deleted, there is no
     * method to restore or undelete an object.
     * <p/>
     * You can delete an object only if you have <code>WRITE</code> access to
     * the bucket, regardless of who owns the object or what rights are granted
     * to it.
     *
     * @param bucketName The bucket that holds the object.
     * @param key        The key that identifies the object.
     * @return a {@link FutureResult} that facilitates waiting synchronously or asynchronously for completion
     * @throws S3Exception if any error occurs
     */
    FutureResult<Void> deleteObject(String bucketName, String key) throws S3Exception;

    /**
     * Fetches the access control policy for a object.
     * <p/>
     * You must have <code>READ_ACP</code> rights to the object in order to
     * retrieve the access control policy for a object.
     *
     * @param bucketName The name of the bucket whose access control policy you
     *                   are trying to obtain
     * @param key        The key that identifies the object.
     * @return the access control policy of the bucket
     * @throws S3Exception if any error occurs
     */
    FutureResult<AccessControlPolicy> getObjectAccessControlPolicy(String bucketName, String key) throws S3Exception;

    /**
     * Sets the Access Control Policy for an existing object. If successful,
     * the previous Access Control Policy for the bucket is entirely replaced
     * with the specified Access Control Policy.
     * <p/>
     * You must have <code>WRITE_ACP</code> rights to the bucket in order to
     * set the access control policy for a object.
     *
     * @param bucketName          The name of the bucket whose access control
     *                            policy you are trying to set
     * @param key                 The key that identifies the object.
     * @param accessControlPolicy the access control policy
     * @return a {@link FutureResult} that facilitates waiting synchronously or asynchronously for completion
     * @throws S3Exception if any error occurs
     */
    FutureResult<Void> setObjectAccessControlPolicy(String bucketName, String key, AccessControlPolicy accessControlPolicy) throws S3Exception;

}
