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
package org.livetribe.s3.rest.v20060301;

import java.net.URL;

import org.junit.Test;


/**
 * @version $Revision$ $Date: 2008-05-30 16:20:54 -0700 (Fri, 30 May 2008) $
 */
public class RestEC2APITest
{
    @Test
    public void empty() throws Exception
    {}

    public static void main(String[] args) throws Exception
    {
        RestS3API client = new RestS3API(new URL("https://s3.amazonaws.com"), args[0], args[1]);
        ((RestS3API) client).setTimeout(Integer.MAX_VALUE);
    }
}
