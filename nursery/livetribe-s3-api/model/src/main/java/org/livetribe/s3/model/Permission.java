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
 * The permission in a grant describes the type of access to be granted to the respective grantee.
 *
 * @version $Revision$ $Date$
 */
public enum Permission
{
    /**
     * Provides READ, WRITE, READ_ACP, and WRITE_ACP permissions.
     * <p/>
     * It does not convey additional rights and is provided only for convenience.
     */
    FULL_CONTROL,

    /**
     * When applied to a bucket, grants permission to list the bucket.
     * <p/>
     * When applied to an object, this grants permission to read the object data and/or metadata.
     */
    READ,

    /**
     * Grants permission to read the ACL for the applicable bucket or object.
     * <p/>
     * The owner of a bucket or object always has this permission implicitly.
     */
    READ_ACP,

    /**
     * When applied to a bucket, grants permission to create, overwrite, and delete any object in the bucket.
     * <p/>
     * This permission is not supported for objects.
     */
    WRITE,

    /**
     * Gives permission to overwrite the ACP for the applicable bucket or object.
     * <p/>
     * The owner of a bucket or object always has this permission implicitly.
     * <p/>
     * Granting this permission is equivalent to granting FULL_CONTROL because the grant recipient can make any changes to the ACP.
     */
    WRITE_ACP
}
