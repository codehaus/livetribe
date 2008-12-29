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
package org.livetribe.ec2.jaxb.v20080201;

import javax.xml.bind.JAXBContext;
import java.io.StringReader;

import org.junit.Test;


/**
 * @version $Revision$ $Date: 2008-05-30 09:40:48 -0700 (Fri, 30 May 2008) $
 */
public class Jaxb20080201Test
{
    @Test
    public void test() throws Exception
    {
        JAXBContext context20080201 = JAXBContext.newInstance("org.livetribe.ec2.jaxb.v20080201");

        context20080201.createUnmarshaller().unmarshal(new StringReader("<?xml version=\"1.0\"?>\n" +
                                                                        "<DescribeImagesResponse xmlns=\"http://ec2.amazonaws.com/doc/2008-02-01/\">\n" +
                                                                        "  <imagesSet>\n" +
                                                                        "    <item>\n" +
                                                                        "      <imageId>ami-be3adfd7</imageId>\n" +
                                                                        "      <imageLocation>ec2-public-images/fedora-8-i386-base-v1.04.manifest.xml</imageLocation>\n" +
                                                                        "      <imageState>available</imageState>\n" +
                                                                        "      <imageOwnerId>206029621532</imageOwnerId>\n" +
                                                                        "      <isPublic>false</isPublic>\n" +
                                                                        "      <architecture>i386</architecture>\n" +
                                                                        "      <imageType>machine</imageType>\n" +
                                                                        "      <kernelId>aki-4438dd2d</kernelId>\n" +
                                                                        "      <ramdiskId>ari-4538dd2c</ramdiskId>\n" +
                                                                        "    </item>\n" +
                                                                        "  </imagesSet>\n" +
                                                                        "</DescribeImagesResponse>"));
    }
}
