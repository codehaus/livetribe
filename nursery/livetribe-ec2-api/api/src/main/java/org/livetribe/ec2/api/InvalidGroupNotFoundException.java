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
package org.livetribe.ec2.api;

/**
 * Specified group name does not exist.
 *
 * @version $Revision$ $Date$
 */
public class InvalidGroupNotFoundException extends ClientException
{
    public InvalidGroupNotFoundException()
    {
    }

    public InvalidGroupNotFoundException(String message)
    {
        super(message);
    }

    public InvalidGroupNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidGroupNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
