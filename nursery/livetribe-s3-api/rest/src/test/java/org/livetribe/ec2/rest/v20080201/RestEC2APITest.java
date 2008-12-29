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
package org.livetribe.ec2.rest.v20080201;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import org.livetribe.ec2.api.v20080201.EC2API;
import org.livetribe.ec2.api.v20080201.KeyPair;
import org.livetribe.ec2.model.AmazonImage;


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
        EC2API client = new RestEC2API(new URL("https://ec2.amazonaws.com"), args[0], args[1]);
        ((RestEC2API) client).setTimeout(Integer.MAX_VALUE);

        Set<AmazonImage> images = client.describeImages(new String[]{"ami-3948ad50"}, null, null);
        images = client.describeImages(null, null, null);
        images = client.describeImages(null, new String[]{"063491364108"}, null);
        images = client.describeImages(null, null, new String[]{"self"});

        KeyPair pair = client.createKeyPair("cabrera");
        List<KeyPair> pairs = client.describeKeyPairs(new String[]{"cabrera"});
        client.deleteKeyPair("foo");
        client.deleteKeyPair("cabrera");
    }
}
