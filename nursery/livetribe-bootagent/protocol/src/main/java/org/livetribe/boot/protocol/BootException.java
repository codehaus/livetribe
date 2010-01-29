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
package org.livetribe.boot.protocol;

/**
 * An exception use to indicate a problem with interacting with provision or
 * content providers.
 *
 * @version $Revision$ $Date$
 */
public final class BootException extends Exception
{
    /**
     * {@inheritDoc}
     */
    public BootException()
    {
    }

    /**
     * {@inheritDoc}
     */
    public BootException(String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public BootException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public BootException(Throwable cause)
    {
        super(cause);
    }
}
