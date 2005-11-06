/**
 *
 * Copyright 2005 (C) The original author or authors
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
package org.livetribe.net.slp;

import java.util.Stack;


/**
 * The ServiceLocationException class is thrown by all methods when exceptional
 * conditions occur in the SLP framework. The error code property determines
 * the exact nature of the condition, and an optional message may provide more
 * information.
 *
 * @version $Revision: 1.2 $ $Date: 2005/05/18 23:18:36 $
 */
public abstract class ServiceLocationException extends Exception
{
    public static final short LANGUAGE_NOT_SUPPORTED = 1;
    public static final short PARSE_ERROR = 2;
    public static final short INVALID_REGISTRATION = 3;
    public static final short SCOPE_NOT_SUPPORTED = 4;
    public static final short AUTHENTICATION_ABSENT = 6;
    public static final short AUTHENTICATION_FAILED = 7;
    public static final short INVALID_UPDATE = 13;
    public static final short REFRESH_REJECTED = 15;
    public static final short NOT_IMPLEMENTED = 16;
    public static final short NETWORK_INIT_FAILED = 17;
    public static final short NETWORK_TIMED_OUT = 18;
    public static final short NETWORK_ERROR = 19;
    public static final short INTERNAL_SYSTEM_ERROR = 20;
    public static final short TYPE_ERROR = 21;
    public static final short BUFFER_OVERFLOW = 22;

    private final short errorCode;

    ServiceLocationException( short errorCode )
    {
        this.errorCode = errorCode;
    }

    public short getErrorCode()
    {
        return errorCode;
    }

    Stack s = new Stack();
}
