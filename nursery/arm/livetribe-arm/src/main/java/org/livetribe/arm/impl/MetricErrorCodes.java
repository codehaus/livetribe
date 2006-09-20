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
public interface MetricErrorCodes extends GeneralErrorCodes
{
    public static final int APPLICATION_DEFINIITON_NULL = METRIC_CODES;
    public static final int GRP_DEF_ARRAY_INVALID = METRIC_CODES + 1;
    public static final int METRIC_DEFINIITON_NULL = METRIC_CODES + 2;
    public static final int METRIC_GRP_ARRAY_INVALID = METRIC_CODES + 3;
    public static final int APPLICATION_NULL = METRIC_CODES + 4;
    public static final int TRN_W_METRICS_DEFINITION_NULL = METRIC_CODES + 5;
    public static final int METRIC_GROUP_NULL = METRIC_CODES + 6;
}
