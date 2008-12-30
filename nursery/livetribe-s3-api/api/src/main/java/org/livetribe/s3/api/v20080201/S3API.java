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
package org.livetribe.s3.api.v20080201;

import java.util.Set;

import org.livetribe.s3.api.S3Exception;
import org.livetribe.s3.model.AccessControlPolicy;
import org.livetribe.s3.model.Bucket;
import org.livetribe.s3.model.LoggingConfiguration;

/**
 * @version $Revision$ $Date$
 */
public interface S3API
{
    /**
     * Returns a list of all buckets owned by the sender of the request.
     *
     * @return a Set of all of the buckets owned by the authenticated sender of the request
     * @throws S3Exception if any error occurs
     */
    Set<Bucket> listAllMyBuckets() throws S3Exception;

    /**
     * Creates a bucket. Not every string is an acceptable bucket name. For
     * information on bucket naming restrictions, see
     * http://docs.amazonwebservices.com/AmazonS3/2006-03-01/UsingBucket.html.
     *
     * @param name The name of the bucket you are trying to create
     * @return the newly create bucket
     * @throws S3Exception if any error occurs
     */
    Bucket createBucket(String name) throws S3Exception;

    AccessControlPolicy getBucketAccessControlPolicy(String name) throws S3Exception;

    void setBucketAccessControlPolicy(String name, AccessControlPolicy accessControlPolicy) throws S3Exception;

    LoggingConfiguration getBucketLoggingConfiguration(String name) throws S3Exception;

    void setBucketLoggingConfiguration(String name, LoggingConfiguration loggingConfiguration) throws S3Exception;

    /**
     * Deletes a bucket. All objects in the bucket must be deleted before the
     * bucket itself can be deleted.
     *
     * @param name The name of the bucket you are trying to delete
     * @throws S3Exception if any error occurs
     */
    void deleteBucket(String name) throws S3Exception;
}
