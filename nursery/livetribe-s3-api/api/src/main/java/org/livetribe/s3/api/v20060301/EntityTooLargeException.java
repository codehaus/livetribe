/**
 *
 * Copyright 2008-2011 (C) The original author or authors
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

import org.livetribe.s3.api.ClientException;

/**
 * Your proposed upload exceeds the maximum allowed object size.
 *
 * @version $Revision$ $Date$
 */
public class EntityTooLargeException extends ClientException
{
    public EntityTooLargeException()
    {
    }

    public EntityTooLargeException(String message)
    {
        super(message);
    }

    public EntityTooLargeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EntityTooLargeException(Throwable cause)
    {
        super(cause);
    }
}
