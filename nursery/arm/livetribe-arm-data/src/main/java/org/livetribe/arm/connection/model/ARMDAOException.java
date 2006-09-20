/**
 *  Copyright 2006 Simula Labs Inc., 4676 Admiralty Way, Suite 520 Marina del Rey, CA 90292 U.S.A. All rights reserved.
 */
package org.livetribe.arm.connection.model;

/**
 * @version $Revision$ $Date$
 */
public class ARMDAOException extends RuntimeException
{
    public ARMDAOException()
    {
    }

    public ARMDAOException(String message)
    {
        super(message);
    }

    public ARMDAOException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ARMDAOException(Throwable cause)
    {
        super(cause);
    }
}
