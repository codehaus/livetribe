/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm.impl;

/**
 * @version $Revision: $ $Date: $
 */
public interface GeneralErrorCodes extends I18NConstants
{
    public static final int UNEXPECTED_ERROR = GENERAL_CODES + 0;
    public static final int USING_INVALID_OBJECT = GENERAL_CODES + 1;
    public static final int NAME_NULL_OR_EMPTY = GENERAL_CODES + 2;
    public static final int NAME_LENGTH_LARGE = GENERAL_CODES + 3;
    public static final int GROUP_LENGTH_LARGE = GENERAL_CODES + 4;
    public static final int CTX_VAL_ARRAY_LONG = GENERAL_CODES + 5;
}
