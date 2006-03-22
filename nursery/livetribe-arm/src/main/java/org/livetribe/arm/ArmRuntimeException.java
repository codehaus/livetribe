/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm;

/**
 * @version $Revision: $ $Date: $
 */
public class ArmRuntimeException extends RuntimeException
{
    private final int errorCode;

    public ArmRuntimeException(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public ArmRuntimeException(int errorCode, String string)
    {
        super(string);
        this.errorCode = errorCode;
    }

    public ArmRuntimeException(int errorCode, String string, Throwable throwable)
    {
        super(string, throwable);
        this.errorCode = errorCode;
    }

    public ArmRuntimeException(int errorCode, Throwable throwable)
    {
        super(throwable);
        this.errorCode = errorCode;
    }

    public int getErrorCode()
    {
        return errorCode;
    }
}
