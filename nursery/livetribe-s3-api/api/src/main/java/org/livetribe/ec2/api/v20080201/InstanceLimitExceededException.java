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
 * User has max allowed concurrent running instances.
 * <p/>
 * Each user has a concurrent running instance limit. For new users, this limit is 20. If you need more than 20 instances, please complete the  Amazon EC2 Instance Request Form and your request will be considered.
 *
 * @version $Revision$ $Date: 2008-05-26 09:48:34 -0700 (Mon, 26 May 2008) $
 */
public class InstanceLimitExceededException extends ClientException
{
    public InstanceLimitExceededException()
    {
    }

    public InstanceLimitExceededException(String message)
    {
        super(message);
    }

    public InstanceLimitExceededException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InstanceLimitExceededException(Throwable cause)
    {
        super(cause);
    }
}
