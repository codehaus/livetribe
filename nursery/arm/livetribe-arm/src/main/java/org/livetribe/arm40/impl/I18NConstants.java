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
package org.livetribe.arm40.impl;


/**
 * @version $Revision: $ $Date: $
 */
public interface I18NConstants
{
    public static final int GROUP_MASK = 0xF0000000;
    public static final int SUBSYSTEM_MASK = 0x0FF00000;
    public static final int CODE_MASK = 0x000FFFFF;

    public static final int METRIC_CODES = 0x10000000;
    public static final int TRANREPORT_CODES = 0x20000000;
    public static final int TRANSACTION_CODES = 0x30000000;
    public static final int GENERAL_CODES = 0x40000000;
    public static final int TEST_CODES = 0x50000000;

    public static final int SUCCESS = 0;
}

