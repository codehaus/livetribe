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
package org.livetribe.ec2.api.v20080201;

import org.livetribe.ec2.api.ClientException;


/**
 * The value supplied for a parameter was invalid.
 * <p/>
 * Requests that could cause this error include (for example) supplying an invalid image attribute to the <code>DescribeImageAttribute</code> request or an invalid <code>version</code> or <code>encoding</code> value for the <code>userData</code> in a <code>RunInstances</code> request.
 *
 * @version $Revision$ $Date: 2008-05-26 09:48:34 -0700 (Mon, 26 May 2008) $
 */
public class InvalidParameterValueException extends ClientException
{
    public InvalidParameterValueException()
    {
    }

    public InvalidParameterValueException(String message)
    {
        super(message);
    }

    public InvalidParameterValueException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidParameterValueException(Throwable cause)
    {
        super(cause);
    }
}
