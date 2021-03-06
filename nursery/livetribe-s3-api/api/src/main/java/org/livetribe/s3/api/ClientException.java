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
package org.livetribe.s3.api;

/**
 * Client exceptions suggest that the error was caused by something the client did, such as an authentication failure or an invalid AMI identifier.
 *
 * @version $Revision$ $Date$
 */
public class ClientException extends S3Exception
{
    public ClientException()
    {
    }

    public ClientException(String message)
    {
        super(message);
    }

    public ClientException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ClientException(Throwable cause)
    {
        super(cause);
    }
}
