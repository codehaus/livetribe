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

import org.livetribe.s3.api.ClientException;


/**
 * Specified instance ID is not valid.
 *
 * @version $Revision$ $Date: 2008-05-26 09:48:34 -0700 (Mon, 26 May 2008) $
 */
public class InvalidInstanceIDMalformedException extends ClientException
{
    public InvalidInstanceIDMalformedException()
    {
    }

    public InvalidInstanceIDMalformedException(String message)
    {
        super(message);
    }

    public InvalidInstanceIDMalformedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidInstanceIDMalformedException(Throwable cause)
    {
        super(cause);
    }
}
