/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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
package org.livetribe.boot.client;

import org.livetribe.boot.protocol.ProvisionEntry;


/**
 * A <code>MissingProvisionException</code> is thrown by a provision store if
 * content indicated by a provision entry is missing from the store.
 *
 * @version $Revision$ $Date$
 */
public class MissingProvisionException extends ProvisionStoreException
{
    private final ProvisionEntry missing;

    /**
     * Constructs a new exception with the provision entry that is missing
     * content and <code>null</code> as its detail message.  The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param missing the provision entry that is missing content in the provision store
     */
    public MissingProvisionException(ProvisionEntry missing)
    {
        this.missing = missing;
    }

    /**
     * Constructs a new exception with the provision entry that is missing
     * content and the specified detail message.  The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param missing the provision entry that is missing content in the provision store
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     */
    public MissingProvisionException(ProvisionEntry missing, String message)
    {
        super(message);
        this.missing = missing;
    }

    /**
     * Constructs a new exception with the provision entry that is missing
     * content and the specified detail message and cause. <p>Note that the
     * detail message associated with <code>cause</code> is <i>not</i>
     * automatically incorporated in this exception's detail message.
     *
     * @param missing the provision entry that is missing content in the provision store
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public MissingProvisionException(ProvisionEntry missing, String message, Throwable cause)
    {
        super(message, cause);
        this.missing = missing;
    }

    /**
     * Constructs a new exception with the provision entry that is missing
     * content and the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     *
     * @param missing the provision entry that is missing content in the provision store
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public MissingProvisionException(ProvisionEntry missing, Throwable cause)
    {
        super(cause);
        this.missing = missing;
    }

    /**
     * Obtain the provision entry that is missing content in the provision store.
     *
     * @return the provision entry that is missing content in the provision store
     */
    public ProvisionEntry getMissing()
    {
        return missing;
    }
}
