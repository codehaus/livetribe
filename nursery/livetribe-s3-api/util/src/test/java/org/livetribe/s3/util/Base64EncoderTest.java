/**
 *
 * Copyright 2008-2011(C) The original author or authors
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
package org.livetribe.s3.util;

import java.net.URLDecoder;

import org.junit.Assert;
import org.junit.Test;


/**
 * @version $Revision$ $Date: 2008-05-26 11:43:27 -0700 (Mon, 26 May 2008) $
 */
public class Base64EncoderTest
{
    @Test
    public void test() throws Exception
    {
        Assert.assertEquals("2006-12-08T07:48:03Z", URLDecoder.decode("2006-12-08T07%3A48%3A03Z", "UTF-8"));
        Assert.assertEquals("2006-12-08T07:48:03Z", URLDecoder.decode("2006-12-08T07:48:03Z", "UTF-8"));
    }
}
