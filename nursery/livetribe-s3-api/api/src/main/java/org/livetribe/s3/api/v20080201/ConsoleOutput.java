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

import java.util.Date;


/**
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public class ConsoleOutput
{
    private final String instanceId;
    private final Date timestamp;
    private final String output;

    public ConsoleOutput(String instanceId, Date timestamp, String output)
    {
        if (instanceId == null) throw new IllegalArgumentException("instanceId cannot be null");
        if (timestamp == null) throw new IllegalArgumentException("timestamp cannot be null");
        if (output == null) throw new IllegalArgumentException("output cannot be null");

        this.instanceId = instanceId;
        this.timestamp = timestamp;
        this.output = output;
    }

    public String getInstanceId()
    {
        return instanceId;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public String getOutput()
    {
        return output;
    }

    @Override
    public String toString()
    {
        return "(ConsoleOutput instanceId: " + instanceId + " timestamp: " + timestamp + " output: " + output + ")";
    }
}
