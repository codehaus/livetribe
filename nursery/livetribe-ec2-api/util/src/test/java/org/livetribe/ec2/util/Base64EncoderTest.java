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
package org.livetribe.ec2.util;

import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Assert;


/**
 * @version $Revision$ $Date$
 */
public class Base64EncoderTest
{
    @Test
    public void test() throws Exception
    {
        Assert.assertEquals("2006-12-08T07:48:03Z", URLDecoder.decode("2006-12-08T07%3A48%3A03Z", "UTF-8"));
        Assert.assertEquals("2006-12-08T07:48:03Z", URLDecoder.decode("2006-12-08T07:48:03Z", "UTF-8"));

        Map<String, String> data = new HashMap<String, String>();

        data.put("Action", "DescribeImages");
        data.put("Timestamp", "2006-12-08T07%3A48%3A03Z");
        data.put("AWSAccessKeyId", "10QMXFEV71ZS32XQFTR2");
        data.put("Version", "2007-01-03");
        data.put("SignatureVersion", "1");

        Assert.assertEquals("GjH3941IBe6qsgQu+k7FpCJjpnc=", Util.sign(data, "DMADSSfPfdaDjbK+RRUhS/aDrjsiZadgAUm8gRU2"));
    }
}
